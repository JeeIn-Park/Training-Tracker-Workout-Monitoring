package com.jeein.trainingtracker.ui.exerciseLog

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class ExerciseLog(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val exerciseCard: UUID,
    var exerciseSetList: List<ExerciseSet> = emptyList(),
    var oneRepMax: Float? = null
) : Serializable