package com.example.core.database

import androidx.room.TypeConverter
import com.example.core.model.AudioCategory
import com.example.core.model.ClassificationSource

class Converters {
    @TypeConverter
    fun fromAudioCategory(category: AudioCategory?): String? = category?.name

    @TypeConverter
    fun toAudioCategory(name: String?): AudioCategory? = name?.let {
        try {
            AudioCategory.valueOf(it)
        } catch (_: Exception) {
            AudioCategory.OTHER
        }
    }

    @TypeConverter
    fun fromClassificationSource(source: ClassificationSource?): String? = source?.name

    @TypeConverter
    fun toClassificationSource(name: String?): ClassificationSource? = name?.let {
        try {
            ClassificationSource.valueOf(it)
        } catch (_: Exception) {
            ClassificationSource.FALLBACK
        }
    }
}
