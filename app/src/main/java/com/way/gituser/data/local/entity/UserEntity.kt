package com.way.gituser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val avatarUrl: String?,
    val company: String?,
    val followers: Int?,
    val following: Int?,
    val location: String?,
    val username: String?,
    val name: String?,
    val repository: Int?,
)
