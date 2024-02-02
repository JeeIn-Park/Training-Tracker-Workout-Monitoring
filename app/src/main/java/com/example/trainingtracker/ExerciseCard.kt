package com.example.trainingtracker

import java.io.Serializable

data class ExerciseCard(
    val id : Int,
    val name : String,
    val mainMuscles : List<String>,
    val subMuscles : List<String>
    // TODO : will muscles really be string? or should I add class for muscles?
    // if it has class, it might have tag (such as, pull push shoulder... or other names)
    ) : Serializable