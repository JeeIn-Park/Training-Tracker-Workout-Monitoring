package com.example.trainingtracker.ui.exerciseCard

import android.content.Context
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.tag.Tag
import java.util.UUID

object ExerciseCardFactory {

    fun createExerciseCard(context: Context, name: String): ExerciseCard {
        val id = generateUniqueId(context)
        return ExerciseCard(id = id, name = name)
    }

    fun updateExerciseCard(
        originalCard: ExerciseCard, name: String, mainMuscles: List<Muscle>, subMuscles: List<Muscle>, tags: List<Tag>)
    : ExerciseCard{
        return ExerciseCard(
            originalCard.id, originalCard.timeAdded, originalCard.lastActivity, name, mainMuscles, subMuscles, tags, originalCard.oneRepMaxRecord, originalCard.oneRepMaxRecordDate
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