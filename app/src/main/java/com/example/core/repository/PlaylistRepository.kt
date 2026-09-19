package com.example.core.repository

import android.content.Context
import com.example.core.database.AppDatabase
import com.example.core.database.entity.PlaylistEntity
import com.example.core.database.entity.PlaylistItemEntity
import com.example.core.model.Audio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val playlistDao = db.playlistDao()

    fun getAllPlaylists(): Flow<List<PlaylistEntity>> =
        playlistDao.getAllPlaylists()

    suspend fun createPlaylist(name: String): Long =
        playlistDao.insertPlaylist(PlaylistEntity(name = name))

    suspend fun renamePlaylist(id: Long, newName: String) =
        playlistDao.updatePlaylistName(id, newName)

    suspend fun deletePlaylist(id: Long) =
        playlistDao.deletePlaylist(id)

    suspend fun addAudioToPlaylist(playlistId: Long, audioId: Long) {
        playlistDao.insertItem(
            PlaylistItemEntity(
                playlistId = playlistId,
                audioId = audioId
            )
        )
    }

    suspend fun removeAudioFromPlaylist(playlistId: Long, audioId: Long) {
        playlistDao.removeItem(playlistId, audioId)
    }

    fun getAudiosForPlaylist(playlistId: Long): Flow<List<Audio>> =
        playlistDao.getAudiosForPlaylist(playlistId).map { list -> list.map { it.toAudio() } }

    fun getPlaylistItemCount(playlistId: Long): Flow<Int> =
        playlistDao.getPlaylistItemCount(playlistId)
}
