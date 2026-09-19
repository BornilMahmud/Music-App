package com.example.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.playback.PlaybackController
import com.example.core.repository.AudioRepository
import com.example.core.ui.BMSongArtwork
import com.example.core.ui.BMSongRow
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue
import com.example.ui.theme.BmViolet

@Composable
fun HomeScreen(
    audioRepository: AudioRepository,
    playbackController: PlaybackController,
    onNavigateToCategory: (AudioCategory) -> Unit,
    onNavigateToSongs: () -> Unit,
    onNavigateToRecorder: () -> Unit,
    onNavigateToSfx: () -> Unit,
    onShowDetails: (Audio) -> Unit,
    onEditCategory: (Audio) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryCounts by audioRepository.getCategoryCounts().collectAsState(initial = emptyMap())
    val recentlyPlayed by audioRepository.getRecentlyPlayed(10).collectAsState(initial = emptyList())
    val recentlyAdded by audioRepository.getRecentlyAddedMusic(10).collectAsState(initial = emptyList())
    val allMusic by audioRepository.getAllMusic().collectAsState(initial = emptyList())
    val scanProgress by audioRepository.scanProgress.collectAsState()
    val playbackState by playbackController.state.collectAsState()
    val scope = rememberCoroutineScope()

    val musicCount = categoryCounts[AudioCategory.MUSIC] ?: 0
    val voiceCount = categoryCounts[AudioCategory.VOICE_RECORDING] ?: 0
    val sfxCount = categoryCounts[AudioCategory.SOUND_EFFECT] ?: 0
    val podcastCount = categoryCounts[AudioCategory.PODCAST] ?: 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(androidx.compose.ui.graphics.Color(0xFF0C101A))
                                .border(1.dp, BmElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_bm_logo),
                                contentDescription = "BM Player Logo",
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "BM PLAYER",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp,
                                    color = androidx.compose.ui.graphics.Color.White
                                )
                            )
                            Text(
                                text = "YOUR AUDIO. YOUR WAY.",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.2.sp,
                                    fontSize = 9.sp,
                                    color = BmElectricBlue
                                )
                            )
                        }
                    }

                    if (scanProgress.isScanning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = BmElectricBlue,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }

        // Quick Category Cards
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    CategoryStatCard(
                        title = "Music",
                        count = "$musicCount tracks",
                        icon = Icons.Default.MusicNote,
                        onClick = onNavigateToSongs
                    )
                }
                item {
                    CategoryStatCard(
                        title = "Voice",
                        count = "$voiceCount notes",
                        icon = Icons.Default.Mic,
                        onClick = onNavigateToRecorder
                    )
                }
                item {
                    CategoryStatCard(
                        title = "SFX",
                        count = "$sfxCount sounds",
                        icon = Icons.Default.SpatialAudio,
                        onClick = onNavigateToSfx
                    )
                }
                item {
                    CategoryStatCard(
                        title = "Podcasts",
                        count = "$podcastCount episodes",
                        icon = Icons.Default.Podcasts,
                        onClick = { onNavigateToCategory(AudioCategory.PODCAST) }
                    )
                }
            }
        }

        // Quick Actions (Play All Music / Shuffle Music)
        if (musicCount > 0) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.MUSIC, sourceTitle = "All Music")
                            val queueToPlay = if (allMusic.isNotEmpty()) allMusic else recentlyAdded
                            if (queueToPlay.isNotEmpty()) {
                                playbackController.setQueue(context, queueToPlay, 0, true)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BmElectricBlue),
                        shape = RoundedCornerShape(9999.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("home_play_all")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Play Music", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.MUSIC, sourceTitle = "Music Shuffle")
                            val queueToPlay = if (allMusic.isNotEmpty()) allMusic else recentlyAdded
                            if (queueToPlay.isNotEmpty()) {
                                playbackController.setQueue(context, queueToPlay, 0, true)
                                playbackController.toggleShuffle()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BmDarkSurfaceHighlight),
                        shape = RoundedCornerShape(9999.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("home_shuffle_all")
                    ) {
                        Icon(Icons.Default.Shuffle, contentDescription = null, tint = BmElectricBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Shuffle", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Continue Listening / Recently Played
        if (recentlyPlayed.isNotEmpty()) {
            item {
                SectionTitle(title = "Recently Played")
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentlyPlayed, key = { it.id }) { audio ->
                        RecentlyPlayedCard(
                            audio = audio,
                            onClick = {
                                val context = PlaybackContext(
                                    source = PlaybackSource.ALL,
                                    sourceTitle = "Recently Played"
                                )
                                playbackController.setQueue(context, recentlyPlayed, recentlyPlayed.indexOf(audio), true)
                            }
                        )
                    }
                }
            }
        }

        // Recently Added Music (Section 21 & 51: SONGS = MUSIC ONLY)
        if (recentlyAdded.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle(title = "Recently Added Music", onActionClick = onNavigateToSongs)
            }

            items(recentlyAdded.take(5), key = { it.id }) { audio ->
                BMSongRow(
                    audio = audio,
                    isSelected = playbackState.currentAudio?.id == audio.id,
                    onClick = {
                        val context = PlaybackContext(PlaybackSource.MUSIC, sourceTitle = "Recently Added Music")
                        playbackController.setQueue(context, recentlyAdded, recentlyAdded.indexOf(audio), true)
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
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        if (onActionClick != null) {
            Text(
                text = "See All",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = BmElectricBlue,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
private fun CategoryStatCard(
    title: String,
    count: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BmDarkSurfaceVariant),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(135.dp)
            .height(115.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = BmDarkSurfaceHighlight,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = BmElectricBlue, modifier = Modifier.size(20.dp))
                }
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
                Text(
                    text = count,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                )
            }
        }
    }
}

@Composable
private fun RecentlyPlayedCard(
    audio: Audio,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        BMSongArtwork(
            audio = audio,
            size = 130.dp,
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = audio.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = audio.displayArtist,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
