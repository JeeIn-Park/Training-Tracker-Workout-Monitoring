package com.example.trainingtracker

import java.io.Serializable
import java.time.LocalDateTime

data class ExerciseCard(
    val id : Int,
    val lastActivity: LocalDateTime,
    val timeAdded : LocalDateTime,
    val name : String,
    val mainMuscles : List<String>,
    val subMuscles : List<String>,
    val tag : List<String>
    // TODO: maybe it's better to save tag for each card rather than make muscle class
    // TODO : maybe I can use muscle library
    ) : Serializable