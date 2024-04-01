package com.example.trainingtracker.ui.exerciseLog

import java.time.LocalDateTime
import java.io.Serializable
import java.util.UUID

// TODO : need serialisable?

data class ExerciseSet(
    val dateTime: LocalDateTime,
    val exerciseCard: UUID,
    val mass: Float?,
    val set: Int?,
    val rep: Int?
) : Serializable
