package com.example.trainingtracker

import android.content.Context
import com.example.trainingtracker.ui.exerciseLog.LogStorage
import java.util.UUID

class Algorithm(context: Context) {

    fun personalOneRepMaxRecord(context: Context, id : UUID) : Float {
        val logStorage = LogStorage(id)
        val logs = logStorage.loadLogs(context)
        var finalOneRepMax : Float = 0F
        logs.forEach{
            if (id == it.exerciseCard) {
                val sets = it.exerciseSetList
                sets.forEach{
                    val mass = it.mass
                    val rep = it.rep
                    if ( (mass != null) && (rep != null)){
                        val tempOneRepMax : Float = oneRepMax(mass, rep)
                        finalOneRepMax = maxOf(finalOneRepMax, tempOneRepMax)
                    }
                }
            }
        }
        return finalOneRepMax
    }


    fun oneRepMax(mass: Float, reps: Int): Float{
           return ((mass * (36f / (37 - reps))) +
                    (mass * (1f + (0.0333f * reps))) +
                    (mass * Math.pow(reps.toDouble(), 0.1).toFloat()) +
                    (mass * (1f + (0.025f * reps)))) / 4
    }
}