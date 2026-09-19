package com.example.feature.equalizer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.audio.EqualizerManager
import com.example.core.ui.BMEmptyState
import com.example.core.ui.BMTopBar
import com.example.ui.theme.BmDarkSurfaceVariant
import com.example.ui.theme.BmElectricBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerScreen(
    equalizerManager: EqualizerManager,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val eqState by equalizerManager.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("equalizer_screen")
    ) {
        BMTopBar(
            title = "Equalizer",
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (!eqState.isSupported) {
            BMEmptyState(
                icon = Icons.Default.Equalizer,
                title = "Equalizer Not Available",
                description = "Your device audio hardware does not support hardware equalizer effects."
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Enable / Disable Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = BmDarkSurfaceVariant),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Audio Effects",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (eqState.isEnabled) "Enabled" else "Disabled",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Switch(
                            checked = eqState.isEnabled,
                            onCheckedChange = { equalizerManager.setEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = BmElectricBlue)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Presets Row
                Text(
                    text = "Presets",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(eqState.presets, key = { it }) { preset ->
                        FilterChip(
                            selected = (eqState.currentPreset == preset),
                            onClick = { equalizerManager.applyPreset(preset) },
                            label = { Text(preset) },
                            enabled = eqState.isEnabled,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BmElectricBlue,
                                selectedLabelColor = androidx.compose.ui.graphics.Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bass Boost Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = BmDarkSurfaceVariant),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Bass Boost",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${eqState.bassBoostStrength / 10}%",
                                style = MaterialTheme.typography.bodyMedium.copy(color = BmElectricBlue)
                            )
                        }

                        Slider(
                            value = eqState.bassBoostStrength.toFloat(),
                            onValueChange = { equalizerManager.setBassBoost(it.toInt()) },
                            valueRange = 0f..1000f,
                            enabled = eqState.isEnabled,
                            colors = SliderDefaults.colors(
                                thumbColor = BmElectricBlue,
                                activeTrackColor = BmElectricBlue
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Frequency Bands
                if (eqState.bands.isNotEmpty()) {
                    Text(
                        text = "Bands",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = BmDarkSurfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            eqState.bands.forEach { band ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${band.centerFreq} Hz",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        modifier = Modifier.width(64.dp)
                                    )

                                    Slider(
                                        value = band.currentLevel.toFloat(),
                                        onValueChange = { level ->
                                            equalizerManager.setBandLevel(band.index, level.toInt().toShort())
                                        },
                                        valueRange = band.minLevel.toFloat()..band.maxLevel.toFloat(),
                                        enabled = eqState.isEnabled,
                                        colors = SliderDefaults.colors(
                                            thumbColor = BmElectricBlue,
                                            activeTrackColor = BmElectricBlue
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = "${band.currentLevel / 100} dB",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                        modifier = Modifier.width(48.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
