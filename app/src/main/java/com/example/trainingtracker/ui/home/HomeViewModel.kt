package com.example.trainingtracker.ui.home

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

    // card recycler
    private val _cardRecyclerViewData = MutableLiveData<List<ExerciseCard>>()
    val cardRecyclerViewData : LiveData<List<ExerciseCard>> = _cardRecyclerViewData
    fun updateCardRecyclerViewData(data : List<ExerciseCard>) {
        _cardRecyclerViewData.value = data
    }

    // tag recycler
    private val _tagRecyclerViewData = MutableLiveData<List<String>>()
    val tagRecyclerViewData : LiveData<List<String>> = _tagRecyclerViewData
    fun updateTagRecyclerViewData(data : List<String>) {
        _tagRecyclerViewData.value = data
    }

}