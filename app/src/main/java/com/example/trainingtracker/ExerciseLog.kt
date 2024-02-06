package com.example.trainingtracker

import android.health.connect.datatypes.WeightRecord
import java.time.LocalDateTime

data class ExerciseLog (
    val exerciseCard: ExerciseCard,
    val time: LocalDateTime,
    val rep : Int,
    val weightRecord: WeightRecord
)

