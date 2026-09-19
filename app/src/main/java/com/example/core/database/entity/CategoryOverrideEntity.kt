package com.example.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.AudioCategory

@Entity(tableName = "category_overrides")
data class CategoryOverrideEntity(
    @PrimaryKey val uri: String,
    val filePath: String?,
    val manualCategory: AudioCategory,
    val overriddenAt: Long = System.currentTimeMillis()
)
