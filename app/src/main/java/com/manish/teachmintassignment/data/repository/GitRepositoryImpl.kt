package com.manish.teachmintassignment.data.repository

import com.manish.teachmintassignment.data.remote.ApiService
import com.manish.teachmintassignment.data.remote.NetworkResult
import com.manish.teachmintassignment.domain.models.GitRepoSearchResponse
import com.manish.teachmintassignment.domain.repository.GitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response



class GitRepositoryImpl(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GitRepository {

    override fun searchRepositories(searchKeyword: String, page: Int, perPage: Int): Flow<NetworkResult<Response<GitRepoSearchResponse>?>> =
        flow {
            emit(NetworkResult.Loading())
            try {
                val response = apiService.getGitRepoList(searchKeyword, page, perPage)
                emit(NetworkResult.Success(response))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message ?: e.toString()))
            }
        }.flowOn(ioDispatcher)

}