package com.example.core.classification

import com.example.core.model.AudioCategory
import com.example.core.model.ClassificationSource
import java.io.File
import java.util.Locale

data class ClassificationResult(
    val category: AudioCategory,
    val confidence: Float,
    val source: ClassificationSource
)

object ClassificationEngine {

    fun classify(
        filePath: String?,
        fileName: String,
        title: String,
        artist: String? = null,
        album: String? = null,
        genre: String? = null,
        durationMs: Long = 0L,
        mimeType: String? = null
    ): ClassificationResult {
        val pathLower = (filePath ?: "").lowercase(Locale.ROOT)
        val nameLower = fileName.lowercase(Locale.ROOT)
        val titleLower = title.lowercase(Locale.ROOT)
        val genreLower = (genre ?: "").lowercase(Locale.ROOT)
        val artistClean = if (artist.isNullOrBlank() || artist == "<unknown>") null else artist
        val albumClean = if (album.isNullOrBlank() || album == "<unknown>") null else album

        // 1. System audio: Ringtones, Notifications, Alarms (Section 14)
        if (pathLower.contains("/ringtone") || pathLower.contains("/ringtones")) {
            return ClassificationResult(AudioCategory.RINGTONE, 0.99f, ClassificationSource.SYSTEM_AUDIO)
        }
        if (pathLower.contains("/notification") || pathLower.contains("/notifications")) {
            return ClassificationResult(AudioCategory.NOTIFICATION, 0.99f, ClassificationSource.SYSTEM_AUDIO)
        }
        if (pathLower.contains("/alarm") || pathLower.contains("/alarms")) {
            return ClassificationResult(AudioCategory.ALARM, 0.99f, ClassificationSource.SYSTEM_AUDIO)
        }

        // 2. WhatsApp Voice Notes and Audio (Strict separation from music library)
        val isWhatsAppPath = pathLower.contains("whatsapp") ||
                pathLower.contains("com.whatsapp") ||
                pathLower.contains("whatsapp audio") ||
                pathLower.contains("whatsapp voice")
        val isWhatsAppFile = nameLower.startsWith("ptt-") ||
                (nameLower.startsWith("aud-") && nameLower.contains("-wa")) ||
                nameLower.contains("whatsapp")

        if (isWhatsAppPath || isWhatsAppFile) {
            return ClassificationResult(AudioCategory.WHATSAPP_AUDIO, 0.99f, ClassificationSource.WHATSAPP_PATH)
        }

        // 3. Voice recordings (Section 10)
        val isVoicePath = pathLower.contains("/recordings") ||
                pathLower.contains("/voice recorder") ||
                pathLower.contains("/sound recorder") ||
                pathLower.contains("/call recordings") ||
                pathLower.contains("/callrecording") ||
                pathLower.contains("/voice") ||
                pathLower.contains("/audio_recorder")
        val isVoiceFileName = nameLower.contains("recording") ||
                nameLower.contains("voice") ||
                nameLower.contains("memo") ||
                nameLower.contains("recorder") ||
                nameLower.contains("call_recording") ||
                nameLower.startsWith("rec_") ||
                nameLower.startsWith("voice_") ||
                nameLower.startsWith("bm_rec_")

        if (isVoicePath) {
            val confidence = if (isVoiceFileName) 0.98f else 0.90f
            return ClassificationResult(AudioCategory.VOICE_RECORDING, confidence, ClassificationSource.RECORDINGS_PATH)
        }
        if (isVoiceFileName && (artistClean == null || artistClean.equals("recorder", ignoreCase = true))) {
            return ClassificationResult(AudioCategory.VOICE_RECORDING, 0.85f, ClassificationSource.RECORDINGS_PATH)
        }

        // 3. Sound Effects (SFX) (Section 11)
        val isSfxPath = pathLower.contains("/sound effects") ||
                pathLower.contains("/sfx") ||
                pathLower.contains("/effects") ||
                pathLower.contains("/sounds")
        val isSfxName = nameLower.contains("sfx") ||
                nameLower.contains("effect") ||
                nameLower.contains("whoosh") ||
                nameLower.contains("impact") ||
                nameLower.contains("transition") ||
                nameLower.contains("explosion") ||
                nameLower.contains("foley") ||
                nameLower.contains("ambience") ||
                nameLower.contains("click") ||
                nameLower.contains("beep")

        if (isSfxPath) {
            val confidence = if (isSfxName) 0.98f else 0.90f
            return ClassificationResult(AudioCategory.SOUND_EFFECT, confidence, ClassificationSource.SFX_PATH)
        }
        if (isSfxName && durationMs in 100..45000) {
            return ClassificationResult(AudioCategory.SOUND_EFFECT, 0.85f, ClassificationSource.SFX_PATH)
        }

        // 4. Podcasts (Section 12)
        val isPodcastPath = pathLower.contains("/podcasts") || pathLower.contains("/podcast")
        val isPodcastMeta = genreLower.contains("podcast") ||
                titleLower.contains("podcast") ||
                nameLower.contains("podcast") ||
                nameLower.contains("episode") ||
                nameLower.contains("ep.")

        if (isPodcastPath || genreLower.contains("podcast")) {
            return ClassificationResult(AudioCategory.PODCAST, 0.95f, ClassificationSource.PODCAST_PATH)
        }
        if (isPodcastMeta && durationMs > 10 * 60 * 1000) { // 10+ minutes
            return ClassificationResult(AudioCategory.PODCAST, 0.88f, ClassificationSource.PODCAST_PATH)
        }

        // 5. Audiobooks (Section 13)
        val isAudiobookPath = pathLower.contains("/audiobooks") ||
                pathLower.contains("/audio books") ||
                pathLower.contains("/books")
        val isAudiobookMeta = genreLower.contains("audiobook") ||
                genreLower.contains("book") ||
                titleLower.contains("chapter")

        if (isAudiobookPath || genreLower.contains("audiobook")) {
            return ClassificationResult(AudioCategory.AUDIOBOOK, 0.95f, ClassificationSource.AUDIOBOOK_PATH)
        }
        if (isAudiobookMeta && durationMs > 15 * 60 * 1000) {
            return ClassificationResult(AudioCategory.AUDIOBOOK, 0.85f, ClassificationSource.AUDIOBOOK_PATH)
        }

        // 6. Music (Section 9)
        // Strong music paths
        val isMusicPath = pathLower.contains("/music") ||
                pathLower.contains("/songs") ||
                pathLower.contains("/spotify") ||
                pathLower.contains("/apple music") ||
                pathLower.contains("/itunes")

        val hasValidMetadata = artistClean != null || albumClean != null
        val standardAudioDuration = durationMs > 30 * 1000 // typical song is > 30 seconds

        if (isMusicPath && hasValidMetadata) {
            return ClassificationResult(AudioCategory.MUSIC, 0.96f, ClassificationSource.METADATA_AND_PATH)
        }
        if (isMusicPath) {
            return ClassificationResult(AudioCategory.MUSIC, 0.90f, ClassificationSource.PATH_RULE)
        }
        if (hasValidMetadata && standardAudioDuration) {
            return ClassificationResult(AudioCategory.MUSIC, 0.85f, ClassificationSource.METADATA_RULE)
        }

        // 7. General Downloads inspection (Section 15)
        if (pathLower.contains("/download")) {
            if (hasValidMetadata && standardAudioDuration) {
                return ClassificationResult(AudioCategory.MUSIC, 0.82f, ClassificationSource.METADATA_RULE)
            }
            if (durationMs > 15 * 60 * 1000) {
                return ClassificationResult(AudioCategory.PODCAST, 0.70f, ClassificationSource.FALLBACK)
            }
            return ClassificationResult(AudioCategory.DOWNLOAD, 0.75f, ClassificationSource.PATH_RULE)
        }

        // Fallback: If duration is reasonable and not explicitly matching other categories, consider MUSIC or OTHER
        return if (durationMs > 45 * 1000) {
            ClassificationResult(AudioCategory.MUSIC, 0.65f, ClassificationSource.FALLBACK)
        } else {
            ClassificationResult(AudioCategory.OTHER, 0.50f, ClassificationSource.FALLBACK)
        }
    }
}
