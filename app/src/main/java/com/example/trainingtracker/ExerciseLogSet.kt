package com.example.trainingtracker

import java.time.LocalDateTime

data class ExerciseLogSet(
    val dateTime: LocalDateTime,
    val exerciseCard: ExerciseCard,
    val list: List<ExerciseLog>,
    val totalSet: Int?,
    val totalWeight: Float?
)