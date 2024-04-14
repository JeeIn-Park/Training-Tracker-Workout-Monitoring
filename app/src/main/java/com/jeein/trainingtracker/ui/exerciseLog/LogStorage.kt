package com.jeein.trainingtracker.ui.exerciseLog

import android.content.Context
import com.jeein.trainingtracker.Event
import com.jeein.trainingtracker.EventManager
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

class LogStorage(id : UUID) {
    private val fileName = "log_${id}.dat"

    private fun saveLogs(context: Context, logs :List<ExerciseLog>) {
        try {
            ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE)).use {
                it.writeObject(logs)
            }
            EventManager.publish(
                Event(
                    context.getString(R.string.event_log),
                    logs
                )
            )
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

    fun loadLogs(context: Context) : List<ExerciseLog> {
        try {
            ObjectInputStream(context.openFileInput(fileName)).use {
                return it.readObject() as? List<ExerciseLog> ?: emptyList()
            }
        } catch (e : FileNotFoundException) {
            return emptyList()
        } catch (e : IOException) {
            e.printStackTrace()
            return emptyList()
        } catch (e : ClassNotFoundException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun deleteLogs(context: Context) {
        context.deleteFile(fileName)
    }

    fun addLog(context: Context, log: ExerciseLog) {
        val currentLogs = loadLogs(context)
        val updatedLogs = listOf(log) + currentLogs
        saveLogs(context, updatedLogs)
    }

    fun removeLog(context: Context, log: ExerciseLog) {
        val currentLogs = loadLogs(context).toMutableList()
        currentLogs.remove(log)
        saveLogs(context, currentLogs)
    }

    fun editLog(context: Context, oldLog: ExerciseLog, newLog: ExerciseLog) {
        val currentLogs = loadLogs(context).toMutableList()
        val index = currentLogs.indexOfFirst { it == oldLog }
        if (index != -1) {
            currentLogs[index] = newLog
            saveLogs(context, currentLogs)
        }
    }

    fun updateLog(context: Context, log: ExerciseLog, sets: List<ExerciseSet>) : ExerciseLog {
        val currentLogs = loadLogs(context).toMutableList()
        val index = currentLogs.indexOfFirst { it.dateTime == log.dateTime }
        var updatedLog = log
        if (index != -1) {
            updatedLog.exerciseSetList = sets
            updatedLog.oneRepMax = OneRepMax.oneRepMaxRecord(sets)
            currentLogs[index] = updatedLog
            saveLogs(context, currentLogs)
            CardStorage.updateCard(context, log)
        }
        return updatedLog
    }

}