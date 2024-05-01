package com.jeein.trainingtracker.ui.exerciseSet

import android.content.Context
import com.jeein.trainingtracker.Event
import com.jeein.trainingtracker.EventManager
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.exerciseLog.ExerciseLogFactory
import com.jeein.trainingtracker.ui.exerciseLog.LogStorage
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDateTime
import java.util.UUID

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
                    EventManager.publish(
                Event(
                    context.getString(R.string.event_add_set),
                    currentSets
                )
            )
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


    fun resetSets(context: Context, currentExercise: UUID) {
        val logStorage = LogStorage(currentExercise)
        val log = ExerciseLogFactory.createExerciseLog(loadSets(context))
        logStorage.addLog(context, log)
        CardStorage.updateCard(context, log)
        saveSets(context, listOf())
    }


    fun getSets(context: Context): List<ExerciseSet> {
        return loadSets(context)
    }

    fun getCurrentExercise(context: Context) : UUID? {
        val currentSets = loadSets(context)
        return if (currentSets.isEmpty()) {
            null
        } else currentSets[0].exerciseCard
    }

    fun getLastDateTime(context: Context) : LocalDateTime? {
        val currentSets = loadSets(context)
        return if (currentSets.isEmpty()) {
            null
        } else currentSets[currentSets.size -1].dateTime
    }

    fun getCurrentSetNum(context: Context): Int {
        val currentSets = loadSets(context)
        if (currentSets.isEmpty()) {
            return 0
        } else {
            for (i in currentSets.indices.reversed()) {
                val currentSetNum = currentSets[i].set
                if (currentSetNum != null) return currentSetNum
            }
        }
        return 0
    }

}