package com.example.postly.features.search.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {
    @Query(
        """
        SELECT * FROM recent_searches
        ORDER BY searchedAt DESC
        LIMIT 10
        """
    )
    fun observeRecentSearches(): Flow<List<RecentSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(search: RecentSearchEntity)

    @Query("DELETE FROM recent_searches WHERE userId = :userId")
    suspend fun delete(userId: String)

    @Query("DELETE FROM recent_searches")
    suspend fun clearAll()

    @Query(
        """
        DELETE FROM recent_searches
        WHERE userId NOT IN (
            SELECT userId
            FROM recent_searches
            ORDER BY searchedAt DESC
            LIMIT 10
        )
        """
    )
    suspend fun deleteOlderThan10()
}