package com.example.trainingtracker

import android.content.Context
import com.example.trainingtracker.ui.exerciseLog.LogStorage
import java.util.UUID

class Algorithm(context: Context) {

    fun totalMass(){

    }

    fun personalOneRepMaxRecord(context: Context, id : UUID) {
        val logs = LogStorage.loadLogs(context)



    }


    fun brzyckiFormula(weight: Float, reps: Int): Float {
        return weight * (36f / (37 - reps))
    }

    fun epleyFormula(weight: Float, reps: Int): Float {
        return weight * (1f + (0.0333f * reps))
    }

    fun lombardiFormula(weight: Float, reps: Int): Float {
        return weight * Math.pow(reps.toDouble(), 0.1).toFloat()
    }

    fun oConnerFormula(weight: Float, reps: Int): Float {
        return weight * (1f + (0.025f * reps))
    }

    fun oneRepMax(weight: Float, reps: Int): Float{
        val brzycki =  brzyckiFormula(weight, reps)
        val epley = epleyFormula(weight, reps)
        val lombardi =  lombardiFormula(weight, reps)
        val oConner = oConnerFormula(weight, reps)

        return (brzycki + epley + lombardi + oConner)/4
    }

}