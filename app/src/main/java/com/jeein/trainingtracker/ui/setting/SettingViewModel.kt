package com.jeein.trainingtracker.ui.setting

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class SettingViewModel(application: Application) : AndroidViewModel(application) {


    // text
    private val _text = MutableLiveData<String>().apply {
        try {
            val appVersion = application.packageManager.getPackageInfo(application.packageName, 0).versionName
            value = "app version : $appVersion"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
    val text: LiveData<String> = _text

}