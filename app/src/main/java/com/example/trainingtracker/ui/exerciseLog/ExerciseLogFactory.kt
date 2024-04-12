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

    fun createEmptyExerciseLog(card: ExerciseCard): ExerciseLog{
        return ExerciseLog(
            dateTime = LocalDateTime.now(),
            exerciseCard = card.id,
            exerciseSetList = emptyList(),
            oneRepMax = null)
    }

    fun createExerciseLog(exerciseSetList: List<ExerciseSet>) : ExerciseLog{
        return ExerciseLog(
            dateTime = exerciseSetList[exerciseSetList.lastIndex].dateTime,
            exerciseCard = exerciseSetList[0].exerciseCard,
            exerciseSetList = exerciseSetList,
            oneRepMax = (exerciseSetList.maxBy { it.oneRepMax ?: Float.MIN_VALUE }.oneRepMax))
    }
}