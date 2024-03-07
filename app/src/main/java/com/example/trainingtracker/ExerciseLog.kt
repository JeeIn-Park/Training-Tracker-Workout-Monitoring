package com.example.trainingtracker

import java.time.LocalDateTime

data class ExerciseLog(
    val dateTime: LocalDateTime,
    val exerciseCard: ExerciseCard,
    val mass: Float?,
    val set: Int?,
    val rep: Int?
)
