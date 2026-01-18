package com.drake.droidblox.ui.view.viewmodels

import androidx.lifecycle.ViewModel
import com.drake.droidblox.sharedprefs.FastFlagsManager
import com.drake.droidblox.sharedprefs.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FFlagsScreenVM @Inject constructor(
    val settingsManager: SettingsManager,
    val fflagsManager: FastFlagsManager
) : ViewModel() {
}