package com.example.feature.playlists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.database.entity.PlaylistEntity
import com.example.core.model.Audio
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.playback.PlaybackController
import com.example.core.repository.AudioRepository
import com.example.core.repository.PlaylistRepository
import com.example.core.ui.BMEmptyState
import com.example.core.ui.BMSongRow
import com.example.core.ui.BMTopBar
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(
    playlistRepository: PlaylistRepository,
    audioRepository: AudioRepository,
    playbackController: PlaybackController,
    onBack: (() -> Unit)? = null,
    onShowDetails: (Audio) -> Unit,
    onEditCategory: (Audio) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val playlists by playlistRepository.getAllPlaylists().collectAsState(initial = emptyList())
    val playbackState by playbackController.state.collectAsState()

    var selectedPlaylist by remember { mutableStateOf<PlaylistEntity?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }

    val selectedPlaylistId = selectedPlaylist?.id
    val playlistAudios by remember(selectedPlaylistId) {
        if (selectedPlaylistId != null) {
            playlistRepository.getAudiosForPlaylist(selectedPlaylistId)
        } else {
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("playlists_screen")
    ) {
        BMTopBar(
            title = selectedPlaylist?.name ?: "Playlists",
            navigationIcon = {
                if (selectedPlaylist != null) {
                    IconButton(onClick = { selectedPlaylist = null }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                } else onBack?.let {
                    IconButton(onClick = it) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            },
            actions = {
                if (selectedPlaylist == null) {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Create Playlist")
                    }
                }
            }
        )

        if (selectedPlaylist == null) {
            if (playlists.isEmpty()) {
                BMEmptyState(
                    icon = Icons.Default.QueueMusic,
                    title = "No Playlists Yet",
                    description = "Create custom playlists to organize your favorite music.",
                    actionLabel = "New Playlist",
                    onAction = { showCreateDialog = true }
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(playlists, key = { it.id }) { playlist ->
                        Surface(
                            color = BmDarkSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedPlaylist = playlist }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = BmDarkSurfaceHighlight,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.PlaylistPlay, contentDescription = null, tint = BmElectricBlue, modifier = Modifier.size(24.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = playlist.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    )
                                    Text(
                                        text = "Custom playlist",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch { playlistRepository.deletePlaylist(playlist.id) }
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Playlist", tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Viewing tracks inside selected playlist
            val playlist = selectedPlaylist!!
            if (playlistAudios.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.PLAYLIST, sourceTitle = playlist.name)
                            playbackController.setQueue(context, playlistAudios, 0, true)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BmElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Play All", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.PLAYLIST, sourceTitle = playlist.name)
                            playbackController.setQueue(context, playlistAudios, 0, true)
                            playbackController.toggleShuffle()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BmDarkSurfaceHighlight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Icon(Icons.Default.Shuffle, contentDescription = null, tint = BmElectricBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Shuffle", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (playlistAudios.isEmpty()) {
                BMEmptyState(
                    icon = Icons.Default.QueueMusic,
                    title = "Playlist is Empty",
                    description = "Add songs to this playlist from the song menu."
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(playlistAudios, key = { _, audio -> audio.id }) { index, audio ->
                        BMSongRow(
                            audio = audio,
                            isSelected = playbackState.currentAudio?.id == audio.id,
                            isPlaying = playbackState.isPlaying && playbackState.currentAudio?.id == audio.id,
                            onClick = {
                                val context = PlaybackContext(PlaybackSource.PLAYLIST, sourceTitle = playlist.name)
                                playbackController.setQueue(context, playlistAudios, index, true)
                            },
                            onFavoriteToggle = {
                                scope.launch { audioRepository.setFavorite(audio.id, !audio.isFavorite) }
                            },
                            onPlayNext = { playbackController.playNext(audio) },
                            onAddToQueue = { playbackController.addToQueue(audio) },
                            onShowDetails = { onShowDetails(audio) },
                            onEditCategory = { onEditCategory(audio) },
                            onDelete = {
                                scope.launch { playlistRepository.removeAudioFromPlaylist(playlist.id, audio.id) }
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
                newPlaylistName = ""
            },
            title = { Text("New Playlist") },
            text = {
                OutlinedTextField(
                    value = newPlaylistName,
                    onValueChange = { newPlaylistName = it },
                    placeholder = { Text("Playlist name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newPlaylistName.isNotBlank()) {
                            scope.launch {
                                playlistRepository.createPlaylist(newPlaylistName.trim())
                                newPlaylistName = ""
                                showCreateDialog = false
                            }
                        }
                    }
                ) {
                    Text("Create", color = BmElectricBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateDialog = false
                        newPlaylistName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
