package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.core.playback.PlaybackController
import com.example.ui.navigation.MainApp
import com.example.ui.theme.BMPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BMPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        PlaybackController.getInstance(this).setUiVisible(true)
    }

    override fun onStop() {
        super.onStop()
        PlaybackController.getInstance(this).setUiVisible(false)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        (application as? BMPlayerApplication)?.handleTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        (application as? BMPlayerApplication)?.handleLowMemory()
    }
}

