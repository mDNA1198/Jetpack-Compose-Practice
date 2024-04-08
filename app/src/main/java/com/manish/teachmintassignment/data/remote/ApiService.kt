package com.manish.teachmintassignment.data.remote

import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.domain.models.GitRepoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/repositories")
    suspend fun getGitRepoList(
        @Query("q") search: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<GitRepoSearchResponse>?

    @GET("repos/{repo_owner}/{repo_name}")
    suspend fun getRepoDetails(
        @Path("repo_owner") repoOwner: String,
        @Path("repo_name") repoName: String,
    ): Response<GitRepoItem>?

}