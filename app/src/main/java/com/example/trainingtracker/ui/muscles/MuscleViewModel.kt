package com.example.trainingtracker.ui.muscles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainingtracker.R

class MuscleViewModel : ViewModel() {
    private val _muscleColor = MutableLiveData<Int>()
    val muscleColor: LiveData<Int> = _muscleColor

    fun updateMuscleStatus(status: Int) {
        when (status) {

            MuscleStatus.RECOVERED -> R.color.turquoise
            MuscleStatus.RECOVERING -> R.color.grey
            MuscleStatus.NEED_EXERCISE -> R.color.cafeLatte
            else -> _muscleColor.value = R.color.red
        }
    }
}
