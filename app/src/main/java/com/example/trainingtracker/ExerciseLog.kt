package com.example.trainingtracker

import android.health.connect.datatypes.units.Mass
import java.time.LocalDateTime

data class ExerciseLog(
    val dateTime: LocalDateTime,
    val exerciseCard: ExerciseCard,
    val mass: Float?,
    val set: Int?,
    val rep: Int?
)
