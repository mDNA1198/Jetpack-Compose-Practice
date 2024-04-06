package com.manish.teachmintassignment.domain.repository

import com.manish.teachmintassignment.data.remote.NetworkResult
import com.manish.teachmintassignment.domain.models.GitRepoSearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface GitRepository {

    fun searchRepositories(searchKeyword: String, page: Int, perPage: Int): Flow<NetworkResult<Response<GitRepoSearchResponse>?>>

}