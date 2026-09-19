package com.example.core.playback

import com.example.core.model.Audio
import com.example.core.model.PlaybackContext

enum class RepeatMode {
    OFF,
    ONE,
    ALL;

    fun next(): RepeatMode = when (this) {
        OFF -> ALL
        ALL -> ONE
        ONE -> OFF
    }
}

data class PlaybackState(
    val currentAudio: Audio? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val queue: List<Audio> = emptyList(),
    val queueIndex: Int = -1,
    val shuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val context: PlaybackContext? = null,
    val playbackSpeed: Float = 1.0f,
    val sleepTimerRemainingSeconds: Long? = null
) {
    val hasNext: Boolean
        get() = queue.isNotEmpty() && (repeatMode != RepeatMode.OFF || queueIndex < queue.size - 1 || shuffleEnabled)

    val hasPrevious: Boolean
        get() = queue.isNotEmpty() && (queueIndex > 0 || positionMs > 3000L || repeatMode != RepeatMode.OFF)
}
