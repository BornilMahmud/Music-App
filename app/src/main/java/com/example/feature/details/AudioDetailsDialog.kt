package com.example.feature.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.common.Formatters
import com.example.core.model.Audio

@Composable
fun AudioDetailsDialog(
    audio: Audio,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Audio Details",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                DetailItem(label = "Title", value = audio.title)
                DetailItem(label = "Artist", value = audio.displayArtist)
                DetailItem(label = "Album", value = audio.displayAlbum)
                DetailItem(label = "Category", value = audio.effectiveCategory.displayName)
                DetailItem(
                    label = "Classification",
                    value = "${audio.classificationSource.name} (${(audio.classificationConfidence * 100).toInt()}%)"
                )
                if (audio.manualCategory != null) {
                    DetailItem(label = "Manual Override", value = audio.manualCategory.displayName)
                }
                DetailItem(label = "Duration", value = Formatters.formatDuration(audio.durationMs))
                DetailItem(label = "Size", value = Formatters.formatFileSize(audio.sizeBytes))
                DetailItem(label = "Codec / Format", value = audio.codec ?: audio.mimeType ?: "Unknown")
                audio.bitrate?.let { DetailItem(label = "Bitrate", value = Formatters.formatBitrate(it)) }
                audio.sampleRate?.let { DetailItem(label = "Sample Rate", value = Formatters.formatSampleRate(it)) }
                DetailItem(label = "File Name", value = audio.fileName)
                DetailItem(label = "File Path", value = audio.filePath ?: audio.uri)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
    }
}
