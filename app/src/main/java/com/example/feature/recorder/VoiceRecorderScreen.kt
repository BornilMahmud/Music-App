package com.example.feature.recorder

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.core.common.Formatters
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.model.PlaybackContext
import com.example.core.model.PlaybackSource
import com.example.core.playback.PlaybackController
import com.example.core.recording.VoiceRecorderManager
import com.example.core.repository.AudioRepository
import com.example.core.ui.BMEmptyState
import com.example.core.ui.BMSongRow
import com.example.core.ui.BMTopBar
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue
import com.example.ui.theme.BmFavorite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecorderScreen(
    audioRepository: AudioRepository,
    playbackController: PlaybackController,
    onBack: (() -> Unit)? = null,
    onShowDetails: (Audio) -> Unit,
    onEditCategory: (Audio) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val recorderManager = remember { VoiceRecorderManager(context) }
    val recorderState by recorderManager.state.collectAsState()

    val recordings by audioRepository.getByCategory(AudioCategory.VOICE_RECORDING).collectAsState(initial = emptyList())
    val playbackState by playbackController.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            recorderManager.startRecording()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("voice_recorder_screen")
    ) {
        BMTopBar(
            title = "Voice Notes",
            navigationIcon = onBack?.let {
                {
                    IconButton(onClick = it) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        )

        // Recorder Widget Card
        Card(
            colors = CardDefaults.cardColors(containerColor = BmDarkSurfaceVariant),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (recorderState.isRecording) {
                        if (recorderState.isPaused) "RECORDING PAUSED" else "RECORDING..."
                    } else "TAP TO RECORD",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = if (recorderState.isRecording) BmFavorite else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = Formatters.formatDuration(recorderState.elapsedSeconds * 1000L),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 36.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Record Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (recorderState.isRecording) {
                        // Cancel button
                        IconButton(
                            onClick = { recorderManager.cancelRecording() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel", tint = MaterialTheme.colorScheme.error)
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // Stop & Save button
                        Surface(
                            onClick = {
                                scope.launch {
                                    recorderManager.stopRecording()
                                }
                            },
                            shape = CircleShape,
                            color = BmFavorite,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Stop, contentDescription = "Save Recording", tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(32.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // Pause/Resume button
                        IconButton(
                            onClick = {
                                if (recorderState.isPaused) recorderManager.resumeRecording() else recorderManager.pauseRecording()
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (recorderState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (recorderState.isPaused) "Resume" else "Pause",
                                tint = BmElectricBlue
                            )
                        }
                    } else {
                        // Start Record Button
                        Surface(
                            onClick = {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    recorderManager.startRecording()
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            shape = CircleShape,
                            color = BmElectricBlue,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Mic, contentDescription = "Start Recording", tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(32.dp))
                            }
                        }
                    }
                }
            }
        }

        // List of Voice Notes
        Text(
            text = "Saved Voice Notes (${recordings.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (recordings.isEmpty()) {
            BMEmptyState(
                icon = Icons.Default.Mic,
                title = "No Voice Notes Yet",
                description = "Record quick thoughts, memos, or lectures with crystal-clear AAC quality."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(recordings, key = { _, audio -> audio.id }) { index, audio ->
                    BMSongRow(
                        audio = audio,
                        isSelected = playbackState.currentAudio?.id == audio.id,
                        isPlaying = playbackState.isPlaying && playbackState.currentAudio?.id == audio.id,
                        onClick = {
                            val context = PlaybackContext(PlaybackSource.VOICE_RECORDINGS, sourceTitle = "Voice Notes")
                            playbackController.setQueue(context, recordings, index, true)
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
