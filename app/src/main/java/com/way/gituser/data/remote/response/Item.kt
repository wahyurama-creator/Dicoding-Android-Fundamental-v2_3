package com.way.gituser.data.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Item(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("events_url")
    val eventsUrl: String,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following_url")
    val followingUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String,
    @SerializedName("repos_url")
    val reposUrl: String,
    @SerializedName("url")
    val url: String
) : Serializable