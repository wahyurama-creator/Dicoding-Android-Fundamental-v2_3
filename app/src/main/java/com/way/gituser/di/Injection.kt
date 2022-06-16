package com.way.gituser.di

import android.content.Context
import com.way.gituser.data.local.room.UserDatabase
import com.way.gituser.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(dao)
    }
}