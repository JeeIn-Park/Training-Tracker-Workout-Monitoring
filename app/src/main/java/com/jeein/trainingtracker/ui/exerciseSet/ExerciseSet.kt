package com.jeein.trainingtracker.ui.exerciseSet

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID


data class ExerciseSet(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val exerciseCard: UUID,
    val mass: Float? = null,
    val set: Int? = null,
    val rep: Int? = null,
    val oneRepMax: Float? = null
) : Serializable
