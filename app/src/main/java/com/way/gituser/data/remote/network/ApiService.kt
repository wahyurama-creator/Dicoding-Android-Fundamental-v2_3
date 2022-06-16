package com.way.gituser.data.remote.network

import com.way.gituser.data.remote.response.Item
import com.way.gituser.data.remote.response.Search
import com.way.gituser.data.remote.response.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getRandomUser(): Response<List<Item>>

    @GET("search/users")
    suspend fun getSearchedUser(
        @Query("q")
        searchQuery: String
    ): Response<Search>

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username")
        username: String
    ): Response<User>

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username")
        username: String
    ): Response<List<Item>>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username")
        username: String
    ): Response<List<Item>>
}