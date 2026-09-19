package com.example.core.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.model.Audio
import com.example.core.model.AudioCategory
import com.example.ui.theme.BmDarkSurfaceHighlight
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue
import com.example.ui.theme.BmViolet

@Composable
fun BMSongArtwork(
    audio: Audio?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    val category = audio?.effectiveCategory ?: AudioCategory.MUSIC
    val defaultIcon: ImageVector = remember(category) {
        when (category) {
            AudioCategory.VOICE_RECORDING -> Icons.Default.Mic
            AudioCategory.WHATSAPP_AUDIO -> Icons.Default.Mic
            AudioCategory.SOUND_EFFECT -> Icons.Default.SpatialAudio
            AudioCategory.PODCAST, AudioCategory.AUDIOBOOK -> Icons.Default.Podcasts
            else -> Icons.Default.MusicNote
        }
    }

    val context = LocalContext.current
    val density = LocalDensity.current
    val targetPx = remember(size, density) {
        with(density) { size.roundToPx() }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(BmDarkSurfaceHighlight, BmDarkSurfaceVariant)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (audio != null && audio.hasArtwork) {
            val imageRequest = remember(audio.uri, targetPx) {
                ImageRequest.Builder(context)
                    .data(Uri.parse(audio.uri))
                    // Downsample directly to view dimensions to avoid decoding huge 4K covers (Low-RAM rule)
                    .size(targetPx, targetPx)
                    // Use RGB_565 (2 bytes/pixel) instead of ARGB_8888 (4 bytes/pixel) for 50% RAM savings
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .crossfade(150)
                    .build()
            }

            AsyncImage(
                model = imageRequest,
                contentDescription = "Album Artwork",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            DefaultArtworkFallback(icon = defaultIcon, size = size)
        }
    }
}

@Composable
private fun DefaultArtworkFallback(
    icon: ImageVector,
    size: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        BmElectricBlue.copy(alpha = 0.25f),
                        BmViolet.copy(alpha = 0.25f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BmElectricBlue,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}
