package com.way.gituser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.way.gituser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserFavorite(favorite: List<UserEntity>)

    @Query("SELECT * FROM user")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username)")
    fun checkIsFavorite(username: String): Boolean

    @Query("DELETE FROM user WHERE username = :username")
    fun deleteUserFavorite(username: String)
}