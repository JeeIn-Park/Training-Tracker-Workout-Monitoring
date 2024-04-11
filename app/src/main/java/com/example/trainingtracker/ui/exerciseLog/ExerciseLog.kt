package com.example.trainingtracker.ui.exerciseLog

import java.time.LocalDateTime
import java.io.Serializable
import java.util.UUID

data class ExerciseLog(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val exerciseCard: UUID,
    val exerciseSetList: List<ExerciseSet> = emptyList(),
    val oneRepMax: Float? = null
): Serializable