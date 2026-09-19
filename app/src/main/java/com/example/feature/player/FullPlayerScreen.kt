package com.example.feature.player

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.common.Formatters
import com.example.core.playback.PlaybackController
import com.example.core.playback.PlaybackState
import com.example.core.playback.RepeatMode
import com.example.core.ui.BMSongArtwork
import com.example.feature.details.AudioDetailsDialog
import com.example.feature.queue.QueueSheet
import com.example.ui.theme.BmDarkBackground
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue
import com.example.ui.theme.BmFavorite
import com.example.ui.theme.BmViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPlayerScreen(
    playbackState: PlaybackState,
    playbackController: PlaybackController,
    onMinimize: () -> Unit,
    onNavigateToEqualizer: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val audio = playbackState.currentAudio ?: return

    var isSeeking by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableFloatStateOf(0f) }

    var showQueue by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }

    var dragOffsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        BmDarkSurfaceVariant,
                        BmDarkBackground
                    )
                )
            )
            .testTag("full_player_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMinimize, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Minimize player",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PLAYING FROM",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = playbackState.context?.sourceTitle.takeUnless { it.isNullOrBlank() }
                            ?: playbackState.context?.source?.name ?: "Library",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BmElectricBlue
                        )
                    )
                }

                IconButton(onClick = { showDetailsDialog = true }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Audio Details",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Artwork with Swipe gesture (Section 34)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            dragOffsetX += delta
                        },
                        onDragStopped = {
                            if (dragOffsetX > 100f) {
                                // Swipe Right -> Forward / Next track
                                playbackController.next()
                            } else if (dragOffsetX < -100f) {
                                // Swipe Left -> Backward / Previous track
                                playbackController.previous()
                            }
                            dragOffsetX = 0f
                        }
                    )
                    .testTag("full_player_artwork"),
                contentAlignment = Alignment.Center
            ) {
                BMSongArtwork(
                    audio = audio,
                    size = 320.dp,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title & Artist & Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = audio.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${audio.displayArtist} • ${audio.displayAlbum}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (audio.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (audio.isFavorite) BmFavorite else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                val currentPos = if (isSeeking) seekPosition.toLong() else playbackState.positionMs
                val maxDuration = if (playbackState.durationMs > 0) playbackState.durationMs else 1L

                Slider(
                    value = currentPos.toFloat(),
                    onValueChange = {
                        isSeeking = true
                        seekPosition = it
                    },
                    onValueChangeFinished = {
                        playbackController.seekTo(seekPosition.toLong())
                        isSeeking = false
                    },
                    valueRange = 0f..maxDuration.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = BmElectricBlue,
                        activeTrackColor = BmElectricBlue,
                        inactiveTrackColor = Color(0xFF2A2D3A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = Formatters.formatDuration(currentPos),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = Formatters.formatDuration(maxDuration),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            // Primary Playback Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle
                IconButton(
                    onClick = { playbackController.toggleShuffle() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (playbackState.shuffleEnabled) BmElectricBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Previous
                IconButton(
                    onClick = { playbackController.previous() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Track",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Play / Pause FAB with Sonic Tangerine Glow
                Surface(
                    onClick = { playbackController.togglePlayPause() },
                    shape = CircleShape,
                    color = BmElectricBlue,
                    shadowElevation = 12.dp,
                    modifier = Modifier
                        .shadow(16.dp, CircleShape, spotColor = BmElectricBlue)
                        .size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // Next
                IconButton(
                    onClick = { playbackController.next() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Track",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Repeat
                IconButton(
                    onClick = { playbackController.toggleRepeat() },
                    modifier = Modifier.size(48.dp)
                ) {
                    val icon = when (playbackState.repeatMode) {
                        RepeatMode.ONE -> Icons.Default.RepeatOne
                        else -> Icons.Default.Repeat
                    }
                    val tint = if (playbackState.repeatMode != RepeatMode.OFF) BmElectricBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    Icon(imageVector = icon, contentDescription = "Repeat", tint = tint)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary Controls Bar (Equalizer, Speed, Sleep Timer, Queue)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateToEqualizer) {
                    Icon(imageVector = Icons.Default.Equalizer, contentDescription = "Equalizer", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                IconButton(onClick = { showSpeedDialog = true }) {
                    Icon(imageVector = Icons.Default.Speed, contentDescription = "Playback Speed", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                IconButton(onClick = { showSleepTimerDialog = true }) {
                    val tint = if (playbackState.sleepTimerRemainingSeconds != null) BmElectricBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    Icon(imageVector = Icons.Default.Bedtime, contentDescription = "Sleep Timer", tint = tint)
                }

                IconButton(onClick = { showQueue = true }) {
                    Icon(imageVector = Icons.Default.QueueMusic, contentDescription = "Queue", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }

    // Queue Bottom Sheet
    if (showQueue) {
        QueueSheet(
            playbackState = playbackState,
            playbackController = playbackController,
            onDismiss = { showQueue = false }
        )
    }

    // Audio Details Dialog
    if (showDetailsDialog) {
        AudioDetailsDialog(
            audio = audio,
            onDismiss = { showDetailsDialog = false }
        )
    }

    // Speed Selection Dialog
    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            title = { Text("Playback Speed") },
            text = {
                val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)
                Column {
                    speeds.forEach { speed ->
                        TextButton(
                            onClick = {
                                playbackController.setPlaybackSpeed(speed)
                                showSpeedDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${speed}x",
                                color = if (playbackState.playbackSpeed == speed) BmElectricBlue else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (playbackState.playbackSpeed == speed) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Sleep Timer Dialog
    if (showSleepTimerDialog) {
        AlertDialog(
            onDismissRequest = { showSleepTimerDialog = false },
            title = { Text("Sleep Timer") },
            text = {
                val presets = listOf(5, 10, 15, 30, 45, 60)
                Column {
                    playbackState.sleepTimerRemainingSeconds?.let { rem ->
                        Text(
                            text = if (rem == -1L) "Timer set: End of Track" else "Remaining: ${Formatters.formatDuration(rem * 1000)}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = BmElectricBlue, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    presets.forEach { mins ->
                        TextButton(
                            onClick = {
                                playbackController.sleepTimer.startTimer(mins)
                                showSleepTimerDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("$mins minutes")
                        }
                    }

                    TextButton(
                        onClick = {
                            playbackController.sleepTimer.setEndOfTrack()
                            showSleepTimerDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("End of Track")
                    }

                    if (playbackState.sleepTimerRemainingSeconds != null) {
                        TextButton(
                            onClick = {
                                playbackController.sleepTimer.cancel()
                                showSleepTimerDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Turn Off Timer", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSleepTimerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
