package com.example.core.repository

import android.content.Context
import com.example.core.database.AppDatabase
import com.example.core.database.entity.AudioEntity
import com.example.core.database.entity.CategoryOverrideEntity
import com.example.core.media.MediaStoreScanner
import com.example.core.media.ScanProgress
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.io.File

data class FolderInfo(
    val name: String,
    val path: String,
    val audioCount: Int
)

class AudioRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val audioDao = db.audioDao()
    private val overrideDao = db.categoryOverrideDao()
    private val scanner = MediaStoreScanner(context)

    val scanProgress: StateFlow<ScanProgress> = scanner.scanProgress

    fun getAllMusic(): Flow<List<Audio>> =
        audioDao.getAllMusic().map { list -> list.map { it.toAudio() } }

    fun getByCategory(category: AudioCategory): Flow<List<Audio>> =
        audioDao.getByCategory(category).map { list -> list.map { it.toAudio() } }

    suspend fun getByCategoryList(category: AudioCategory): List<Audio> =
        audioDao.getByCategoryList(category).map { it.toAudio() }

    fun getRecentlyAddedMusic(limit: Int = 50): Flow<List<Audio>> =
        audioDao.getRecentlyAddedMusic(limit).map { list -> list.map { it.toAudio() } }

    fun getFavoriteMusic(): Flow<List<Audio>> =
        audioDao.getFavoriteMusic().map { list -> list.map { it.toAudio() } }

    fun getMusicCount(): Flow<Int> = audioDao.getMusicCount()

    fun getFavoriteMusicCount(): Flow<Int> = audioDao.getFavoriteMusicCount()

    fun getAllFavorites(): Flow<List<Audio>> =
        audioDao.getAllFavorites().map { list -> list.map { it.toAudio() } }

    fun getRecentlyPlayed(limit: Int = 30): Flow<List<Audio>> =
        audioDao.getRecentlyPlayed(limit).map { list -> list.map { it.toAudio() } }

    fun getAllAudio(): Flow<List<Audio>> =
        audioDao.getAllAudio().map { list -> list.map { it.toAudio() } }

    suspend fun getAudioById(id: Long): Audio? =
        audioDao.getAudioById(id)?.toAudio()

    fun observeAudioById(id: Long): Flow<Audio?> =
        audioDao.observeAudioById(id).map { it?.toAudio() }

    fun getCategoryCounts(): Flow<Map<AudioCategory, Int>> =
        audioDao.getCategoryCounts().map { list ->
            list.associate { it.effectiveCategory to it.count }
        }

    fun searchAll(query: String): Flow<List<Audio>> =
        audioDao.searchAll(query).map { list -> list.map { it.toAudio() } }

    fun searchCategory(query: String, category: AudioCategory): Flow<List<Audio>> =
        audioDao.searchCategory(query, category).map { list -> list.map { it.toAudio() } }

    fun getAudioInFolder(folderPath: String): Flow<List<Audio>> =
        audioDao.getAudioInFolder(folderPath).map { list -> list.map { it.toAudio() } }

    fun getFolders(): Flow<List<FolderInfo>> =
        audioDao.getAllFilePaths().map { paths ->
            val folderMap = mutableMapOf<String, Int>()
            paths.forEach { path ->
                val parent = File(path).parent
                if (!parent.isNullOrBlank()) {
                    folderMap[parent] = (folderMap[parent] ?: 0) + 1
                }
            }
            folderMap.map { (path, count) ->
                FolderInfo(
                    name = File(path).name,
                    path = path,
                    audioCount = count
                )
            }.sortedBy { it.name.lowercase() }
        }

    suspend fun setFavorite(id: Long, isFavorite: Boolean) {
        audioDao.setFavorite(id, isFavorite)
    }

    suspend fun setManualCategory(audio: Audio, newCategory: AudioCategory) {
        // Save override in category_overrides table so it survives future rescans (Rule 6)
        overrideDao.insertOverride(
            CategoryOverrideEntity(
                uri = audio.uri,
                filePath = audio.filePath,
                manualCategory = newCategory
            )
        )
        // Update audio entity in Room
        audioDao.setManualCategory(audio.id, newCategory)
    }

    suspend fun updatePlaybackStats(id: Long, positionMs: Long) {
        audioDao.updatePlaybackStats(id, positionMs, System.currentTimeMillis())
    }

    suspend fun deleteAudio(id: Long) {
        audioDao.deleteById(id)
    }

    suspend fun scanLibrary(incremental: Boolean = false): ScanProgress {
        return scanner.scanLibrary(incremental)
    }
}
