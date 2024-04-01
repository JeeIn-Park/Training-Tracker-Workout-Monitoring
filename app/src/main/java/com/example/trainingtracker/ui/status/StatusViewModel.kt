package com.example.trainingtracker.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainingtracker.ui.tag.Tag

class StatusViewModel : ViewModel() {

    // tag recycler
    private val _tagRecyclerViewData = MutableLiveData<List<Tag>>()
    val tagRecyclerViewData : LiveData<List<Tag>> = _tagRecyclerViewData
    fun updateTagRecyclerViewData(data : List<Tag>) {
        _tagRecyclerViewData.value = data
    }

}