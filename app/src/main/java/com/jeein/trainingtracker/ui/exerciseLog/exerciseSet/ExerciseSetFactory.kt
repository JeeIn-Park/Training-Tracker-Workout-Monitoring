package com.jeein.trainingtracker.ui.exerciseLog.exerciseSet

import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.exerciseLog.OneRepMax

object ExerciseSetFactory {

    fun createExerciseSet(
        card: ExerciseCard, mass: Float?, set: Int?, rep: Int?
    )
            : ExerciseSet {
        return ExerciseSet(
            exerciseCard = card.id,
            mass = mass,
            set = set,
            rep = rep,
            oneRepMax = OneRepMax.oneRepMax(mass, rep)
        )
    }

}