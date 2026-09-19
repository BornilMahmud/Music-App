package com.example.core.recording

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.SystemClock
import com.example.core.database.AppDatabase
import com.example.core.database.entity.AudioEntity
import com.example.core.model.AudioCategory
import com.example.core.model.ClassificationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RecorderState(
    val isRecording: Boolean = false,
    val isPaused: Boolean = false,
    val elapsedSeconds: Long = 0L,
    val currentFile: File? = null,
    val errorMessage: String? = null
)

class VoiceRecorderManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val db = AppDatabase.getInstance(context)
    private val audioDao = db.audioDao()

    private var mediaRecorder: MediaRecorder? = null
    private var timerJob: Job? = null
    private var currentOutputFile: File? = null
    private var startTimeMillis: Long = 0L
    private var accumulatedSeconds: Long = 0L

    private val _state = MutableStateFlow(RecorderState())
    val state: StateFlow<RecorderState> = _state.asStateFlow()

    fun startRecording(): Boolean {
        try {
            val recordingsDir = File(context.filesDir, "BMRecordings").apply { mkdirs() }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(recordingsDir, "BM_REC_$timeStamp.m4a")
            currentOutputFile = file

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }

            this.mediaRecorder = recorder
            startTimeMillis = SystemClock.elapsedRealtime()
            accumulatedSeconds = 0L

            _state.value = RecorderState(
                isRecording = true,
                isPaused = false,
                elapsedSeconds = 0L,
                currentFile = file
            )

            startTimer()
            return true
        } catch (e: Exception) {
            _state.value = RecorderState(errorMessage = e.localizedMessage ?: "Failed to start recording")
            return false
        }
    }

    fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && _state.value.isRecording && !_state.value.isPaused) {
            try {
                mediaRecorder?.pause()
                timerJob?.cancel()
                val delta = (SystemClock.elapsedRealtime() - startTimeMillis) / 1000
                accumulatedSeconds += delta
                _state.update { it.copy(isPaused = true) }
            } catch (_: Exception) {}
        }
    }

    fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && _state.value.isRecording && _state.value.isPaused) {
            try {
                mediaRecorder?.resume()
                startTimeMillis = SystemClock.elapsedRealtime()
                _state.update { it.copy(isPaused = false) }
                startTimer()
            } catch (_: Exception) {}
        }
    }

    suspend fun stopRecording(): File? = withContext(Dispatchers.IO) {
        timerJob?.cancel()
        var savedFile: File? = null
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            savedFile = currentOutputFile

            if (savedFile != null && savedFile.exists() && savedFile.length() > 0) {
                // Register in Room as VOICE_RECORDING
                val durationMs = (_state.value.elapsedSeconds * 1000L).coerceAtLeast(1000L)
                val entity = AudioEntity(
                    id = System.currentTimeMillis(),
                    uri = savedFile.toURI().toString(),
                    title = savedFile.nameWithoutExtension,
                    artist = "BM Voice Recorder",
                    album = "Voice Recordings",
                    albumArtist = "BM Player",
                    genre = "Voice",
                    fileName = savedFile.name,
                    filePath = savedFile.absolutePath,
                    mimeType = "audio/mp4",
                    durationMs = durationMs,
                    sizeBytes = savedFile.length(),
                    dateAdded = System.currentTimeMillis() / 1000,
                    dateModified = System.currentTimeMillis() / 1000,
                    bitrate = 128000,
                    sampleRate = 44100,
                    channels = 1,
                    codec = "AAC",
                    hasArtwork = false,
                    automaticCategory = AudioCategory.VOICE_RECORDING,
                    manualCategory = null,
                    effectiveCategory = AudioCategory.VOICE_RECORDING,
                    classificationSource = ClassificationSource.RECORDINGS_PATH,
                    classificationConfidence = 0.99f
                )
                audioDao.insert(entity)
            }
        } catch (e: Exception) {
            // Stop failed
        } finally {
            currentOutputFile = null
            _state.value = RecorderState()
        }
        savedFile
    }

    fun cancelRecording() {
        timerJob?.cancel()
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (_: Exception) {}
        mediaRecorder = null
        currentOutputFile?.delete()
        currentOutputFile = null
        _state.value = RecorderState()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (isActive && _state.value.isRecording && !_state.value.isPaused) {
                delay(1000L)
                val currentDelta = (SystemClock.elapsedRealtime() - startTimeMillis) / 1000
                _state.update { it.copy(elapsedSeconds = accumulatedSeconds + currentDelta) }
            }
        }
    }
}
