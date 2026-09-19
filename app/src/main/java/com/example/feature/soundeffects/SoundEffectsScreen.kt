package com.example.feature.soundeffects

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.playback.PlaybackController
import com.example.core.playback.SfxPlayMode
import com.example.core.repository.AudioRepository
import com.example.core.ui.BMEmptyState
import com.example.core.ui.BMSongRow
import com.example.core.ui.BMTopBar
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmElectricBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundEffectsScreen(
    audioRepository: AudioRepository,
    playbackController: PlaybackController,
    onBack: (() -> Unit)? = null,
    onShowDetails: (Audio) -> Unit,
    onEditCategory: (Audio) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val sfxList by audioRepository.getByCategory(AudioCategory.SOUND_EFFECT).collectAsState(initial = emptyList())
    val playbackState by playbackController.state.collectAsState()

    var currentSfxMode by remember { mutableStateOf(playbackController.sfxMode) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("sfx_screen")
    ) {
        BMTopBar(
            title = "Sound Effects",
            navigationIcon = onBack?.let {
                {
                    IconButton(onClick = it) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        )

        // Mode Selector Chips (Section 44: Single Play, Repeat, Random)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mode:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            SfxPlayMode.values().forEach { mode ->
                FilterChip(
                    selected = (currentSfxMode == mode),
                    onClick = {
                        currentSfxMode = mode
                        playbackController.sfxMode = mode
                    },
                    label = { Text(mode.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BmElectricBlue,
                        selectedLabelColor = androidx.compose.ui.graphics.Color.White
                    )
                )
            }
        }

        if (sfxList.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val context = PlaybackContext(PlaybackSource.SOUND_EFFECTS, sourceTitle = "Sound Effects")
                        playbackController.setQueue(context, sfxList, 0, true)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BmElectricBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Play First", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val context = PlaybackContext(PlaybackSource.SOUND_EFFECTS, sourceTitle = "SFX Random")
                        playbackController.sfxMode = SfxPlayMode.RANDOM
                        currentSfxMode = SfxPlayMode.RANDOM
                        playbackController.setQueue(context, sfxList, 0, true)
                        playbackController.next()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BmDarkSurfaceHighlight),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Icon(Icons.Default.Shuffle, contentDescription = null, tint = BmElectricBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Random SFX", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (sfxList.isEmpty()) {
            BMEmptyState(
                icon = Icons.Default.SpatialAudio,
                title = "No Sound Effects Found",
                description = "Sound effects are kept completely isolated from your music library.",
                actionLabel = "Scan Library",
                onAction = { scope.launch { audioRepository.scanLibrary() } }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(sfxList, key = { _, audio -> audio.id }) { index, audio ->
                    BMSongRow(
                        audio = audio,
                        isSelected = playbackState.currentAudio?.id == audio.id,
                        isPlaying = playbackState.isPlaying && playbackState.currentAudio?.id == audio.id,
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.SOUND_EFFECTS, sourceTitle = "Sound Effects")
                            playbackController.setQueue(context, sfxList, index, true)
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
