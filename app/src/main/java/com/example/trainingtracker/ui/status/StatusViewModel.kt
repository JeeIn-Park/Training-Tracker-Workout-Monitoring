package com.example.trainingtracker.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatusViewModel : ViewModel() {

    // tag recycler
    private val _tagRecyclerViewData = MutableLiveData<List<String>>()
    val tagRecyclerViewData : LiveData<List<String>> = _tagRecyclerViewData
    fun updateTagRecyclerViewData(data : List<String>) {
        _tagRecyclerViewData.value = data
    }

}