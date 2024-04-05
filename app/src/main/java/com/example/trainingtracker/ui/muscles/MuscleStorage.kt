package com.example.trainingtracker.ui.muscles

import android.content.Context
import com.example.trainingtracker.Event
import com.example.trainingtracker.EventManager
import com.example.trainingtracker.R
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

object MuscleStorage {
    private const val FILE_NAME = "muscles.dat"

    fun saveMuscles(context: Context, muscles: List<Muscle>) {
        try {
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(muscles)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // TODO : when not match the type, file not found exception
    fun loadMuscles(context: Context): List<Muscle> {
        try {
            ObjectInputStream(context.openFileInput(FILE_NAME)).use { inputStream ->
                val loadedMuscles = inputStream.readObject()
                if (loadedMuscles is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    return loadedMuscles as List<Muscle>
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return loadDefaultMuscles()
    }

    fun updateMuscle(context: Context, oldMuscle: Muscle, newMuscle: Muscle) {
        val currentMuscles = loadMuscles(context).toMutableList()
        val index = currentMuscles.indexOfFirst { it == oldMuscle }
        if (index != -1) {
            currentMuscles[index] = newMuscle
            saveMuscles(context, currentMuscles)
            EventManager.publish(
                Event(
                    context.getString(R.string.event_muscle),
                    loadMuscles(context)
                )
            )
        }
    }

//    fun getSelectedTags(context: Context): List<Tag> {
//        val tags = TagStorage.loadTags(context)
//        val selectedTags: MutableList<Tag> = mutableListOf()
//        for (tag in tags) {
//            if (tag.isSelected) {
//                selectedTags.add(tag)
//            }
//        }
//        return selectedTags
//    }

    private fun loadDefaultMuscles(): List<Muscle> {
        var uuidList : MutableList<UUID> = listOf<UUID>().toMutableList()
        return listOf(
            Muscle(null, 0, "Neck / Traps", listOf("muscle_front_neck_traps", "muscle_back_neck_traps")),
            Muscle(null, 0, "Shoulder", listOf("muscle_front_shoulder", "muscle_back_shoulder")),
            Muscle(null, 0, "Chest", listOf("muscle_front_chest")),
            Muscle(null, 0, "Arm", listOf("muscle_front_biceps", "muscle_back_triceps", "muscle_front_forearm", "muscle_back_forearm")),
            Muscle(null, 0, "Biceps", listOf("muscle_front_biceps")),
            Muscle(null, 0, "Triceps", listOf("muscle_back_triceps")),
            Muscle(null, 0, "Forearms", listOf("muscle_front_forearm", "muscle_back_forearm")),
            Muscle(null, 0, "Abs", listOf("muscle_front_abs")),
            Muscle(null, 0, "Obliques", listOf("muscle_front_obliques", "muscle_back_obliques")),
            Muscle(null, 0, "Back", listOf("muscle_back_upper_back", "muscle_back_lower_back")),
            Muscle(null, 0, "Upper Back", listOf("muscle_back_upper_back")),
            Muscle(null, 0, "Lower Back", listOf("muscle_back_lower_back")),
            Muscle(null, 0, "Leg", listOf("muscle_front_inner_thigh", "muscle_front_quadriceps", "muscle_back_hamstrings", "muscle_front_calves", "muscle_back_calves")),
            Muscle(null, 0, "Inner Thigh", listOf("muscle_front_inner_thigh")),
            Muscle(null, 0, "Glutes / Buttocks", listOf("muscle_back_glutes_buttocks")),
            Muscle(null, 0, "Quadriceps", listOf("muscle_front_quadriceps")),
            Muscle(null, 0, "Hamstrings", listOf("muscle_back_hamstrings")),
            Muscle(null, 0, "Calves", listOf("muscle_front_calves", "muscle_back_calves")),
        )

    }
}