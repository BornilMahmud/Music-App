package com.example.core.model

enum class PlaybackSource {
    MUSIC,
    FAVORITES,
    RECENTLY_ADDED,
    PLAYLIST,
    VOICE_RECORDINGS,
    SOUND_EFFECTS,
    PODCASTS,
    AUDIOBOOKS,
    RINGTONES,
    FOLDER,
    SEARCH,
    ALL
}

data class PlaybackContext(
    val source: PlaybackSource,
    val sourceId: String? = null,
    val sourceTitle: String = "",
    val allowedCategories: Set<AudioCategory>? = null
) {
    fun isAllowed(audio: Audio): Boolean {
        if (allowedCategories != null) {
            return audio.effectiveCategory in allowedCategories
        }
        return when (source) {
            PlaybackSource.MUSIC,
            PlaybackSource.RECENTLY_ADDED -> audio.effectiveCategory == AudioCategory.MUSIC
            PlaybackSource.FAVORITES -> true // Or contextual
            PlaybackSource.VOICE_RECORDINGS -> audio.effectiveCategory == AudioCategory.VOICE_RECORDING
            PlaybackSource.SOUND_EFFECTS -> audio.effectiveCategory == AudioCategory.SOUND_EFFECT
            PlaybackSource.PODCASTS -> audio.effectiveCategory == AudioCategory.PODCAST
            PlaybackSource.AUDIOBOOKS -> audio.effectiveCategory == AudioCategory.AUDIOBOOK
            PlaybackSource.RINGTONES -> audio.effectiveCategory == AudioCategory.RINGTONE
            PlaybackSource.FOLDER -> true
            PlaybackSource.PLAYLIST -> true
            PlaybackSource.SEARCH -> true
            PlaybackSource.ALL -> true
        }
    }
}
