package com.way.gituser.data.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("followers")
    val followers: Int?,
    @SerializedName("following")
    val following: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("login")
    val username: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("public_repos")
    val repository: Int?,
) : Serializable