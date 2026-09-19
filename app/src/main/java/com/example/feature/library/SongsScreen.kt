package com.example.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.playback.PlaybackController
import com.example.core.repository.AudioRepository
import com.example.core.ui.BMEmptyState
import com.example.core.ui.BMSongRow
import com.example.core.ui.BMTopBar
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    audioRepository: AudioRepository,
    playbackController: PlaybackController,
    onBack: (() -> Unit)? = null,
    onShowDetails: (Audio) -> Unit,
    onEditCategory: (Audio) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var selectedClassification by remember { mutableStateOf(AudioCategory.MUSIC) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val playbackState by playbackController.state.collectAsState()
    val musicCount by audioRepository.getMusicCount().collectAsState(initial = 0)
    val favoriteMusicCount by audioRepository.getFavoriteMusicCount().collectAsState(initial = 0)
    val categoryCounts by audioRepository.getCategoryCounts().collectAsState(initial = emptyMap())
    val whatsappCount = categoryCounts[AudioCategory.WHATSAPP_AUDIO] ?: 0
    val recordingCount = categoryCounts[AudioCategory.VOICE_RECORDING] ?: 0

    // Database-level filtering and tab-specific query to avoid holding multiple lists in memory or filtering 50k items in Compose (Rules 21, 51, Low-RAM)
    val currentListFlow = remember(selectedClassification, selectedTab, searchQuery) {
        if (searchQuery.isNotBlank()) {
            audioRepository.searchCategory(searchQuery.trim(), selectedClassification)
        } else {
            when (selectedClassification) {
                AudioCategory.MUSIC -> {
                    when (selectedTab) {
                        0 -> audioRepository.getAllMusic()
                        1 -> audioRepository.getRecentlyAddedMusic(50)
                        2 -> audioRepository.getFavoriteMusic()
                        else -> audioRepository.getAllMusic()
                    }
                }
                AudioCategory.WHATSAPP_AUDIO -> audioRepository.getByCategory(AudioCategory.WHATSAPP_AUDIO)
                AudioCategory.VOICE_RECORDING -> audioRepository.getByCategory(AudioCategory.VOICE_RECORDING)
                else -> audioRepository.getAllMusic()
            }
        }
    }
    val currentList by currentListFlow.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("songs_screen")
    ) {
        // Top Bar
        val headerTitle = when (selectedClassification) {
            AudioCategory.MUSIC -> "Songs"
            AudioCategory.WHATSAPP_AUDIO -> "WhatsApp Audio"
            AudioCategory.VOICE_RECORDING -> "Voice Recordings"
            else -> "Audio"
        }

        BMTopBar(
            title = headerTitle,
            navigationIcon = {
                IconButton(onClick = {
                    if (selectedClassification != AudioCategory.MUSIC) {
                        selectedClassification = AudioCategory.MUSIC
                    } else {
                        onBack?.invoke()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { isSearchActive = !isSearchActive }) {
                    Icon(Icons.Default.Search, contentDescription = "Search Audio")
                }
                IconButton(onClick = {
                    scope.launch { audioRepository.scanLibrary() }
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Rescan")
                }
            }
        )

        // Search bar when active
        if (isSearchActive) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search songs, notes, recordings...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Top Classification Filter Bar (Strict separation of Pure Music vs WhatsApp vs Voice Recordings)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ClassificationPill(
                title = "Pure Music",
                count = musicCount,
                isSelected = selectedClassification == AudioCategory.MUSIC,
                activeColor = BmElectricBlue,
                onClick = { selectedClassification = AudioCategory.MUSIC },
                modifier = Modifier.weight(1f)
            )
            ClassificationPill(
                title = "WhatsApp",
                count = whatsappCount,
                isSelected = selectedClassification == AudioCategory.WHATSAPP_AUDIO,
                activeColor = Color(0xFF25D366),
                onClick = { selectedClassification = AudioCategory.WHATSAPP_AUDIO },
                modifier = Modifier.weight(1f)
            )
            ClassificationPill(
                title = "Recordings",
                count = recordingCount,
                isSelected = selectedClassification == AudioCategory.VOICE_RECORDING,
                activeColor = Color(0xFFFF3366),
                onClick = { selectedClassification = AudioCategory.VOICE_RECORDING },
                modifier = Modifier.weight(1f)
            )
        }

        // Music Sub-tabs (All Music, Recently Added, Favorites - strictly within MUSIC)
        if (selectedClassification == AudioCategory.MUSIC) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = BmElectricBlue
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("All ($musicCount)") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Recent") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Favorites ($favoriteMusicCount)") }
                )
            }
        }

        // Action Buttons: Play All & Shuffle (Rule 1 & Rule 39: Context-aware queue!)
        if (currentList.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val playButtonColor = when (selectedClassification) {
                    AudioCategory.WHATSAPP_AUDIO -> Color(0xFF25D366)
                    AudioCategory.VOICE_RECORDING -> Color(0xFFFF3366)
                    else -> BmElectricBlue
                }

                Button(
                    onClick = {
                        val source = when (selectedClassification) {
                            AudioCategory.VOICE_RECORDING -> PlaybackSource.VOICE_RECORDINGS
                            AudioCategory.WHATSAPP_AUDIO -> PlaybackSource.MUSIC
                            else -> PlaybackSource.MUSIC
                        }
                        val context = PlaybackContext(source, sourceTitle = headerTitle)
                        playbackController.setQueue(context, currentList, 0, true)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = playButtonColor),
                    shape = RoundedCornerShape(9999.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("songs_play_all")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    val label = when (selectedClassification) {
                        AudioCategory.WHATSAPP_AUDIO -> "Play All Notes"
                        AudioCategory.VOICE_RECORDING -> "Play All Recordings"
                        else -> "Play All"
                    }
                    Text(label, fontWeight = FontWeight.Bold)
                }

                if (selectedClassification == AudioCategory.MUSIC) {
                    Button(
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.MUSIC, sourceTitle = "Songs Shuffle")
                            playbackController.setQueue(context, currentList, 0, true)
                            playbackController.toggleShuffle()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BmDarkSurfaceHighlight),
                        shape = RoundedCornerShape(9999.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("songs_shuffle")
                    ) {
                        Icon(Icons.Default.Shuffle, contentDescription = null, tint = BmElectricBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Shuffle", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // List View
        if (currentList.isEmpty()) {
            val emptyTitle = when (selectedClassification) {
                AudioCategory.WHATSAPP_AUDIO -> "No WhatsApp Audio Found"
                AudioCategory.VOICE_RECORDING -> "No Voice Recordings"
                else -> "No Music Found"
            }
            val emptyDesc = when (selectedClassification) {
                AudioCategory.WHATSAPP_AUDIO -> "WhatsApp voice notes and received audio clips will appear here separately from your music library."
                AudioCategory.VOICE_RECORDING -> "Recorded voice memos and call recordings will appear here separately from your music library."
                else -> "BM Player found no music tracks in this section. Add music or scan again."
            }

            BMEmptyState(
                icon = Icons.Default.MusicNote,
                title = emptyTitle,
                description = emptyDesc,
                actionLabel = "Scan Library",
                onAction = { scope.launch { audioRepository.scanLibrary() } }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(currentList, key = { _, audio -> audio.id }) { index, audio ->
                    val source = when (selectedClassification) {
                        AudioCategory.VOICE_RECORDING -> PlaybackSource.VOICE_RECORDINGS
                        AudioCategory.WHATSAPP_AUDIO -> PlaybackSource.MUSIC
                        else -> PlaybackSource.MUSIC
                    }
                    BMSongRow(
                        audio = audio,
                        isSelected = playbackState.currentAudio?.id == audio.id,
                        isPlaying = playbackState.isPlaying && playbackState.currentAudio?.id == audio.id,
                        onClick = {
                            val context = PlaybackContext(source, sourceTitle = headerTitle)
                            playbackController.setQueue(context, currentList, index, true)
                        },
                        onFavoriteToggle = {
                            scope.launch {
                                audioRepository.setFavorite(audio.id, !audio.isFavorite)
                            }
                        },
                        onPlayNext = { playbackController.playNext(audio) },
                        onAddToQueue = { playbackController.addToQueue(audio) },
                        onShowDetails = { onShowDetails(audio) },
                        onEditCategory = { onEditCategory(audio) },
                        onDelete = {
                            scope.launch { audioRepository.deleteAudio(audio.id) }
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ClassificationPill(
    title: String,
    count: Int,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) activeColor.copy(alpha = 0.18f) else BmDarkSurfaceHighlight
    val borderColor = if (isSelected) activeColor else Color.Transparent
    val textColor = if (isSelected) activeColor else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(9999.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(9999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title ($count)",
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
            maxLines = 1
        )
    }
}

