package com.example.trainingtracker.ui.exerciseCard

import android.content.Context
import com.example.trainingtracker.ui.exerciseLog.ExerciseLog
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.tag.Tag
import java.time.LocalDateTime
import java.util.UUID

object ExerciseCardFactory {

    fun createExerciseCard(context: Context, name: String): ExerciseCard {
        val id = generateUniqueId(context)
        return ExerciseCard(id = id, name = name)
    }

    fun editExerciseCard(
        originalCard: ExerciseCard, name: String, mainMuscles: List<Muscle>, subMuscles: List<Muscle>, tags: List<Tag>)
    : ExerciseCard{
        return ExerciseCard(
            originalCard.id, originalCard.timeAdded, originalCard.lastActivity, name, mainMuscles, subMuscles, tags, originalCard.oneRepMaxRecord, originalCard.oneRepMaxRecordDate
        )
    }


    fun updateExerciseCard(originalCard: ExerciseCard, exerciseLog: ExerciseLog, exerciseDate: LocalDateTime) : ExerciseCard{
        if (
            exerciseLog.oneRepMax ?: Float.MIN_VALUE >= originalCard.oneRepMaxRecord ?: Float.MIN_VALUE)
        { return ExerciseCard(
            originalCard.id,
            originalCard.timeAdded,
            exerciseDate,
            originalCard.name,
            originalCard.mainMuscles,
            originalCard.subMuscles,
            originalCard.tag,
            exerciseLog.oneRepMax,
            exerciseLog.dateTime
        )
        } else return ExerciseCard(
            originalCard.id,
            originalCard.timeAdded,
            exerciseDate,
            originalCard.name,
            originalCard.mainMuscles,
            originalCard.subMuscles,
            originalCard.tag,
            originalCard.oneRepMaxRecord,
            originalCard.oneRepMaxRecordDate
        )
    }

    private fun generateUniqueId(context: Context): UUID {
        var uniqueId: UUID
        do {
            uniqueId = UUID.randomUUID()
        } while (CardStorage.isIdInUse(context, uniqueId))
        return uniqueId
    }

}