package com.example.core.common

import java.util.Locale

object Formatters {

    fun formatDuration(durationMs: Long): String {
        if (durationMs <= 0) return "0:00"
        val totalSeconds = durationMs / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600

        return if (hours > 0) {
            String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
        }
    }

    fun formatFileSize(sizeBytes: Long): String {
        if (sizeBytes <= 0) return "0 B"
        val kb = sizeBytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0

        return when {
            gb >= 1.0 -> String.format(Locale.ROOT, "%.2f GB", gb)
            mb >= 1.0 -> String.format(Locale.ROOT, "%.1f MB", mb)
            kb >= 1.0 -> String.format(Locale.ROOT, "%.0f KB", kb)
            else -> "$sizeBytes B"
        }
    }

    fun formatBitrate(bitrate: Int?): String {
        if (bitrate == null || bitrate <= 0) return "N/A"
        return "${bitrate / 1000} kbps"
    }

    fun formatSampleRate(sampleRate: Int?): String {
        if (sampleRate == null || sampleRate <= 0) return "N/A"
        return String.format(Locale.ROOT, "%.1f kHz", sampleRate / 1000.0)
    }
}
