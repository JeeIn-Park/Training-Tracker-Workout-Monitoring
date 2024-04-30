package com.jeein.trainingtracker.ui.exerciseSet

import com.jeein.trainingtracker.ui.exerciseLog.ExerciseLog

object OneRepMax {


    fun oneRepMaxRecord_pb(logs: List<ExerciseLog>): Float {
        var finalOneRepMax: Float = 0F
        logs.forEach {
            val sets = it.exerciseSetList
            sets.forEach {
                val mass = it.mass
                val rep = it.rep
                if ((mass != null) && (rep != null)) {
                    val tempOneRepMax: Float = oneRepMax(mass, rep)
                    finalOneRepMax = maxOf(finalOneRepMax, tempOneRepMax)
                }
            }
        }
        return finalOneRepMax
    }


    fun oneRepMaxRecord(log: ExerciseLog): Float {
        var finalOneRepMax: Float = 0F
        val sets = log.exerciseSetList
        sets.forEach {
            val mass = it.mass
            val rep = it.rep
            if ((mass != null) && (rep != null)) {
                val tempOneRepMax: Float = oneRepMax(mass, rep)
                finalOneRepMax = maxOf(finalOneRepMax, tempOneRepMax)
            }
        }
        return finalOneRepMax
    }

    fun oneRepMaxRecord(sets: List<ExerciseSet>): Float {
        var finalOneRepMax: Float = 0F
        sets.forEach {
            val mass = it.mass
            val rep = it.rep
            if ((mass != null) && (rep != null)) {
                val tempOneRepMax: Float = oneRepMax(mass, rep)
                finalOneRepMax = maxOf(finalOneRepMax, tempOneRepMax)
            }
        }
        return finalOneRepMax
    }

    fun oneRepMax(mass: Float?, reps: Int?): Float {
        return if ((mass == null) || (reps == null)) {
            0F
        } else ((mass * (36f / (37 - reps))) +
                (mass * (1f + (0.0333f * reps))) +
                (mass * Math.pow(reps.toDouble(), 0.1).toFloat()) +
                (mass * (1f + (0.025f * reps)))) / 4
    }

    fun oneRepMaxComplex(totalMass: Float?, totalReps: Int?): Float{
        return if ((totalMass == null) || (totalReps == null)) {
            0F
        } else {
            val averageMass = totalMass / totalReps
            (((averageMass * (36.0 / (37 - totalReps))) +
                    (averageMass * (1 + 0.0333 * totalReps)) +
                    (averageMass * Math.pow(totalReps.toDouble(), 0.1)) +
                    (averageMass * (1 + 0.025 * totalReps))) / 4).toFloat()
        }
    }


}