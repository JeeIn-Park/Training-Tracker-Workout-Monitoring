package com.example.trainingtracker.ui.exerciseLog

import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import java.time.LocalDateTime

data class ExerciseLogSet(
    val dateTime: LocalDateTime,
    val exerciseCard: ExerciseCard,
    val list: List<ExerciseLog>,
    val totalSet: Int?,
    val totalWeight: Float?
)