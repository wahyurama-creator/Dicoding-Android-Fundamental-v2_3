package com.way.gituser.presentation.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.way.gituser.data.local.datastore.SettingPreferences
import com.way.gituser.data.repository.UserRepository
import com.way.gituser.di.Injection

class ViewModelFactory(
    private val repository: UserRepository?,
    private val settingPreferences: SettingPreferences?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return repository?.let { HomeViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return repository?.let { DetailViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return repository?.let { FavoriteViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return settingPreferences?.let { SettingsViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), null)
            }.also { instance = it }
    }
}