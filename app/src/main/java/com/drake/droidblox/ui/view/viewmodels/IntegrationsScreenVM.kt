package com.drake.droidblox.ui.view.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.drake.droidblox.sharedprefs.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class IntegrationsScreenVM @Inject constructor(
    @ApplicationContext private val context: Context?,
    val settingsManager: SettingsManager
) : ViewModel() {
    fun launchRoblox() = context?.let {
        com.drake.droidblox.roblox.launchRoblox(context = it)
    }
}
