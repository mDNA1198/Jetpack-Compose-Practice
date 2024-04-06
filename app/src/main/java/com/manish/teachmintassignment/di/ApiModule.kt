package com.manish.teachmintassignment.di

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.manish.teachmintassignment.BuildConfig
import com.manish.teachmintassignment.R
import com.manish.teachmintassignment.data.remote.ApiService
import com.manish.teachmintassignment.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(Constants.API_MEDIA_TYPE.toMediaType()))
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun getInterceptor(
        lclApp: Application,
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(Interceptor { chain ->
                val request = getRequest(chain)

                val response: Response
                try {
                    response = chain.proceed(request)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Timber.d(e)
                    val s = if (e is UnknownHostException) {
                        if (!isInternetAvailable()) {
                            Timber.d(lclApp.getString(R.string.internet_not_available))
                            lclApp.getString(R.string.internet_not_available)
                        } else {
                            lclApp.getString(R.string.server_unreachable)
                        }
                    } else {
                        lclApp.getString(R.string.server_unreachable)
                    }
                    val body =
                        s.toResponseBody(contentType = Constants.API_MEDIA_TYPE.toMediaType())
                    return@Interceptor Response.Builder().code(Constants.CustomErrorCode)
                        .message(s)
                        .request(request)
                        .protocol(Protocol.HTTP_1_1).body(body).build()
                }

                val s = when (response.code) {
                    404 -> {
                        Timber.d("$$request | $response")
                        lclApp.getString(R.string.server_unreachable)
                    }

                    500 -> lclApp.getString(R.string.server_500_error_msg)
                    else -> return@Interceptor response
                }
                val body =
                    s.toResponseBody(contentType = Constants.API_MEDIA_TYPE.toMediaType())
                Response.Builder().code(Constants.CustomErrorCode)
                    .message(s)
                    .request(request)
                    .build()
            })
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor { message ->
                        Timber.tag("OkHttp").d(message)
                    }.apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
                connectTimeout(1L, TimeUnit.MINUTES)
                callTimeout(1L, TimeUnit.MINUTES)
                readTimeout(1L, TimeUnit.MINUTES)
                writeTimeout(1L, TimeUnit.MINUTES)
            }.build()
    }

    private fun getRequest(chain: Interceptor.Chain): Request {
        val original = chain.request()

        val request = original.newBuilder().also {
            it.header("Accept", "application/vnd.github+json")
            it.header("X-GitHub-Api-Version", "2022-11-28")
        }.method(original.method, original.body).build()

        return request
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val command = "ping -c 1 google.com"
            Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: java.lang.Exception) {
            false
        }
    }
}
