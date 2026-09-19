package com.example.feature.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.playback.PlaybackController
import com.example.core.repository.AudioRepository
import com.example.core.ui.BMEmptyState
import com.example.core.ui.BMSongRow
import com.example.core.ui.BMTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAudioScreen(
    category: AudioCategory,
    audioRepository: AudioRepository,
    playbackController: PlaybackController,
    onBack: () -> Unit,
    onShowDetails: (Audio) -> Unit,
    onEditCategory: (Audio) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val audioList by audioRepository.getByCategory(category).collectAsState(initial = emptyList())
    val playbackState by playbackController.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("category_audio_screen_${category.name}")
    ) {
        BMTopBar(
            title = category.displayName,
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (audioList.isEmpty()) {
            BMEmptyState(
                icon = Icons.Default.Category,
                title = "No ${category.displayName} Found",
                description = "Tracks automatically classified as ${category.displayName} will appear here."
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(audioList, key = { _, audio -> audio.id }) { index, audio ->
                    BMSongRow(
                        audio = audio,
                        isSelected = playbackState.currentAudio?.id == audio.id,
                        isPlaying = playbackState.isPlaying && playbackState.currentAudio?.id == audio.id,
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.ALL, allowedCategories = setOf(category), sourceTitle = category.displayName)
                            playbackController.setQueue(context, audioList, index, true)
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
