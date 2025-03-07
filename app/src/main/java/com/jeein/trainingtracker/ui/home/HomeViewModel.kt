package com.jeein.trainingtracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.muscles.Muscle
import com.jeein.trainingtracker.ui.tag.Tag

class HomeViewModel : ViewModel() {

    // text
    private val _text = MutableLiveData<String>().apply {
        value = "Select exercise"
    }
    val text: LiveData<String> = _text

    // card recycler
    private val _cardRecyclerViewData = MutableLiveData<List<ExerciseCard>>()
    val cardRecyclerViewData: LiveData<List<ExerciseCard>> = _cardRecyclerViewData
    fun updateCardRecyclerViewData(data: List<ExerciseCard>) {
        _cardRecyclerViewData.value = data
    }

    // tag recycler
    // this enable the submit list method
    private val _tagRecyclerViewData = MutableLiveData<List<Tag>>()
    val tagRecyclerViewData: LiveData<List<Tag>> = _tagRecyclerViewData
    fun updateTagRecyclerViewData(data: List<Tag>) {
        _tagRecyclerViewData.value = data
    }

    // muscle
    private val _muscleViewData = MutableLiveData<List<Muscle>>()
    val muscleViewData: LiveData<List<Muscle>> = _muscleViewData
    fun updateMuscleViewData(data: List<Muscle>) {
        _muscleViewData.value = data
    }

}