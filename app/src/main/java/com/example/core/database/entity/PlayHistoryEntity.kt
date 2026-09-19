package com.example.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "play_history",
    indices = [
        Index(value = ["audioId"]),
        Index(value = ["playedAt"])
    ]
)
data class PlayHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val audioId: Long,
    val playedAt: Long = System.currentTimeMillis(),
    val durationPlayedMs: Long = 0L,
    val completed: Boolean = false
)
