package com.example.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.core.audio.EqualizerManager
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.core.playback.PlaybackController
import com.example.core.repository.AudioRepository
import com.example.core.repository.PlaylistRepository
import com.example.core.ui.BMMiniPlayer
import com.example.feature.details.AudioDetailsDialog
import com.example.feature.details.EditCategoryDialog
import com.example.feature.equalizer.EqualizerScreen
import com.example.feature.home.HomeScreen
import com.example.feature.library.CategoryAudioScreen
import com.example.feature.library.FoldersScreen
import com.example.feature.library.SongsScreen
import com.example.feature.player.FullPlayerScreen
import com.example.feature.playlists.PlaylistsScreen
import com.example.feature.recorder.VoiceRecorderScreen
import com.example.feature.settings.SettingsScreen
import com.example.feature.soundeffects.SoundEffectsScreen
import com.example.ui.theme.BmDarkSurface
import com.example.ui.theme.BmElectricBlue
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Songs : Screen()
    object VoiceNotes : Screen()
    object Playlists : Screen()
    object Settings : Screen()
    object Sfx : Screen()
    object Folders : Screen()
    object Equalizer : Screen()
    data class CategoryDetail(val category: AudioCategory) : Screen()
}

@Composable
fun MainApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val audioRepository = remember { AudioRepository(context) }
    val playlistRepository = remember { PlaylistRepository(context) }
    val playbackController = remember { PlaybackController.getInstance(context) }
    val equalizerManager = remember { EqualizerManager(context) }

    val playbackState by playbackController.state.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var isFullPlayerVisible by remember { mutableStateOf(false) }

    var selectedAudioForDetails by remember { mutableStateOf<Audio?>(null) }
    var selectedAudioForEditCategory by remember { mutableStateOf<Audio?>(null) }

    // Request permissions on first launch
    val permissionsToRequest = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_AUDIO)
            add(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        // Scan library once storage/audio permission is granted
        val audioGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            results[Manifest.permission.READ_MEDIA_AUDIO] == true
        } else {
            results[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }
        if (audioGranted) {
            scope.launch {
                audioRepository.scanLibrary()
            }
        }
    }

    LaunchedEffect(Unit) {
        val allGranted = permissionsToRequest.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (!allGranted) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            audioRepository.scanLibrary()
        }
    }

    // Back handling
    BackHandler(enabled = isFullPlayerVisible || currentScreen != Screen.Home) {
        if (isFullPlayerVisible) {
            isFullPlayerVisible = false
        } else if (currentScreen != Screen.Home) {
            currentScreen = Screen.Home
        }
    }

    Scaffold(
        bottomBar = {
            if (!isFullPlayerVisible) {
                Column {
                    // Mini Player docked right above bottom navigation
                    BMMiniPlayer(
                        playbackState = playbackState,
                        onExpand = { isFullPlayerVisible = true },
                        onPlayPause = { playbackController.togglePlayPause() },
                        onNext = { playbackController.next() }
                    )

                    NavigationBar(
                        containerColor = androidx.compose.ui.graphics.Color(0xFF181920),
                        tonalElevation = 8.dp
                    ) {
                        val navColors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BmElectricBlue,
                            selectedTextColor = BmElectricBlue,
                            unselectedIconColor = com.example.ui.theme.SonicSlateMuted,
                            unselectedTextColor = com.example.ui.theme.SonicSlateMuted,
                            indicatorColor = BmElectricBlue.copy(alpha = 0.15f)
                        )

                        NavigationBarItem(
                            selected = currentScreen is Screen.Home,
                            onClick = { currentScreen = Screen.Home },
                            icon = {
                                Icon(
                                    imageVector = if (currentScreen is Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            },
                            label = { Text("Home") },
                            colors = navColors
                        )

                        NavigationBarItem(
                            selected = currentScreen is Screen.Songs,
                            onClick = { currentScreen = Screen.Songs },
                            icon = {
                                Icon(
                                    imageVector = if (currentScreen is Screen.Songs) Icons.Filled.MusicNote else Icons.Outlined.MusicNote,
                                    contentDescription = "Songs"
                                )
                            },
                            label = { Text("Songs") },
                            colors = navColors
                        )

                        NavigationBarItem(
                            selected = currentScreen is Screen.VoiceNotes,
                            onClick = { currentScreen = Screen.VoiceNotes },
                            icon = {
                                Icon(
                                    imageVector = if (currentScreen is Screen.VoiceNotes) Icons.Filled.Mic else Icons.Outlined.Mic,
                                    contentDescription = "Voice"
                                )
                            },
                            label = { Text("Voice") },
                            colors = navColors
                        )

                        NavigationBarItem(
                            selected = currentScreen is Screen.Playlists,
                            onClick = { currentScreen = Screen.Playlists },
                            icon = {
                                Icon(
                                    imageVector = if (currentScreen is Screen.Playlists) Icons.Filled.PlaylistPlay else Icons.Outlined.PlaylistPlay,
                                    contentDescription = "Playlists"
                                )
                            },
                            label = { Text("Playlists") },
                            colors = navColors
                        )

                        NavigationBarItem(
                            selected = currentScreen is Screen.Settings,
                            onClick = { currentScreen = Screen.Settings },
                            icon = {
                                Icon(
                                    imageVector = if (currentScreen is Screen.Settings) Icons.Filled.Settings else Icons.Outlined.Settings,
                                    contentDescription = "Settings"
                                )
                            },
                            label = { Text("Settings") },
                            colors = navColors
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(
                        audioRepository = audioRepository,
                        playbackController = playbackController,
                        onNavigateToCategory = { cat -> currentScreen = Screen.CategoryDetail(cat) },
                        onNavigateToSongs = { currentScreen = Screen.Songs },
                        onNavigateToRecorder = { currentScreen = Screen.VoiceNotes },
                        onNavigateToSfx = { currentScreen = Screen.Sfx },
                        onShowDetails = { selectedAudioForDetails = it },
                        onEditCategory = { selectedAudioForEditCategory = it }
                    )
                }
                is Screen.Songs -> {
                    SongsScreen(
                        audioRepository = audioRepository,
                        playbackController = playbackController,
                        onBack = { currentScreen = Screen.Home },
                        onShowDetails = { selectedAudioForDetails = it },
                        onEditCategory = { selectedAudioForEditCategory = it }
                    )
                }
                is Screen.VoiceNotes -> {
                    VoiceRecorderScreen(
                        audioRepository = audioRepository,
                        playbackController = playbackController,
                        onBack = { currentScreen = Screen.Home },
                        onShowDetails = { selectedAudioForDetails = it },
                        onEditCategory = { selectedAudioForEditCategory = it }
                    )
                }
                is Screen.Playlists -> {
                    PlaylistsScreen(
                        playlistRepository = playlistRepository,
                        audioRepository = audioRepository,
                        playbackController = playbackController,
                        onBack = { currentScreen = Screen.Home },
                        onShowDetails = { selectedAudioForDetails = it },
                        onEditCategory = { selectedAudioForEditCategory = it }
                    )
                }
                is Screen.Settings -> {
                    SettingsScreen(
                        audioRepository = audioRepository,
                        onBack = { currentScreen = Screen.Home }
                    )
                }
                is Screen.Sfx -> {
                    SoundEffectsScreen(
                        audioRepository = audioRepository,
                        playbackController = playbackController,
                        onBack = { currentScreen = Screen.Home },
                        onShowDetails = { selectedAudioForDetails = it },
                        onEditCategory = { selectedAudioForEditCategory = it }
                    )
                }
                is Screen.Folders -> {
                    FoldersScreen(
                        audioRepository = audioRepository,
                        playbackController = playbackController,
                        onBack = { currentScreen = Screen.Home },
                        onShowDetails = { selectedAudioForDetails = it },
                        onEditCategory = { selectedAudioForEditCategory = it }
                    )
                }
                is Screen.Equalizer -> {
                    EqualizerScreen(
                        equalizerManager = equalizerManager,
                        onBack = { currentScreen = Screen.Home }
                    )
                }
                is Screen.CategoryDetail -> {
                    CategoryAudioScreen(
                        category = screen.category,
                        audioRepository = audioRepository,
                        playbackController = playbackController,
                        onBack = { currentScreen = Screen.Home },
                        onShowDetails = { selectedAudioForDetails = it },
                        onEditCategory = { selectedAudioForEditCategory = it }
                    )
                }
            }

            // Full Player overlay with smooth slide transition
            AnimatedVisibility(
                visible = isFullPlayerVisible,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                FullPlayerScreen(
                    playbackState = playbackState,
                    playbackController = playbackController,
                    onMinimize = { isFullPlayerVisible = false },
                    onNavigateToEqualizer = {
                        isFullPlayerVisible = false
                        currentScreen = Screen.Equalizer
                    },
                    onFavoriteToggle = {
                        playbackState.currentAudio?.let { current ->
                            scope.launch {
                                audioRepository.setFavorite(current.id, !current.isFavorite)
                            }
                        }
                    }
                )
            }
        }
    }

    // Audio Details Dialog
    selectedAudioForDetails?.let { audio ->
        AudioDetailsDialog(
            audio = audio,
            onDismiss = { selectedAudioForDetails = null }
        )
    }

    // Edit Category Dialog (Rule 6: manual override preserved across rescans)
    selectedAudioForEditCategory?.let { audio ->
        EditCategoryDialog(
            audio = audio,
            onCategorySelected = { newCategory ->
                scope.launch {
                    audioRepository.setManualCategory(audio, newCategory)
                }
            },
            onDismiss = { selectedAudioForEditCategory = null }
        )
    }
}
