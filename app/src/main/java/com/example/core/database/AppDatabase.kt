package com.example.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core.database.dao.AudioDao
import com.example.core.database.dao.CategoryOverrideDao
import com.example.core.database.dao.PlayHistoryDao
import com.example.core.database.dao.PlaylistDao
import com.example.core.database.entity.AudioEntity
import com.example.core.database.entity.CategoryOverrideEntity
import com.example.core.database.entity.PlayHistoryEntity
import com.example.core.database.entity.PlaylistEntity
import com.example.core.database.entity.PlaylistItemEntity

@Database(
    entities = [
        AudioEntity::class,
        PlaylistEntity::class,
        PlaylistItemEntity::class,
        PlayHistoryEntity::class,
        CategoryOverrideEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun audioDao(): AudioDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playHistoryDao(): PlayHistoryDao
    abstract fun categoryOverrideDao(): CategoryOverrideDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bm_player.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
