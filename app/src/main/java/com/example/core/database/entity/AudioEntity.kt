package com.example.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.model.ClassificationSource

@Entity(
    tableName = "audio",
    indices = [
        Index(value = ["effectiveCategory"]),
        Index(value = ["title"]),
        Index(value = ["artist"]),
        Index(value = ["album"]),
        Index(value = ["dateAdded"]),
        Index(value = ["lastPlayedAt"]),
        Index(value = ["isFavorite"]),
        Index(value = ["filePath"]),
        Index(value = ["uri"], unique = true)
    ]
)
data class AudioEntity(
    @PrimaryKey val id: Long,
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
    val bitrate: Int?,
    val sampleRate: Int?,
    val channels: Int?,
    val codec: String?,
    val hasArtwork: Boolean,
    val automaticCategory: AudioCategory,
    val manualCategory: AudioCategory?,
    val effectiveCategory: AudioCategory,
    val classificationSource: ClassificationSource,
    val classificationConfidence: Float,
    val isFavorite: Boolean = false,
    val playCount: Int = 0,
    val lastPlayedAt: Long? = null,
    val playbackPositionMs: Long = 0L
) {
    fun toAudio(): Audio = Audio(
        id = id,
        uri = uri,
        title = title,
        artist = artist,
        album = album,
        albumArtist = albumArtist,
        genre = genre,
        fileName = fileName,
        filePath = filePath,
        mimeType = mimeType,
        durationMs = durationMs,
        sizeBytes = sizeBytes,
        dateAdded = dateAdded,
        dateModified = dateModified,
        bitrate = bitrate,
        sampleRate = sampleRate,
        channels = channels,
        codec = codec,
        hasArtwork = hasArtwork,
        automaticCategory = automaticCategory,
        manualCategory = manualCategory,
        classificationSource = classificationSource,
        classificationConfidence = classificationConfidence,
        isFavorite = isFavorite,
        playCount = playCount,
        lastPlayedAt = lastPlayedAt,
        playbackPositionMs = playbackPositionMs
    )

    companion object {
        fun fromAudio(audio: Audio): AudioEntity = AudioEntity(
            id = audio.id,
            uri = audio.uri,
            title = audio.title,
            artist = audio.artist,
            album = audio.album,
            albumArtist = audio.albumArtist,
            genre = audio.genre,
            fileName = audio.fileName,
            filePath = audio.filePath,
            mimeType = audio.mimeType,
            durationMs = audio.durationMs,
            sizeBytes = audio.sizeBytes,
            dateAdded = audio.dateAdded,
            dateModified = audio.dateModified,
            bitrate = audio.bitrate,
            sampleRate = audio.sampleRate,
            channels = audio.channels,
            codec = audio.codec,
            hasArtwork = audio.hasArtwork,
            automaticCategory = audio.automaticCategory,
            manualCategory = audio.manualCategory,
            effectiveCategory = audio.effectiveCategory,
            classificationSource = audio.classificationSource,
            classificationConfidence = audio.classificationConfidence,
            isFavorite = audio.isFavorite,
            playCount = audio.playCount,
            lastPlayedAt = audio.lastPlayedAt,
            playbackPositionMs = audio.playbackPositionMs
        )
    }
}
