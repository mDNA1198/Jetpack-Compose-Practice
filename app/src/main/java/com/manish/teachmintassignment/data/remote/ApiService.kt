package com.manish.teachmintassignment.data.remote

import com.manish.teachmintassignment.domain.models.GitRepoSearchResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/repositories")
    suspend fun getGitRepoList(
        @Query("q") search: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<GitRepoSearchResponse>?

}