package com.jeein.trainingtracker.ui.exerciseLog

import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
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