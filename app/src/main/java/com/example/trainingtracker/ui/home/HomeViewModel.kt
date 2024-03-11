package com.example.trainingtracker.ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard

class HomeViewModel : ViewModel() {

    // text
    private val _text = MutableLiveData<String>().apply {
        value = "Select exercise"
    }
    val text: LiveData<String> = _text

    // recycler
    private val _recyclerViewData = MutableLiveData<List<ExerciseCard>>()
    val recyclerViewData : LiveData<List<ExerciseCard>> = _recyclerViewData

    fun updateRecyclerViewData(data : List<ExerciseCard>) {
        _recyclerViewData.value = data
    }

}