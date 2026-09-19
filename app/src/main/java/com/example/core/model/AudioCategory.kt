package com.example.core.model

enum class AudioCategory {
    MUSIC,
    VOICE_RECORDING,
    WHATSAPP_AUDIO,
    SOUND_EFFECT,
    PODCAST,
    AUDIOBOOK,
    DOWNLOAD,
    RINGTONE,
    NOTIFICATION,
    ALARM,
    OTHER;

    val displayName: String
        get() = when (this) {
            MUSIC -> "Music"
            VOICE_RECORDING -> "Voice Recording"
            WHATSAPP_AUDIO -> "WhatsApp Audio"
            SOUND_EFFECT -> "Sound Effect"
            PODCAST -> "Podcast"
            AUDIOBOOK -> "Audiobook"
            DOWNLOAD -> "Download"
            RINGTONE -> "Ringtone"
            NOTIFICATION -> "Notification"
            ALARM -> "Alarm"
            OTHER -> "Other"
        }
}

enum class ClassificationSource {
    METADATA_AND_PATH,
    PATH_RULE,
    METADATA_RULE,
    RECORDINGS_PATH,
    WHATSAPP_PATH,
    SFX_PATH,
    PODCAST_PATH,
    AUDIOBOOK_PATH,
    SYSTEM_AUDIO,
    FALLBACK,
    USER
}
