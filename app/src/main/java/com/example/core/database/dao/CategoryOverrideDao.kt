package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.database.entity.CategoryOverrideEntity

@Dao
interface CategoryOverrideDao {

    @Query("SELECT * FROM category_overrides")
    suspend fun getAllOverrides(): List<CategoryOverrideEntity>

    @Query("SELECT * FROM category_overrides WHERE uri = :uri LIMIT 1")
    suspend fun getOverrideForUri(uri: String): CategoryOverrideEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOverride(override: CategoryOverrideEntity)

    @Query("DELETE FROM category_overrides WHERE uri = :uri")
    suspend fun deleteOverride(uri: String)
}
