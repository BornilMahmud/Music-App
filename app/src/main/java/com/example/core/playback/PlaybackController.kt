package com.example.core.playback

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.repository.AudioRepository
import com.example.service.playback.PlaybackService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlaybackController private constructor(private val appContext: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val audioRepository = AudioRepository(appContext)

    private val _state = MutableStateFlow(PlaybackState())
    val state: StateFlow<PlaybackState> = _state.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    private val smartShuffle = SmartShuffle<Audio>()
    val sfxRandomBag = SfxRandomBag()
    var sfxMode: SfxPlayMode = SfxPlayMode.RANDOM

    private var progressJob: Job? = null

    val sleepTimer = SleepTimerManager(scope) {
        pause()
    }

    init {
        scope.launch {
            sleepTimer.remainingSeconds.collect { rem ->
                _state.update { it.copy(sleepTimerRemainingSeconds = rem) }
            }
        }
    }

    fun attachPlayer(player: ExoPlayer, session: MediaSession) {
        this.exoPlayer = player
        this.mediaSession = session

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.update { it.copy(isPlaying = isPlaying) }
                if (isPlaying) {
                    startProgressTracker()
                } else {
                    stopProgressTracker()
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val currentMediaId = mediaItem?.mediaId?.toLongOrNull() ?: return
                val currentQueue = _state.value.queue
                val foundIndex = currentQueue.indexOfFirst { it.id == currentMediaId }
                if (foundIndex >= 0) {
                    val audio = currentQueue[foundIndex]
                    _state.update {
                        it.copy(
                            queueIndex = foundIndex,
                            currentAudio = audio,
                            durationMs = audio.durationMs,
                            positionMs = 0L
                        )
                    }
                    scope.launch(Dispatchers.IO) {
                        audioRepository.updatePlaybackStats(audio.id, 0L)
                    }
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    sleepTimer.onTrackCompleted()
                    onTrackEnded()
                }
            }
        })
    }

    fun detachPlayer() {
        this.exoPlayer = null
        this.mediaSession = null
        stopProgressTracker()
    }

    private fun ensureServiceRunning() {
        PlaybackService.start(appContext)
    }

    fun setQueue(
        context: PlaybackContext,
        items: List<Audio>,
        startIndex: Int = 0,
        playImmediately: Boolean = true
    ) {
        ensureServiceRunning()
        if (items.isEmpty()) return

        // Global Playback Safety (Section 107): filter to ensure category isolation
        val safeItems = items.filter { context.isAllowed(it) }
        if (safeItems.isEmpty()) return

        val safeStartIndex = startIndex.coerceIn(0, safeItems.size - 1)
        val selectedAudio = safeItems[safeStartIndex]

        smartShuffle.setList(safeItems, selectedAudio)
        if (context.source == PlaybackSource.SOUND_EFFECTS) {
            sfxRandomBag.setEffects(safeItems)
        }

        _state.update {
            it.copy(
                queue = safeItems,
                queueIndex = safeStartIndex,
                currentAudio = selectedAudio,
                context = context,
                durationMs = selectedAudio.durationMs,
                positionMs = 0L
            )
        }

        val player = exoPlayer
        if (player != null) {
            val mediaItems = safeItems.map { audio ->
                val mediaMetadata = MediaMetadata.Builder()
                    .setTitle(audio.title)
                    .setArtist(audio.displayArtist)
                    .setAlbumTitle(audio.displayAlbum)
                    .setArtworkUri(Uri.parse(audio.uri))
                    .build()

                MediaItem.Builder()
                    .setUri(Uri.parse(audio.uri))
                    .setMediaId(audio.id.toString())
                    .setMediaMetadata(mediaMetadata)
                    .build()
            }

            player.setMediaItems(mediaItems, safeStartIndex, 0L)
            player.prepare()
            if (selectedAudio.playbackPositionMs > 0 &&
                (selectedAudio.effectiveCategory == AudioCategory.PODCAST || selectedAudio.effectiveCategory == AudioCategory.AUDIOBOOK)) {
                player.seekTo(safeStartIndex, selectedAudio.playbackPositionMs)
            }
            player.setPlaybackSpeed(_state.value.playbackSpeed)
            if (playImmediately) {
                player.play()
            }
        } else {
            playAudioInternal(selectedAudio, playImmediately)
        }

        // Record play history in database
        scope.launch(Dispatchers.IO) {
            audioRepository.updatePlaybackStats(selectedAudio.id, 0L)
        }
    }

    fun playAudioInContext(audio: Audio, context: PlaybackContext) {
        setQueue(context, listOf(audio), startIndex = 0, playImmediately = true)
    }

    private fun playAudioInternal(audio: Audio, playImmediately: Boolean) {
        ensureServiceRunning()
        val player = exoPlayer ?: return

        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(audio.title)
            .setArtist(audio.displayArtist)
            .setAlbumTitle(audio.displayAlbum)
            .setArtworkUri(Uri.parse(audio.uri))
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(audio.uri))
            .setMediaId(audio.id.toString())
            .setMediaMetadata(mediaMetadata)
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        if (audio.playbackPositionMs > 0 && (audio.effectiveCategory == AudioCategory.PODCAST || audio.effectiveCategory == AudioCategory.AUDIOBOOK)) {
            player.seekTo(audio.playbackPositionMs)
        }
        player.setPlaybackSpeed(_state.value.playbackSpeed)
        if (playImmediately) {
            player.play()
        }

        // Record play history in database
        scope.launch(Dispatchers.IO) {
            audioRepository.updatePlaybackStats(audio.id, 0L)
        }
    }

    fun play() {
        ensureServiceRunning()
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun togglePlayPause() {
        val player = exoPlayer
        if (player != null) {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        } else {
            // Player not yet created, start if queue has audio
            _state.value.currentAudio?.let {
                playAudioInternal(it, true)
            }
        }
    }

    fun next() {
        val player = exoPlayer
        val currentState = _state.value
        val queue = currentState.queue
        if (queue.isEmpty()) return

        // Special SFX mode check (Section 42-44)
        if (currentState.context?.source == PlaybackSource.SOUND_EFFECTS) {
            when (sfxMode) {
                SfxPlayMode.SINGLE_PLAY -> {
                    pause()
                    seekTo(0)
                    return
                }
                SfxPlayMode.REPEAT -> {
                    seekTo(0)
                    play()
                    return
                }
                SfxPlayMode.RANDOM -> {
                    val nextSfx = sfxRandomBag.nextRandom() ?: return
                    val nextIdx = queue.indexOfFirst { it.id == nextSfx.id }.coerceAtLeast(0)
                    moveToQueueItem(nextIdx, nextSfx)
                    return
                }
            }
        }

        if (currentState.shuffleEnabled) {
            val nextShuffled = smartShuffle.next()
            if (nextShuffled != null) {
                val nextIdx = queue.indexOfFirst { it.id == nextShuffled.id }.coerceAtLeast(0)
                moveToQueueItem(nextIdx, nextShuffled)
                return
            }
        }

        if (player != null && player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
            if (!player.isPlaying) {
                player.play()
            }
        } else {
            val nextIndex = currentState.queueIndex + 1
            if (nextIndex < queue.size) {
                moveToQueueItem(nextIndex, queue[nextIndex])
            } else if (currentState.repeatMode == RepeatMode.ALL) {
                moveToQueueItem(0, queue[0])
            } else {
                // End of queue reached
                pause()
            }
        }
    }

    fun previous() {
        val currentState = _state.value
        val player = exoPlayer
        if (player != null && player.currentPosition > 3000L) {
            player.seekTo(0)
            return
        }

        val queue = currentState.queue
        if (queue.isEmpty()) return

        if (player != null && player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
            if (!player.isPlaying) {
                player.play()
            }
        } else {
            val prevIndex = currentState.queueIndex - 1
            if (prevIndex >= 0) {
                moveToQueueItem(prevIndex, queue[prevIndex])
            } else if (currentState.repeatMode == RepeatMode.ALL) {
                val lastIndex = queue.size - 1
                moveToQueueItem(lastIndex, queue[lastIndex])
            } else {
                player?.seekTo(0)
            }
        }
    }

    private fun moveToQueueItem(index: Int, audio: Audio) {
        // Validate category safety (Rule 108 & Rule 155)
        val context = _state.value.context
        if (context != null && !context.isAllowed(audio)) {
            return
        }

        _state.update {
            it.copy(
                queueIndex = index,
                currentAudio = audio,
                durationMs = audio.durationMs,
                positionMs = 0L
            )
        }

        val player = exoPlayer
        if (player != null && index in 0 until player.mediaItemCount) {
            player.seekTo(index, 0L)
            if (!player.isPlaying) {
                player.play()
            }
        } else {
            playAudioInternal(audio, playImmediately = true)
        }
    }

    private fun onTrackEnded() {
        val currentState = _state.value
        if (currentState.repeatMode == RepeatMode.ONE) {
            exoPlayer?.seekTo(0)
            exoPlayer?.play()
        } else {
            next()
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _state.update { it.copy(positionMs = positionMs) }
    }

    fun toggleShuffle() {
        _state.update {
            val nextShuffle = !it.shuffleEnabled
            if (nextShuffle && it.queue.isNotEmpty()) {
                smartShuffle.setList(it.queue, it.currentAudio)
            }
            it.copy(shuffleEnabled = nextShuffle)
        }
    }

    fun toggleRepeat() {
        _state.update { it.copy(repeatMode = it.repeatMode.next()) }
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer?.setPlaybackSpeed(speed)
        _state.update { it.copy(playbackSpeed = speed) }
    }

    fun addToQueue(audio: Audio) {
        _state.update {
            val updatedQueue = it.queue + audio
            it.copy(queue = updatedQueue)
        }
    }

    fun playNext(audio: Audio) {
        _state.update {
            val curIdx = it.queueIndex
            val mutable = it.queue.toMutableList()
            if (curIdx in mutable.indices) {
                mutable.add(curIdx + 1, audio)
            } else {
                mutable.add(audio)
            }
            it.copy(queue = mutable)
        }
    }

    fun removeFromQueue(index: Int) {
        _state.update {
            if (index in it.queue.indices) {
                val mutable = it.queue.toMutableList()
                mutable.removeAt(index)
                val newIndex = when {
                    mutable.isEmpty() -> -1
                    index < it.queueIndex -> it.queueIndex - 1
                    index == it.queueIndex -> index.coerceAtMost(mutable.size - 1)
                    else -> it.queueIndex
                }
                val current = if (newIndex in mutable.indices) mutable[newIndex] else null
                it.copy(queue = mutable, queueIndex = newIndex, currentAudio = current)
            } else it
        }
    }

    fun reorderQueue(fromIndex: Int, toIndex: Int) {
        _state.update {
            if (fromIndex in it.queue.indices && toIndex in it.queue.indices) {
                val mutable = it.queue.toMutableList()
                val item = mutable.removeAt(fromIndex)
                mutable.add(toIndex, item)
                val newIndex = when (it.queueIndex) {
                    fromIndex -> toIndex
                    in (fromIndex + 1)..toIndex -> it.queueIndex - 1
                    in toIndex until fromIndex -> it.queueIndex + 1
                    else -> it.queueIndex
                }
                it.copy(queue = mutable, queueIndex = newIndex)
            } else it
        }
    }

    fun clearQueue() {
        pause()
        _state.update {
            it.copy(
                queue = emptyList(),
                queueIndex = -1,
                currentAudio = null,
                positionMs = 0L,
                durationMs = 0L
            )
        }
    }

    private var isUiVisible: Boolean = true

    /**
     * Called by UI components when the app enters/leaves foreground.
     * When in background or screen is off, the 500ms ticker is halted to let CPU sleep.
     */
    fun setUiVisible(visible: Boolean) {
        if (isUiVisible == visible) return
        isUiVisible = visible
        if (visible) {
            // Sync immediately once on returning to foreground
            exoPlayer?.let { player ->
                val pos = player.currentPosition
                val dur = if (player.duration > 0) player.duration else _state.value.durationMs
                _state.update { it.copy(positionMs = pos, durationMs = dur) }
            }
            if (_state.value.isPlaying) {
                startProgressTracker()
            }
        } else {
            // Halt 500ms ticker when app is in background or screen is off to save battery
            stopProgressTracker()
        }
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        if (!isUiVisible) return // Save battery: do not poll when UI is not visible
        progressJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    val pos = player.currentPosition
                    val dur = if (player.duration > 0) player.duration else _state.value.durationMs
                    _state.update { it.copy(positionMs = pos, durationMs = dur) }
                }
                delay(500L)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    companion object {
        @Volatile
        private var INSTANCE: PlaybackController? = null

        fun getInstance(context: Context): PlaybackController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PlaybackController(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
