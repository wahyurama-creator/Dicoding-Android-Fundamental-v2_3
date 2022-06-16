package com.way.gituser.data.repository

import androidx.lifecycle.LiveData
import com.way.gituser.data.local.entity.UserEntity
import com.way.gituser.data.local.room.UserDao
import com.way.gituser.data.remote.response.User

class UserRepository(private val userDao: UserDao) {

    fun setFavorite(user: User) {
        val userToEntity = UserEntity(
            user.id,
            user.avatarUrl,
            user.company,
            user.followers,
            user.following,
            user.location,
            user.username,
            user.name,
            user.repository,
        )
        val listUserEntity = ArrayList<UserEntity>()
        listUserEntity.add(userToEntity)
        userDao.insertUserFavorite(listUserEntity)
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUser()
    }

    fun checkExistOrNot(username: String): Boolean {
        return userDao.checkIsFavorite(username)
    }

    fun deleteUserFavorite(username: String) {
        userDao.deleteUserFavorite(username)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userDao: UserDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDao)
            }.also { instance = it }
    }
}