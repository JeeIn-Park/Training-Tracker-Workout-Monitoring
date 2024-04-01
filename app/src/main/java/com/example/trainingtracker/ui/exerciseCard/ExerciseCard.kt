package com.example.trainingtracker.ui.exerciseCard

import com.example.trainingtracker.ui.tag.Tag
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class ExerciseCard(
    val id : UUID,
    val lastActivity: LocalDateTime?,
    val timeAdded : LocalDateTime,
    val name : String,
    val mainMuscles : List<String>,
    val subMuscles : List<String>,
    val tag : List<Tag>,
    val oneRepMax : Float?
    // TODO: maybe it's better to save tag for each card rather than make muscle class
    // TODO : maybe I can use muscle library
) : Serializable