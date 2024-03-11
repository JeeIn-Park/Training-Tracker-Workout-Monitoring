package com.example.trainingtracker.ui.exerciseLog

import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import java.time.LocalDateTime
import java.io.Serializable
// TODO : need serialisable?

data class ExerciseSet(
    val dateTime: LocalDateTime,
    val exerciseCard: ExerciseCard,
    val mass: Float?,
    val set: Int?,
    val rep: Int?
) : Serializable
