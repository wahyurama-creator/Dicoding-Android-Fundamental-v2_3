package com.way.gituser.data.remote.network

import com.way.gituser.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "token ${BuildConfig.Token}")
                        .addHeader("Accept", BuildConfig.HeaderAccept)
                        .build()
                    chain.proceed(newRequest)
                })
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}