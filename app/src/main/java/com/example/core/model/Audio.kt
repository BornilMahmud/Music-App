package com.example.core.model

data class Audio(
    val id: Long,
    val uri: String,
    val title: String,
    val artist: String?,
    val album: String?,
    val albumArtist: String?,
    val genre: String?,
    val fileName: String,
    val filePath: String?,
    val mimeType: String?,
    val durationMs: Long,
    val sizeBytes: Long,
    val dateAdded: Long,
    val dateModified: Long,
    val bitrate: Int? = null,
    val sampleRate: Int? = null,
    val channels: Int? = null,
    val codec: String? = null,
    val hasArtwork: Boolean = false,
    val automaticCategory: AudioCategory = AudioCategory.OTHER,
    val manualCategory: AudioCategory? = null,
    val classificationSource: ClassificationSource = ClassificationSource.FALLBACK,
    val classificationConfidence: Float = 0.5f,
    val isFavorite: Boolean = false,
    val playCount: Int = 0,
    val lastPlayedAt: Long? = null,
    val playbackPositionMs: Long = 0L
) {
    val effectiveCategory: AudioCategory
        get() = manualCategory ?: automaticCategory

    val displayArtist: String
        get() = if (artist.isNullOrBlank() || artist == "<unknown>") "Unknown Artist" else artist

    val displayAlbum: String
        get() = if (album.isNullOrBlank() || album == "<unknown>") "Unknown Album" else album
}
