package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.database.entity.AudioEntity
import com.example.core.database.entity.PlaylistEntity
import com.example.core.database.entity.PlaylistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists ORDER BY modifiedAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id LIMIT 1")
    suspend fun getPlaylistById(id: Long): PlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("UPDATE playlists SET name = :name, modifiedAt = :timestamp WHERE id = :id")
    suspend fun updatePlaylistName(id: Long, name: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylist(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: PlaylistItemEntity): Long

    @Query("DELETE FROM playlist_items WHERE playlistId = :playlistId AND audioId = :audioId")
    suspend fun removeItem(playlistId: Long, audioId: Long)

    @Query("""
        SELECT a.* FROM audio a
        INNER JOIN playlist_items pi ON a.id = pi.audioId
        WHERE pi.playlistId = :playlistId
        ORDER BY pi.sortOrder ASC
    """)
    fun getAudiosForPlaylist(playlistId: Long): Flow<List<AudioEntity>>

    @Query("SELECT COUNT(*) FROM playlist_items WHERE playlistId = :playlistId")
    fun getPlaylistItemCount(playlistId: Long): Flow<Int>
}
