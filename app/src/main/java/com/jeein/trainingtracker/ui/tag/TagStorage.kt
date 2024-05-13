package com.jeein.trainingtracker.ui.tag

import android.content.Context
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

object TagStorage {

    private const val FILE_NAME = "tags.dat"

    fun saveTags(context: Context, tags: List<Tag>) {
        try {
            val filteredTags = tags.filter { it != Tag.ADD_TAG }
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(filteredTags)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // TODO : when not match the type
    fun loadTags(context: Context): List<Tag> {
        try {
            ObjectInputStream(context.openFileInput(FILE_NAME)).use {
                return it.readObject() as? List<Tag> ?: emptyList()
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

//    fun loadTagBarTags(context: Context): List<Tag> {
//
//    }

    fun addTag(context: Context, tag: Tag) {
        val currentTags = loadTags(context).toMutableList()
        currentTags.add(tag)
        saveTags(context, currentTags)
    }

    fun removeTag(context: Context, tag: Tag) {
        val currentTags = loadTags(context).toMutableList()
        currentTags.remove(tag)
        saveTags(context, currentTags)
        CardStorage.deleteTag(context, tag)
    }


    fun editTag(context: Context, oldTag: Tag, newTag: Tag) {
        val currentTags = loadTags(context).toMutableList()
        val index = currentTags.indexOfFirst { it == oldTag }
        if (index != -1) {
            currentTags[index] = newTag
            saveTags(context, currentTags)
        }
        CardStorage.editTag(context, oldTag, newTag)
    }

    fun isIdInUse(context: Context, id: UUID): Boolean {
        val currentTags = loadTags(context)
        return currentTags.any { it.id == id }
    }

    fun getSelectedTags(context: Context): List<Tag> {
        val tags = loadTags(context)
        val selectedTags: MutableList<Tag> = mutableListOf()
        for (tag in tags) {
            if (tag.isSelected) {
                selectedTags.add(tag)
            }
        }
        return selectedTags
    }

}