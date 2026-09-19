package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.database.entity.AudioEntity
import com.example.core.model.AudioCategory
import kotlinx.coroutines.flow.Flow

data class CategoryCount(
    val effectiveCategory: AudioCategory,
    val count: Int
)

@Dao
interface AudioDao {

    @Query("SELECT * FROM audio WHERE effectiveCategory = 'MUSIC' ORDER BY title COLLATE NOCASE ASC")
    fun getAllMusic(): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE effectiveCategory = :category ORDER BY title COLLATE NOCASE ASC")
    fun getByCategory(category: AudioCategory): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE effectiveCategory = :category ORDER BY title COLLATE NOCASE ASC")
    suspend fun getByCategoryList(category: AudioCategory): List<AudioEntity>

    @Query("SELECT * FROM audio WHERE effectiveCategory = 'MUSIC' ORDER BY CASE WHEN dateAdded > 0 THEN dateAdded ELSE dateModified END DESC, id DESC LIMIT :limit")
    fun getRecentlyAddedMusic(limit: Int = 50): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE effectiveCategory = 'MUSIC' AND isFavorite = 1 ORDER BY title COLLATE NOCASE ASC")
    fun getFavoriteMusic(): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE isFavorite = 1 ORDER BY title COLLATE NOCASE ASC")
    fun getAllFavorites(): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE lastPlayedAt IS NOT NULL ORDER BY lastPlayedAt DESC LIMIT :limit")
    fun getRecentlyPlayed(limit: Int = 30): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio ORDER BY title COLLATE NOCASE ASC")
    fun getAllAudio(): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE id = :id LIMIT 1")
    suspend fun getAudioById(id: Long): AudioEntity?

    @Query("SELECT * FROM audio WHERE id = :id LIMIT 1")
    fun observeAudioById(id: Long): Flow<AudioEntity?>

    @Query("SELECT * FROM audio WHERE uri = :uri LIMIT 1")
    suspend fun getAudioByUri(uri: String): AudioEntity?

    @Query("SELECT uri FROM audio")
    suspend fun getAllUris(): List<String>

    @Query("SELECT filePath FROM audio WHERE filePath IS NOT NULL")
    fun getAllFilePaths(): Flow<List<String>>

    @Query("SELECT effectiveCategory, COUNT(*) as count FROM audio GROUP BY effectiveCategory")
    fun getCategoryCounts(): Flow<List<CategoryCount>>

    @Query("SELECT COUNT(*) FROM audio WHERE effectiveCategory = 'MUSIC'")
    fun getMusicCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM audio WHERE effectiveCategory = 'MUSIC' AND isFavorite = 1")
    fun getFavoriteMusicCount(): Flow<Int>

    @Query("SELECT * FROM audio WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%' ORDER BY title COLLATE NOCASE ASC")
    fun searchAll(query: String): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE effectiveCategory = :category AND (title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%') ORDER BY title COLLATE NOCASE ASC")
    fun searchCategory(query: String, category: AudioCategory): Flow<List<AudioEntity>>

    @Query("SELECT * FROM audio WHERE filePath LIKE :folderPrefix || '%' ORDER BY title COLLATE NOCASE ASC")
    fun getAudioInFolder(folderPrefix: String): Flow<List<AudioEntity>>

    @Query("UPDATE audio SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE audio SET manualCategory = :category, effectiveCategory = :category WHERE id = :id")
    suspend fun setManualCategory(id: Long, category: AudioCategory)

    @Query("UPDATE audio SET playCount = playCount + 1, lastPlayedAt = :timestamp, playbackPositionMs = :positionMs WHERE id = :id")
    suspend fun updatePlaybackStats(id: Long, positionMs: Long, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<AudioEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AudioEntity): Long

    @Update
    suspend fun update(entity: AudioEntity)

    @Query("DELETE FROM audio WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM audio WHERE uri IN (:uris)")
    suspend fun deleteByUris(uris: List<String>)

    @Query("SELECT COUNT(*) FROM audio")
    fun getTotalCount(): Flow<Int>
}
