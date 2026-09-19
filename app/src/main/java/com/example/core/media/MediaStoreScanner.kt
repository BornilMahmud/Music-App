package com.example.core.media

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.core.classification.ClassificationEngine
import com.example.core.database.AppDatabase
import com.example.core.database.entity.AudioEntity
import com.example.core.model.AudioCategory
import com.example.core.model.ClassificationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File

data class ScanProgress(
    val isScanning: Boolean = false,
    val totalFound: Int = 0,
    val musicCount: Int = 0,
    val voiceCount: Int = 0,
    val sfxCount: Int = 0,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
)

class MediaStoreScanner(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val audioDao = db.audioDao()
    private val overrideDao = db.categoryOverrideDao()

    private val _scanProgress = MutableStateFlow(ScanProgress())
    val scanProgress: StateFlow<ScanProgress> = _scanProgress.asStateFlow()

    suspend fun scanLibrary(incremental: Boolean = false): ScanProgress = withContext(Dispatchers.IO) {
        _scanProgress.value = ScanProgress(isScanning = true)
        try {
            // 1. Fetch persistent manual overrides
            val manualOverrides = overrideDao.getAllOverrides().associateBy { it.uri }

            // 2. Query MediaStore
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME
            )

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 OR ${MediaStore.Audio.Media.DURATION} > 0"
            val sortOrder = "${MediaStore.Audio.Media.DATE_MODIFIED} DESC"

            val audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? = context.contentResolver.query(
                audioUri,
                projection,
                selection,
                null,
                sortOrder
            )

            // Ultra-Low RAM Streaming: chunk into 100-item batches directly to Room DB
            val batch = ArrayList<AudioEntity>(100)
            val foundUris = mutableSetOf<String>()
            var totalFound = 0
            var music = 0
            var voice = 0
            var whatsapp = 0
            var sfx = 0

            cursor?.use { c ->
                val idCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val albumIdCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val durationCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val dateAddedCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
                val dateModCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
                val mimeCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
                val dataCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val nameCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

                while (c.moveToNext()) {
                    val id = c.getLong(idCol)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()
                    foundUris.add(contentUri)

                    val title = c.getString(titleCol) ?: "Unknown Title"
                    val artist = c.getString(artistCol)
                    val album = c.getString(albumCol)
                    val albumId = c.getLong(albumIdCol)
                    val duration = c.getLong(durationCol)
                    val size = c.getLong(sizeCol)
                    val dateAdded = c.getLong(dateAddedCol)
                    val dateModified = c.getLong(dateModCol)
                    val mimeType = c.getString(mimeCol)
                    val data = c.getString(dataCol)
                    val displayName = c.getString(nameCol) ?: (data?.let { File(it).name } ?: "audio.mp3")

                    // Artwork availability
                    val hasArtwork = albumId > 0

                    // Check classification
                    val classification = ClassificationEngine.classify(
                        filePath = data,
                        fileName = displayName,
                        title = title,
                        artist = artist,
                        album = album,
                        genre = null,
                        durationMs = duration,
                        mimeType = mimeType
                    )

                    // Check manual override (Rule 6)
                    val override = manualOverrides[contentUri]
                    val manualCategory = override?.manualCategory
                    val effectiveCategory = manualCategory ?: classification.category
                    val finalSource = if (manualCategory != null) ClassificationSource.USER else classification.source

                    when (effectiveCategory) {
                        AudioCategory.MUSIC -> music++
                        AudioCategory.VOICE_RECORDING -> voice++
                        AudioCategory.WHATSAPP_AUDIO -> whatsapp++
                        AudioCategory.SOUND_EFFECT -> sfx++
                        else -> {}
                    }

                    batch.add(
                        AudioEntity(
                            id = id,
                            uri = contentUri,
                            title = title,
                            artist = artist,
                            album = album,
                            albumArtist = null,
                            genre = null,
                            fileName = displayName,
                            filePath = data,
                            mimeType = mimeType,
                            durationMs = duration,
                            sizeBytes = size,
                            dateAdded = dateAdded,
                            dateModified = dateModified,
                            bitrate = null,
                            sampleRate = null,
                            channels = null,
                            codec = mimeType?.substringAfter('/')?.uppercase(),
                            hasArtwork = hasArtwork,
                            automaticCategory = classification.category,
                            manualCategory = manualCategory,
                            effectiveCategory = effectiveCategory,
                            classificationSource = finalSource,
                            classificationConfidence = classification.confidence
                        )
                    )
                    totalFound++

                    // Stream directly into database every 100 items to free heap memory (O(1) RAM usage)
                    if (batch.size >= 100) {
                        audioDao.insertAll(batch)
                        batch.clear()
                    }
                }
            }

            // Flush any remaining records
            if (batch.isNotEmpty()) {
                audioDao.insertAll(batch)
                batch.clear()
            }

            // Detect and remove deleted tracks in batches
            val existingUris = audioDao.getAllUris()
            val deletedUris = existingUris.filterNot { foundUris.contains(it) }
            if (deletedUris.isNotEmpty()) {
                deletedUris.chunked(200).forEach { chunk ->
                    audioDao.deleteByUris(chunk)
                }
            }

            val finalProgress = ScanProgress(
                isScanning = false,
                totalFound = totalFound,
                musicCount = music,
                voiceCount = voice,
                sfxCount = sfx,
                isCompleted = true
            )
            _scanProgress.value = finalProgress
            finalProgress
        } catch (e: Exception) {
            val errProgress = ScanProgress(
                isScanning = false,
                isCompleted = true,
                errorMessage = e.localizedMessage ?: "Scanning failed"
            )
            _scanProgress.value = errProgress
            errProgress
        }
    }
}
