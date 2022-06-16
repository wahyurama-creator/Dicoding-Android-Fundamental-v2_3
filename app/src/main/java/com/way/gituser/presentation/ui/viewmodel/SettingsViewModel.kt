package com.way.gituser.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.way.gituser.data.local.datastore.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingPref: SettingPreferences) : ViewModel() {
    fun getTheme(): LiveData<Boolean> = settingPref.getTheme().asLiveData()
    fun saveTheme(themeState: Boolean) = viewModelScope.launch { settingPref.saveTheme(themeState) }
}