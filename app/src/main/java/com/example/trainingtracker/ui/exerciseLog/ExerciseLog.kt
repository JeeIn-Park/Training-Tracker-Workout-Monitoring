package com.example.trainingtracker.ui.exerciseLog

import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import java.time.LocalDateTime
import java.io.Serializable
import java.util.UUID

data class ExerciseLog(
    val dateTime: LocalDateTime,
    val exerciseCard: UUID,
    val exerciseSetList: List<ExerciseSet>,
    val totalSet: Int?,
    val totalWeight: Float?
): Serializable