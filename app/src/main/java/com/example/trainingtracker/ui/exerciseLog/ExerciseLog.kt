package com.example.trainingtracker.ui.exerciseLog

import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import java.time.LocalDateTime

data class ExerciseLog(
    val dateTime: LocalDateTime,
    val exerciseCard: ExerciseCard,
    val list: List<ExerciseSet>,
    val totalSet: Int?,
    val totalWeight: Float?
)