package com.example.trainingtracker.ui.muscles

import java.time.LocalDateTime
import java.time.Duration

object MuscleStatus {
    const val RECOVERED = 0
    const val RECOVERING = 1
    const val NEED_EXERCISE = 2

    private const val IN_SECONDS = 0
    private const val IN_MINUTES = 1
    private const val IN_HOURS = 2
    private const val IN_DAYS = 3

    fun muscleState(muscle: Muscle) : Int{
        if (muscle.lastActivity == null) {
            return RECOVERED
        } else if (restingTime(muscle.lastActivity, IN_HOURS) < 50) {
            return RECOVERING
        } else if (10 < restingTime(muscle.lastActivity, IN_DAYS)) {
            return NEED_EXERCISE
        }
        return RECOVERED
    }

    fun getMuscleColour(){}

    fun restingTime(lastActivity: LocalDateTime?, mode: Int) : Long {
        val currentDateTime = LocalDateTime.now()
        val duration = Duration.between(lastActivity, currentDateTime)

//        if (mode == IN_SECONDS) {
//            return duration.seconds
//        } else if (mode == IN_MINUTES) {
//            return  duration.toMinutes()
//        } else if (mode == IN_HOURS) {
//            return duration.toHours()
//        } else if (mode == IN_DAYS) {
//            return duration.toDays()
//        }
//        return -1

        return when (mode) {
            IN_SECONDS -> {
                duration.seconds
            }

            IN_MINUTES -> {
                duration.toMinutes()
            }

            IN_HOURS -> {
                duration.toHours()
            }

            IN_DAYS -> {
                duration.toDays()
            }

            else -> -1
        }
    }

}