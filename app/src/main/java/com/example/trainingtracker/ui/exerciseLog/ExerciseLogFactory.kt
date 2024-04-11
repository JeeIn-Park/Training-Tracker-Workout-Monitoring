package com.example.trainingtracker.ui.exerciseLog

import android.content.Context
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.example.trainingtracker.ui.exerciseCard.ExerciseCardFactory
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.muscles.MuscleStatus
import com.example.trainingtracker.ui.muscles.MuscleStorage
import java.time.LocalDateTime

object ExerciseLogFactory {

    fun createExerciseLog(exerciseSetList: List<ExerciseSet>) : ExerciseLog{
        return ExerciseLog(
            dateTime = exerciseSetList[exerciseSetList.lastIndex].dateTime,
            exerciseCard = exerciseSetList[0].exerciseCard,
            exerciseSetList = exerciseSetList,
            oneRepMax = (exerciseSetList.maxBy { it.oneRepMax ?: Float.MIN_VALUE }.oneRepMax))
    }

    fun logExercise(
        context: Context,
        exerciseSetList: List<ExerciseSet>,
        exerciseCard: ExerciseCard,
        exerciseDate: LocalDateTime,
        logStorage: LogStorage) {

        val exerciseLog = createExerciseLog(exerciseSetList)
        logStorage.addLog(context, exerciseLog)

        CardStorage.editCard(
            context, exerciseCard, ExerciseCardFactory.updateExerciseCard(exerciseCard, exerciseLog, exerciseDate))

        // TODO : muscle factory
        for (muscle in exerciseCard.mainMuscles) {
            MuscleStorage.updateMuscle(context, muscle, Muscle( exerciseDate, MuscleStatus.RECOVERING, muscle.name, muscle.layout))
        }
        for (muscle in exerciseCard.subMuscles) {
            MuscleStorage.updateMuscle(context, muscle, Muscle( exerciseDate, MuscleStatus.RECOVERING, muscle.name, muscle.layout))
        }
    }
}