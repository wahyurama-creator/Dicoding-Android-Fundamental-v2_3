package com.way.gituser.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.way.gituser.R
import com.way.gituser.data.local.datastore.SettingPreferences
import com.way.gituser.databinding.ActivityMainBinding
import com.way.gituser.presentation.ui.viewmodel.SettingsViewModel
import com.way.gituser.presentation.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingPref = SettingPreferences.getInstance(dataStore)

        val settingsViewModel = ViewModelProvider(
            viewModelStore,
            ViewModelFactory(null, settingPref)
        )[SettingsViewModel::class.java]

        settingsViewModel.getTheme().observe(this) { state ->
            if (state) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        binding.btmNav.setupWithNavController(navHost.navController)

        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> binding.btmNav.visibility = INVISIBLE
                R.id.homeFragment -> binding.btmNav.visibility = VISIBLE
                R.id.favoriteFragment -> binding.btmNav.visibility = VISIBLE
                R.id.settingsFragment -> binding.btmNav.visibility = VISIBLE
                R.id.detailFragment -> binding.btmNav.visibility = INVISIBLE
            }
        }
    }
}