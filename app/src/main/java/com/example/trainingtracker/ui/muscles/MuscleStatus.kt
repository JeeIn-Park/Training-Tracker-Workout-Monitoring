package com.example.trainingtracker.ui.muscles

import android.content.Context
//import com.example.trainingtracker.databinding.FragmentMuscleBackBinding
//import com.example.trainingtracker.databinding.FragmentMuscleFrontBinding
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.example.trainingtracker.ui.exerciseLog.ExerciseLog
import com.example.trainingtracker.ui.exerciseLog.ExerciseSet
import com.example.trainingtracker.ui.exerciseLog.LogStorage
import com.example.trainingtracker.ui.exerciseLog.OneRepMax
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

    private fun muscleState(context: Context, muscle: Muscle): Muscle {
        var updatedMuscle = muscle
        if (muscle.lastActivity == null) {
            updatedMuscle.lastActivity = LocalDateTime.now()
        } else if (10 < restingTime(muscle.lastActivity, IN_DAYS)) {
            updatedMuscle.status = NEED_EXERCISE
        } else if (50 < restingTime(muscle.lastActivity, IN_HOURS)) {
            updatedMuscle.status = RECOVERED
        }
        return updatedMuscle
    }

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

    fun refreshMuscle(context : Context){
        val muscleList = MuscleStorage.loadMuscles(context)
        val updatedMuscleList : MutableList<Muscle> = listOf<Muscle>().toMutableList()

        for(muscle in muscleList) {
            updatedMuscleList.add( muscleState(context, muscle))
        }
        MuscleStorage.saveMuscles(context, updatedMuscleList)
    }

    fun logExercise(
        context: Context,
        exerciseSetList: List<ExerciseSet>,
        exerciseCard: ExerciseCard,
        exerciseDate: LocalDateTime,
        logStorage: LogStorage) {

        var muscles : MutableList<Muscle> = MuscleStorage.loadMuscles(context).toMutableList()
        val log = ExerciseLog(
            dateTime = exerciseDate,
            exerciseCard = exerciseCard.id,
            exerciseSetList = exerciseSetList,
            totalSet = null,
            totalWeight = null
        )
        logStorage.addLog(context, log)

        val updatedCard = ExerciseCard(
            id = exerciseCard.id,
            lastActivity = exerciseDate,
            timeAdded = exerciseCard.timeAdded,
            name = exerciseCard.name,
            mainMuscles = exerciseCard.mainMuscles,
            subMuscles = exerciseCard.subMuscles,
            tag = exerciseCard.tag,
            oneRepMax = OneRepMax.oneRepMaxRecord_pb(logStorage.loadLogs(context))
        )
        CardStorage.editCard(context, exerciseCard, updatedCard)

        for (muscle in exerciseCard.mainMuscles) {
            val updatedMuscle = Muscle(
                exerciseDate,
                RECOVERING,
                muscle.name,
                muscle.layout
            )
            MuscleStorage.updateMuscle(context, muscle, updatedMuscle)
        }
        for (muscle in exerciseCard.subMuscles) {
            val updatedMuscle = Muscle(
                exerciseDate,
                RECOVERING,
                muscle.name,
                muscle.layout
            )
            MuscleStorage.updateMuscle(context, muscle, updatedMuscle)
        }
    }

}
