package com.manish.teachmintassignment.di

import android.app.Application
import com.manish.teachmintassignment.data.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application): LocalDatabase =
        LocalDatabase.buildDatabase(application)
}