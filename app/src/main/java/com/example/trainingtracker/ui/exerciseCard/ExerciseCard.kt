package com.example.trainingtracker.ui.exerciseCard

import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.tag.Tag
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class ExerciseCard(
    val id : UUID,
    val lastActivity: LocalDateTime?,
    val timeAdded : LocalDateTime,
    val name : String,
    val mainMuscles : List<Muscle>,
    val subMuscles : List<Muscle>,
    val tag : List<Tag>,
    val oneRepMax : Float?
    // TODO: maybe it's better to save tag for each card rather than make muscle class
    // TODO : maybe I can use muscle library
) : Serializable