package com.jeein.trainingtracker.ui.exerciseLog.exerciseSet

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object SetStorage {
    private const val FILE_NAME = "exercise_sets.dat"

    private fun saveSets(context: Context, sets: List<ExerciseSet>) {
        try {
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(sets)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadSets(context: Context): List<ExerciseSet> {
        try {
            ObjectInputStream(context.openFileInput(FILE_NAME)).use {
                return it.readObject() as? List<ExerciseSet> ?: emptyList()
            }
        } catch (e: FileNotFoundException) {
            return emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun addSet(context: Context, set: ExerciseSet) {
        val currentSets : MutableList<ExerciseSet> = loadSets(context).toMutableList()
        currentSets.add(set)
        saveSets(context, currentSets)
    }

    fun removeSet(context: Context, set: ExerciseSet) {
        val currentSets = loadSets(context).toMutableList()
        currentSets.remove(set)
        saveSets(context, currentSets)
    }

    fun editSet(context: Context, oldSet: ExerciseSet, newSet: ExerciseSet) {
        val currentSets = loadSets(context).toMutableList()
        val index = currentSets.indexOfFirst { it == oldSet }
        if (index != -1) {
            currentSets[index] = newSet
            saveSets(context, currentSets)
        }
    }

//    fun updateSet(context: Context, log: ExerciseLog) {
//        val set = getSet(context, log.exerciseSet)
//        editSet(
//            context,
//            set,
//            ExerciseSetFactory.updateExerciseSet(set, log)
//        )
//        // TODO : muscle factory
//        MuscleFactory.updateMuscle(context, set)
//    }


//    fun isIdInUse(context: Context, id: UUID): Boolean {
//        val currentSets = loadSets(context)
//        return currentSets.any { it.id == id }
//    }


    fun getSets(context: Context): List<ExerciseSet> {
        return loadSets(context)
    }

    fun getCurrentSetNum(context: Context): Int {
        val currentSets = loadSets(context).toMutableList()
        for (i in currentSets.indices.reversed()) {
            val currentSetNum = currentSets[i].set
            if (currentSetNum != null) return currentSetNum
        }
        return 0
    }

}