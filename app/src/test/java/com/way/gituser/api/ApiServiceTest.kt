package com.way.gituser.api

import com.google.common.truth.Truth
import com.way.gituser.BuildConfig
import com.way.gituser.data.remote.network.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {
    private lateinit var apiService: ApiService
    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "token ${BuildConfig.Token}")
                    .addHeader("Accept", BuildConfig.HeaderAccept)
                    .build()
                chain.proceed(newRequest)
            })
            .build()
        apiService = Retrofit.Builder()
            .baseUrl(mockServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun getRandomUser_receivedResponse_correctPageSize() {
        runBlocking {
            Enqueue.enqueueMockResponse("random_user_response.json", mockServer)
            val responseBody = apiService.getRandomUser().body()
            val articleList = responseBody?.size
            Truth.assertThat(articleList).isEqualTo(30)
        }
    }

    @Test
    fun getRandomUser_receivedResponse_correctContent() {
        runBlocking {
            Enqueue.enqueueMockResponse("random_user_response.json", mockServer)
            val responseBody = apiService.getRandomUser().body()
            val user = responseBody?.get(0)
            Truth.assertThat(user?.login).isEqualTo("mojombo")
            Truth.assertThat(user?.id).isEqualTo(1)
            Truth.assertThat(user?.avatarUrl).isEqualTo("https://avatars.githubusercontent.com/u/1?v=4")
            Truth.assertThat(user?.url).isEqualTo("https://api.github.com/users/mojombo")
            Truth.assertThat(user?.eventsUrl).isEqualTo("https://api.github.com/users/mojombo/events{/privacy}")
            Truth.assertThat(user?.followersUrl).isEqualTo("https://api.github.com/users/mojombo/followers")
            Truth.assertThat(user?.followingUrl).isEqualTo("https://api.github.com/users/mojombo/following{/other_user}")
            Truth.assertThat(user?.reposUrl).isEqualTo("https://api.github.com/users/mojombo/repos")
        }
    }
}