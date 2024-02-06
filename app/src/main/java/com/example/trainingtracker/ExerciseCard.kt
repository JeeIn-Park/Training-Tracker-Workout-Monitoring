package com.example.trainingtracker

import java.io.Serializable

data class ExerciseCard(
    val id : Int,
    val name : String,
    val mainMuscles : List<Muscles>,
    val subMuscles : List<Muscles>
    ) : Serializable