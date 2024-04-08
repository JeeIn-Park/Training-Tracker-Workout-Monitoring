package com.example.trainingtracker

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object AppDataStorage {
    private const val FILE_NAME = "app_data_.dat"

    fun saveData(context: Context, data: Map<String, Any?>) {
        try {
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(data)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadData(context: Context) : Map<String, Any?> {
        try {
            ObjectInputStream(context.openFileInput(FILE_NAME)).use {
                return it.readObject() as? Map<String, Any?> ?: emptyMap()
            }
        } catch (e : FileNotFoundException) {
            return emptyMap()
        } catch (e : IOException) {
            e.printStackTrace()
            return emptyMap()
        } catch (e : ClassNotFoundException) {
            e.printStackTrace()
            return emptyMap()
        }
    }

    fun addData(context: Context, data: Map<String, Any?>) {
        val currentData = loadData(context).toMutableMap()
        currentData.plus(data)
        saveData(context, currentData)
    }

    fun removeData(context: Context, key: String){
        val currentData = loadData(context).toMutableMap()
        currentData.remove(key)
        saveData(context, currentData)
    }

    fun editData(context: Context, key: String, value: Any?){
        val currentData = loadData(context).toMutableMap()
        currentData.replace(key, value)
        saveData(context, currentData)
    }

}











