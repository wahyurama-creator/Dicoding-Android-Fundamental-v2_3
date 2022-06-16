package com.way.gituser.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.way.gituser.data.local.entity.UserEntity
import com.way.gituser.data.repository.UserRepository

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {
    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return repository.getFavoriteUser()
    }
}