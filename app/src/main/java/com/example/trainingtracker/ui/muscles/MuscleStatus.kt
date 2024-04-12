package com.example.trainingtracker.ui.muscles

import android.content.Context
import android.widget.ImageButton
import com.example.trainingtracker.R
//import com.example.trainingtracker.databinding.FragmentMuscleBackBinding
//import com.example.trainingtracker.databinding.FragmentMuscleFrontBinding
import java.time.LocalDateTime
import java.time.Duration

object MuscleStatus {
    const val RECOVERED = 0
    const val RECOVERING = 1
    const val NEED_EXERCISE = 2

    private const val IN_SECONDS = 0
    private const val IN_MINUTES = 1
    private const val IN_HOURS = 2
    private const val IN_DAYS = 3


    private fun restingTime(lastActivity: LocalDateTime?, mode: Int) : Long {
        val currentDateTime = LocalDateTime.now()
        val duration = Duration.between(lastActivity, currentDateTime)
        return when (mode) {
            IN_SECONDS -> { duration.seconds }
            IN_MINUTES -> { duration.toMinutes() }
            IN_HOURS -> { duration.toHours() }
            IN_DAYS -> { duration.toDays() }
            else -> -1
        }
    }


    private fun muscleState(context: Context, muscle: Muscle): Muscle {
        var updatedMuscle = muscle
        return if (muscle.lastActivity == null) {
            updatedMuscle.lastActivity = LocalDateTime.now()
            updatedMuscle
        } else if (10 < restingTime(muscle.lastActivity, IN_DAYS)) {
            updatedMuscle.status = NEED_EXERCISE
            updatedMuscle
        } else if (50 < restingTime(muscle.lastActivity, IN_HOURS)) {
            updatedMuscle.status = RECOVERED
            updatedMuscle
        } else updatedMuscle
    }


    fun refreshMuscle(context : Context){
        val muscleList = MuscleStorage.loadMuscles(context)
        val updatedMuscleList : MutableList<Muscle> = listOf<Muscle>().toMutableList()

        for(muscle in muscleList) {
            updatedMuscleList.add( muscleState(context, muscle))
        }
        MuscleStorage.updateMuscles(context, updatedMuscleList)
    }


    fun getColorByStatus(status: Int): Int {
        return when (status) {
            MuscleStatus.RECOVERED -> R.color.turquoise
            MuscleStatus.RECOVERING -> R.color.grey
            MuscleStatus.NEED_EXERCISE -> R.color.cafeLatte
            else -> R.color.red
        }
    }


}
