package com.example.trainingtracker.ui.exerciseLog

import java.time.LocalDateTime
import java.io.Serializable
import java.util.UUID

// TODO : need serialisable?

data class ExerciseSet(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val exerciseCard: UUID,
    val mass: Float? = null,
    val set: Int? = null,
    val rep: Int? = null,
    val oneRepMax: Float? = null
) : Serializable
