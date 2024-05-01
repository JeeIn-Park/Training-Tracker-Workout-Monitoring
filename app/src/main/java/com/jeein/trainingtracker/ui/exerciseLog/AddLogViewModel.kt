package com.jeein.trainingtracker.ui.exerciseLog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeein.trainingtracker.ui.exerciseSet.ExerciseSet

class AddLogViewModel : ViewModel() {

    // past log (sets)
    private val _setRecyclerViewData = MutableLiveData<List<ExerciseSet>>()
    val setRecyclerViewData: LiveData<List<ExerciseSet>> = _setRecyclerViewData
    fun updateSetRecyclerViewData(data: List<ExerciseSet>) {
        _setRecyclerViewData.value = data
    }

}