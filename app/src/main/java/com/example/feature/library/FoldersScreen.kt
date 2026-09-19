package com.example.feature.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.Audio
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.playback.PlaybackController
import com.example.core.repository.AudioRepository
import com.example.core.repository.FolderInfo
import com.example.core.ui.BMEmptyState
import com.example.core.ui.BMSongRow
import com.example.core.ui.BMTopBar
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
    audioRepository: AudioRepository,
    playbackController: PlaybackController,
    onBack: (() -> Unit)? = null,
    onShowDetails: (Audio) -> Unit,
    onEditCategory: (Audio) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val folders by audioRepository.getFolders().collectAsState(initial = emptyList())
    val playbackState by playbackController.state.collectAsState()

    var selectedFolder by remember { mutableStateOf<FolderInfo?>(null) }
    val selectedPath = selectedFolder?.path
    val folderAudios by remember(selectedPath) {
        if (selectedPath != null) {
            audioRepository.getAudioInFolder(selectedPath)
        } else {
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("folders_screen")
    ) {
        BMTopBar(
            title = selectedFolder?.name ?: "Folders",
            navigationIcon = {
                IconButton(onClick = {
                    if (selectedFolder != null) {
                        selectedFolder = null
                    } else {
                        onBack?.invoke()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (selectedFolder == null) {
            if (folders.isEmpty()) {
                BMEmptyState(
                    icon = Icons.Default.Folder,
                    title = "No Audio Folders Found",
                    description = "BM Player scans directories containing audio files on device storage."
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(folders, key = { it.path }) { folder ->
                        Surface(
                            color = BmDarkSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedFolder = folder }
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
                                        Icon(
                                            imageVector = Icons.Default.Folder,
                                            contentDescription = null,
                                            tint = BmElectricBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = folder.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${folder.audioCount} files • ${folder.path}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 12.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Viewing audio files inside selected folder
            if (folderAudios.isEmpty()) {
                BMEmptyState(
                    icon = Icons.Default.Folder,
                    title = "Folder is Empty",
                    description = "No playable audio found in this folder."
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(folderAudios, key = { _, audio -> audio.id }) { index, audio ->
                        BMSongRow(
                            audio = audio,
                            isSelected = playbackState.currentAudio?.id == audio.id,
                            isPlaying = playbackState.isPlaying && playbackState.currentAudio?.id == audio.id,
                            onClick = {
                                val context = PlaybackContext(PlaybackSource.FOLDER, sourceTitle = selectedFolder?.name ?: "Folder")
                                playbackController.setQueue(context, folderAudios, index, true)
                            },
                            onFavoriteToggle = {
                                scope.launch { audioRepository.setFavorite(audio.id, !audio.isFavorite) }
                            },
                            onPlayNext = { playbackController.playNext(audio) },
                            onAddToQueue = { playbackController.addToQueue(audio) },
                            onShowDetails = { onShowDetails(audio) },
                            onEditCategory = { onEditCategory(audio) },
                            onDelete = { scope.launch { audioRepository.deleteAudio(audio.id) } },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
