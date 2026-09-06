package com.example.postly.features.settings.di

import com.example.postly.features.settings.data.remote.SettingsApi
import com.example.postly.features.settings.data.repository.SettingsRepositoryImpl
import com.example.postly.features.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    companion object {
        @Provides
        @Singleton
        fun provideSettingsApi(retrofit: Retrofit): SettingsApi =
            retrofit.create(SettingsApi::class.java)
    }
}