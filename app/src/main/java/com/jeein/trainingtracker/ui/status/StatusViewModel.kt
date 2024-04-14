package com.jeein.trainingtracker.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.tag.Tag

class StatusViewModel : ViewModel() {

    // card recycler
    private val _cardRecyclerViewData = MutableLiveData<List<ExerciseCard>>()
    val cardRecyclerViewData : LiveData<List<ExerciseCard>> = _cardRecyclerViewData
    fun updateCardRecyclerViewData(data : List<ExerciseCard>) {
        _cardRecyclerViewData.value = data
    }

    // tag recycler
    private val _tagRecyclerViewData = MutableLiveData<List<Tag>>()
    val tagRecyclerViewData : LiveData<List<Tag>> = _tagRecyclerViewData
    fun updateTagRecyclerViewData(data : List<Tag>) {
        _tagRecyclerViewData.value = data
    }

}