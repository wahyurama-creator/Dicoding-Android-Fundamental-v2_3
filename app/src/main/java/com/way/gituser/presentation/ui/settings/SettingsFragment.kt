package com.way.gituser.presentation.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.way.gituser.R
import com.way.gituser.data.local.datastore.SettingPreferences
import com.way.gituser.databinding.FragmentSettingsBinding
import com.way.gituser.presentation.ui.viewmodel.SettingsViewModel
import com.way.gituser.presentation.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingPref = SettingPreferences.getInstance(requireContext().dataStore)

        settingsViewModel =
            ViewModelProvider(
                requireParentFragment(),
                ViewModelFactory(null, settingPref)
            )[SettingsViewModel::class.java]

        getTheme()

        binding.apply {
            switchTheme.setOnCheckedChangeListener { _, p1 ->
                settingsViewModel.saveTheme(p1)
            }
        }
    }

    private fun getTheme() {
        settingsViewModel.getTheme().observe(viewLifecycleOwner) { state ->
            if (state) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
                binding.tvTheme.text = getString(R.string.change_theme, "Light")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
                binding.tvTheme.text = getString(R.string.change_theme, "Dark")
            }
        }
    }

}