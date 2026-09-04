package com.example.postly.features.search.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [RecentSearchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecentSearchDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}