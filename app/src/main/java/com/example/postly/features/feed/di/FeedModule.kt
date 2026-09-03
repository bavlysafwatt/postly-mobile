package com.example.postly.features.feed.di

import com.example.postly.features.feed.data.remote.FeedApi
import com.example.postly.features.feed.data.repository.FeedRepositoryImpl
import com.example.postly.features.feed.domain.repository.FeedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedModule {

    @Binds
    @Singleton
    abstract fun bindFeedRepository(impl: FeedRepositoryImpl): FeedRepository

    companion object {
        @Provides
        @Singleton
        fun provideFeedApi(retrofit: Retrofit): FeedApi = retrofit.create(FeedApi::class.java)
    }
}