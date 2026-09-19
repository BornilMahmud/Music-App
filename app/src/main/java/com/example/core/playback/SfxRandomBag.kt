package com.example.core.playback

import com.example.core.model.Audio
import kotlin.random.Random

enum class SfxPlayMode {
    SINGLE_PLAY,
    REPEAT,
    RANDOM;

    val displayName: String
        get() = when (this) {
            SINGLE_PLAY -> "Single Play"
            REPEAT -> "Repeat"
            RANDOM -> "Random"
        }
}

class SfxRandomBag {
    private var effects = listOf<Audio>()
    private var bag = mutableListOf<Audio>()
    private var lastPlayed: Audio? = null

    fun setEffects(list: List<Audio>) {
        effects = list
        bag.clear()
        refill()
    }

    fun nextRandom(): Audio? {
        if (effects.isEmpty()) return null
        if (effects.size == 1) return effects.first()

        if (bag.isEmpty()) {
            refill()
        }

        // Pop from bag
        val item = bag.removeAt(0)
        lastPlayed = item
        return item
    }

    private fun refill() {
        val candidates = effects.toMutableList()
        candidates.shuffle(Random)
        // Avoid immediate repetition: if top of bag is same as lastPlayed, move it
        if (lastPlayed != null && candidates.size > 1 && candidates.first().id == lastPlayed?.id) {
            val swapIndex = 1 + Random.nextInt(candidates.size - 1)
            val temp = candidates[0]
            candidates[0] = candidates[swapIndex]
            candidates[swapIndex] = temp
        }
        bag = candidates
    }
}
