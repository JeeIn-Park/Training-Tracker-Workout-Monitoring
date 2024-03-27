package com.example.trainingtracker.ui.tag

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object TagStorage {

    private const val FILE_NAME = "tags.dat"

    fun saveTags(context: Context, tags : List<String>) {
        try {
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(tags)
            }
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

    // TODO : when not match the type
    fun loadTags(context: Context) : List<String> {
        try {
            ObjectInputStream(context.openFileInput(FILE_NAME)).use {
                return it.readObject() as? List<String> ?: emptyList()
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

    fun addTag(context: Context, tag: String) {
        val currentTags = loadTags(context).toMutableList()
        currentTags.add(tag)
        saveTags(context, currentTags)
    }

    fun removeTag(context: Context, tag: String) {
        val currentTags = loadTags(context).toMutableList()
        currentTags.remove(tag)
        saveTags(context, currentTags)
    }

    fun editTag(context: Context, oldTag: String, newTag: String) {
        val currentTags = loadTags(context).toMutableList()
        val index = currentTags.indexOfFirst { it == oldTag } // TODO : what is this code
        if (index != -1) {
            currentTags[index] = newTag
            saveTags(context, currentTags)
        }
    }


}