package com.manish.teachmintassignment.di

import com.manish.teachmintassignment.data.remote.ApiService
import com.manish.teachmintassignment.data.repository.GitRepositoryImpl
import com.manish.teachmintassignment.domain.repository.GitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providesGitRepository(
        apiService: ApiService,
        ioDispatcher: CoroutineDispatcher
    ): GitRepository =
        GitRepositoryImpl(apiService, ioDispatcher)

    @Singleton
    @Provides
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}