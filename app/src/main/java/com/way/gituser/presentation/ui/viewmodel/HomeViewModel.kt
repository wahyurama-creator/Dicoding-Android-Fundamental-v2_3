package com.way.gituser.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.way.gituser.data.remote.network.ApiConfig
import com.way.gituser.data.remote.response.Item
import com.way.gituser.data.remote.response.Search
import com.way.gituser.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    val search: MutableLiveData<Response<Search>> = MutableLiveData()
    val randomUser: MutableLiveData<Response<List<Item>>> = MutableLiveData()

    suspend fun getRandomUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService().getRandomUser()
                randomUser.postValue(response)
            } catch (e: Exception) {
                Log.e(USER, e.message.toString())
            }
        }
    }

    suspend fun getSearchedUser(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService().getSearchedUser(searchQuery)
                search.postValue(response)
            } catch (e: Exception) {
                Log.e(SEARCH, e.message.toString())
            }
        }
    }

    companion object {
        const val SEARCH = "search_response"
        const val USER = "user_response"
    }
}