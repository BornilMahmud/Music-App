package com.example.core.audio

import android.content.Context
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EqualizerBand(
    val index: Int,
    val centerFreq: Int,
    val minLevel: Short,
    val maxLevel: Short,
    val currentLevel: Short
)

data class EqualizerState(
    val isEnabled: Boolean = false,
    val currentPreset: String = "Flat",
    val presets: List<String> = listOf("Flat", "Bass Boost", "Treble Boost", "Vocal", "Rock", "Pop", "Jazz", "Classical", "Custom"),
    val bands: List<EqualizerBand> = emptyList(),
    val bassBoostStrength: Int = 0, // 0 to 1000
    val isSupported: Boolean = true
)

class EqualizerManager(private val context: Context) {

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null

    private val _state = MutableStateFlow(EqualizerState())
    val state: StateFlow<EqualizerState> = _state.asStateFlow()

    fun attachAudioSession(audioSessionId: Int) {
        if (audioSessionId <= 0) return
        try {
            release()
            val eq = Equalizer(0, audioSessionId)
            val bb = BassBoost(0, audioSessionId)

            val bands = mutableListOf<EqualizerBand>()
            val numBands = eq.numberOfBands.toInt()
            val bandLevelRange = eq.bandLevelRange

            for (i in 0 until numBands) {
                bands.add(
                    EqualizerBand(
                        index = i,
                        centerFreq = eq.getCenterFreq(i.toShort()) / 1000, // in Hz
                        minLevel = bandLevelRange[0],
                        maxLevel = bandLevelRange[1],
                        currentLevel = eq.getBandLevel(i.toShort())
                    )
                )
            }

            equalizer = eq
            bassBoost = bb

            _state.value = _state.value.copy(
                isSupported = true,
                bands = bands
            )
        } catch (e: Exception) {
            // Graceful fallback (Section 57)
            _state.value = _state.value.copy(isSupported = false)
        }
    }

    fun setEnabled(enabled: Boolean) {
        try {
            equalizer?.enabled = enabled
            bassBoost?.enabled = enabled
            _state.value = _state.value.copy(isEnabled = enabled)
        } catch (_: Exception) {}
    }

    fun setBandLevel(bandIndex: Int, level: Short) {
        try {
            equalizer?.setBandLevel(bandIndex.toShort(), level)
            val updatedBands = _state.value.bands.map {
                if (it.index == bandIndex) it.copy(currentLevel = level) else it
            }
            _state.value = _state.value.copy(bands = updatedBands, currentPreset = "Custom")
        } catch (_: Exception) {}
    }

    fun setBassBoost(strength: Int) {
        try {
            val clamped = strength.coerceIn(0, 1000)
            bassBoost?.setStrength(clamped.toShort())
            _state.value = _state.value.copy(bassBoostStrength = clamped)
        } catch (_: Exception) {}
    }

    fun applyPreset(presetName: String) {
        val eq = equalizer ?: return
        try {
            val bands = _state.value.bands
            if (bands.isEmpty()) return

            val min = bands[0].minLevel
            val max = bands[0].maxLevel
            val mid = ((min + max) / 2).toShort()

            val targets: List<Short> = when (presetName) {
                "Flat" -> List(bands.size) { mid }
                "Bass Boost" -> bands.mapIndexed { idx, _ ->
                    if (idx == 0) max else if (idx == 1) ((max + mid) / 2).toShort() else mid
                }
                "Treble Boost" -> bands.mapIndexed { idx, _ ->
                    if (idx >= bands.size - 2) max else mid
                }
                "Vocal" -> bands.mapIndexed { idx, _ ->
                    if (idx in 1..2) ((max + mid) / 2).toShort() else mid
                }
                "Rock" -> bands.mapIndexed { idx, _ ->
                    if (idx == 0 || idx == bands.size - 1) max else mid
                }
                "Pop" -> bands.mapIndexed { idx, _ ->
                    if (idx in 1..3) ((max + mid) / 2).toShort() else mid
                }
                "Jazz" -> bands.mapIndexed { idx, _ ->
                    if (idx == 1 || idx == bands.size - 2) ((max + mid) / 2).toShort() else mid
                }
                "Classical" -> List(bands.size) { mid }
                else -> return
            }

            targets.forEachIndexed { index, level ->
                if (index < bands.size) {
                    eq.setBandLevel(index.toShort(), level)
                }
            }

            val updatedBands = bands.mapIndexed { idx, band ->
                band.copy(currentLevel = if (idx < targets.size) targets[idx] else mid)
            }

            _state.value = _state.value.copy(
                currentPreset = presetName,
                bands = updatedBands
            )
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            equalizer?.release()
            bassBoost?.release()
        } catch (_: Exception) {}
        equalizer = null
        bassBoost = null
    }
}
