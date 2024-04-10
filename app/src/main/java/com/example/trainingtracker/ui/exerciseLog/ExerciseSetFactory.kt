package com.example.trainingtracker.ui.exerciseLog

import com.example.trainingtracker.ui.exerciseCard.ExerciseCard

object ExerciseSetFactory {

    fun createExerciseSet(
        card: ExerciseCard, mass: Float?, set: Int?, rep: Int?)
    : ExerciseSet{
        return ExerciseSet(
            exerciseCard = card.id, mass = mass, set = set, rep = rep, oneRepMax = OneRepMax.oneRepMax(mass, rep)
        )
    }

}