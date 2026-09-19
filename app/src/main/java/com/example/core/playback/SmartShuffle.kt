package com.example.core.playback

import kotlin.random.Random

class SmartShuffle<T> {
    private var bag = mutableListOf<T>()
    private var originalList = listOf<T>()
    private val recentHistory = ArrayDeque<T>()
    private val maxHistory = 4

    fun setList(items: List<T>, currentItem: T? = null) {
        originalList = items
        bag.clear()
        recentHistory.clear()
        if (currentItem != null) {
            recentHistory.addLast(currentItem)
        }
        refillBag(exclude = currentItem)
    }

    fun next(): T? {
        if (originalList.isEmpty()) return null
        if (originalList.size == 1) return originalList.first()

        if (bag.isEmpty()) {
            val last = recentHistory.lastOrNull()
            refillBag(exclude = last)
        }

        val nextItem = bag.removeAt(0)
        recentHistory.addLast(nextItem)
        if (recentHistory.size > maxHistory) {
            recentHistory.removeFirst()
        }
        return nextItem
    }

    private fun refillBag(exclude: T? = null) {
        val candidates = originalList.toMutableList()
        candidates.shuffle(Random)
        // If the first candidate matches the excluded item and we have multiple candidates, swap it
        if (exclude != null && candidates.size > 1 && candidates.first() == exclude) {
            val swapIdx = 1 + Random.nextInt(candidates.size - 1)
            val temp = candidates[0]
            candidates[0] = candidates[swapIdx]
            candidates[swapIdx] = temp
        }
        bag = candidates
    }
}
