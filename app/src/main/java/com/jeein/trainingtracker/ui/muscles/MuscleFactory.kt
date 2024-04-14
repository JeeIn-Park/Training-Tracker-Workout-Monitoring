package com.jeein.trainingtracker.ui.muscles

import android.content.Context
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
//import com.example.trainingtracker.databinding.FragmentMuscleBackBinding
//import com.example.trainingtracker.databinding.FragmentMuscleFrontBinding
import java.time.LocalDateTime
import java.time.Duration

object MuscleFactory {
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


    fun updateMuscle(context: Context, card: ExerciseCard){
        for (muscle in card.mainMuscles) {
            MuscleStorage.editMuscle(
                context,
                muscle,
                Muscle( card.lastActivity, RECOVERING, muscle.name, muscle.layout))
        }
        for (muscle in card.subMuscles) {
            MuscleStorage.editMuscle(
                context,
                muscle,
                Muscle( card.lastActivity, RECOVERING, muscle.name, muscle.layout))
        }
    }


    fun refreshMuscle(context : Context){
        val muscleList = MuscleStorage.loadMuscles(context)
        val updatedMuscleList : MutableList<Muscle> = listOf<Muscle>().toMutableList()

        for(muscle in muscleList) {
            updatedMuscleList.add( muscleState(context, muscle))
        }
        MuscleStorage.updateMuscles(context, updatedMuscleList)
    }


    fun getDrawableResourceIdByStatus(context: Context, status: Int, drawableName: String): Int {
        return when (status) {
            RECOVERED -> context.resources.getIdentifier("${drawableName}_recovered", "drawable", context.packageName)
            RECOVERING -> context.resources.getIdentifier("${drawableName}_recovering", "drawable", context.packageName)
            NEED_EXERCISE -> context.resources.getIdentifier("${drawableName}_need_exercise", "drawable", context.packageName)
            else -> context.resources.getIdentifier(drawableName, "drawable", context.packageName)
        }
    }
}
