package com.example.trainingtracker.ui.exerciseLog

import android.content.Context
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.muscles.MuscleStatus
import com.example.trainingtracker.ui.muscles.MuscleStorage
import java.time.LocalDateTime

object LogExercise {

    fun logExercise(
        context: Context,
        exerciseSetList: List<ExerciseSet>,
        exerciseCard: ExerciseCard,
        exerciseDate: LocalDateTime,
        logStorage: LogStorage) {

        val log = ExerciseLog(
            dateTime = exerciseDate,
            exerciseCard = exerciseCard.id,
            exerciseSetList = exerciseSetList,
            totalSet = null,
            oneRepMax = null
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
            oneRepMaxRecord = oneRepMaxRecord_pb(logStorage.loadLogs(context))
        )
        CardStorage.editCard(context, exerciseCard, updatedCard)

        for (muscle in exerciseCard.mainMuscles) {
            MuscleStorage.updateMuscle(context, muscle, Muscle( exerciseDate, MuscleStatus.RECOVERING, muscle.name, muscle.layout))
        }
        for (muscle in exerciseCard.subMuscles) {
            MuscleStorage.updateMuscle(context, muscle, Muscle( exerciseDate, MuscleStatus.RECOVERING, muscle.name, muscle.layout))
        }
    }

    fun oneRepMaxRecord_pb(logs : List<ExerciseLog>) : Float {
        var finalOneRepMax : Float = 0F
        logs.forEach{
            val sets = it.exerciseSetList
            sets.forEach{
                val mass = it.mass
                val rep = it.rep
                if ( (mass != null) && (rep != null)){
                    val tempOneRepMax : Float = oneRepMax(mass, rep)
                    finalOneRepMax = maxOf(finalOneRepMax, tempOneRepMax)
                }
            }
        }
        return finalOneRepMax
    }


    fun oneRepMaxRecord(log : ExerciseLog) : Float {
        var finalOneRepMax : Float = 0F
        val sets = log.exerciseSetList
        sets.forEach{
            val mass = it.mass
            val rep = it.rep
            if ( (mass != null) && (rep != null)){
                val tempOneRepMax : Float = oneRepMax(mass, rep)
                finalOneRepMax = maxOf(finalOneRepMax, tempOneRepMax)
            }
        }
        return finalOneRepMax
    }

    fun oneRepMax(mass: Float, reps: Int): Float{
           return ((mass * (36f / (37 - reps))) +
                    (mass * (1f + (0.0333f * reps))) +
                    (mass * Math.pow(reps.toDouble(), 0.1).toFloat()) +
                    (mass * (1f + (0.025f * reps)))) / 4
    }
}