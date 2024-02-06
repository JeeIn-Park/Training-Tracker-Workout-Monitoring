package com.example.trainingtracker.ui.MyMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainingtracker.ExerciseCard

class SettingViewModel : ViewModel() {

    // text
    private val _text = MutableLiveData<String>().apply {
        value = "Select an exercise to enter ..."
    }
    val text: LiveData<String> = _text

    // recycler
    private val _recyclerViewData = MutableLiveData<List<ExerciseCard>>()
    val recyclerViewData : LiveData<List<ExerciseCard>> = _recyclerViewData

    fun updateRecyclerViewData(data : List<ExerciseCard>) {
        _recyclerViewData.value = data
    }
}