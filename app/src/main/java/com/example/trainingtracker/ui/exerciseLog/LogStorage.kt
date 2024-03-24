package com.example.trainingtracker.ui.exerciseLog

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

class LogStorage(id : UUID) {
    private val fileName = "log_${id}.dat"

    // TODO: make storage for muscles and list

    fun saveLogs(context: Context, logs :List<ExerciseLog>) {
        try {
            ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE)).use {
                it.writeObject(logs)
            }
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
        val currentLogs = loadLogs(context).toMutableList()
        currentLogs.add(log)
        println(currentLogs)
        saveLogs(context, currentLogs)
    }

    fun removeLog(context: Context, log: ExerciseLog) {
        val currentLogs = loadLogs(context).toMutableList()
        currentLogs.remove(log)
        saveLogs(context, currentLogs)
    }
}