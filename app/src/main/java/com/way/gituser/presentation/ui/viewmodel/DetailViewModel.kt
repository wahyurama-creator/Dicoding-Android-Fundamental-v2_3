package com.way.gituser.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.way.gituser.data.remote.network.ApiConfig
import com.way.gituser.data.remote.response.Item
import com.way.gituser.data.remote.response.User
import com.way.gituser.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    val userDetail: MutableLiveData<Response<User>> = MutableLiveData()
    val userFollower: MutableLiveData<Response<List<Item>>> = MutableLiveData()
    val userFollowing: MutableLiveData<Response<List<Item>>> = MutableLiveData()
    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun getUserDetail(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService().getDetailUser(username)
                userDetail.postValue(response)
            } catch (e: Exception) {
                Log.e(USER_DETAIL, e.message.toString())
            }
        }
    }

    suspend fun getFollowerUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService().getFollowers(username)
                userFollower.postValue(response)
            } catch (e: Exception) {
                Log.e(USER_FOLLOWER, e.message.toString())
            }
        }
    }

    suspend fun getFollowingUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService().getFollowing(username)
                userFollowing.postValue(response)
            } catch (e: Exception) {
                Log.e(USER_FOLLOWER, e.message.toString())
            }
        }
    }

    fun setUserFavorite(user: User) {
        viewModelScope.launch {
            repository.setFavorite(user)
        }
    }

    fun deleteUserFavorite(username: String) {
        viewModelScope.launch {
            repository.deleteUserFavorite(username)
        }
    }

    fun checkIsFavorite(username: String) {
        viewModelScope.launch {
            isFavorite.postValue(repository.checkExistOrNot(username))
        }
    }

    companion object {
        const val USER_DETAIL = "user_detail"
        const val USER_FOLLOWER = "user_follower"
        const val USER_FOLLOWING = "user_following"
    }
}