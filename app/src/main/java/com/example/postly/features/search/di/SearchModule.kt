package com.example.postly.features.search.di

import android.content.Context
import androidx.room3.Room
import com.example.postly.features.search.data.local.RecentSearchDao
import com.example.postly.features.search.data.local.RecentSearchDatabase
import com.example.postly.features.search.data.remote.SearchApi
import com.example.postly.features.search.data.repository.SearchRepositoryImpl
import com.example.postly.features.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository

    companion object {
        @Provides
        @Singleton
        fun provideSearchApi(
            retrofit: Retrofit
        ): SearchApi = retrofit.create(SearchApi::class.java)

        @Provides
        @Singleton
        fun provideRecentSearchDatabase(
            @ApplicationContext context: Context
        ): RecentSearchDatabase = Room.databaseBuilder(
            context,
            RecentSearchDatabase::class.java,
            "recent_searches.db"
        ).build()

        @Provides
        @Singleton
        fun provideRecentSearchDao(
            database: RecentSearchDatabase
        ): RecentSearchDao = database.recentSearchDao()
    }
}