package com.example.core.playback

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SleepTimerManager(
    private val scope: CoroutineScope,
    private val onTimerExpired: () -> Unit
) {
    private var timerJob: Job? = null
    private var isEndOfTrack = false

    private val _remainingSeconds = MutableStateFlow<Long?>(null)
    val remainingSeconds: StateFlow<Long?> = _remainingSeconds.asStateFlow()

    fun startTimer(minutes: Int) {
        cancel()
        isEndOfTrack = false
        val totalSeconds = minutes * 60L
        _remainingSeconds.value = totalSeconds

        timerJob = scope.launch(Dispatchers.Default) {
            var current = totalSeconds
            while (isActive && current > 0) {
                delay(1000L)
                current--
                _remainingSeconds.value = current
            }
            if (isActive) {
                _remainingSeconds.value = null
                onTimerExpired()
            }
        }
    }

    fun setEndOfTrack() {
        cancel()
        isEndOfTrack = true
        _remainingSeconds.value = -1L // Marker for End of Track
    }

    fun onTrackCompleted() {
        if (isEndOfTrack) {
            isEndOfTrack = false
            _remainingSeconds.value = null
            onTimerExpired()
        }
    }

    fun cancel() {
        timerJob?.cancel()
        timerJob = null
        isEndOfTrack = false
        _remainingSeconds.value = null
    }
}
