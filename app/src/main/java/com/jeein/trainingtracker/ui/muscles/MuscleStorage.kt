package com.jeein.trainingtracker.ui.muscles

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

object MuscleStorage {
    private const val FILE_NAME = "muscles.dat"

    private fun saveMuscles(context: Context, muscles: List<Muscle>) {
        try {
            ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use {
                it.writeObject(muscles)
            }
            // TODO : observer pattern
//            EventManager.publish(
//                Event(
//                    context.getString(R.string.event_muscle),
//                    muscles
//                )
//            )
            // TODO : when publish muscle update it should refresh muscle colour
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadMuscles(context: Context): List<Muscle> {

        try {
            // Attempt to open the file and read a List<Muscle> object
            context.openFileInput(FILE_NAME).use { fileInputStream ->
                ObjectInputStream(fileInputStream).use { objectInputStream ->
                    val loadedMuscles = objectInputStream.readObject()

                    if (loadedMuscles is List<*>) {
                        // Successfully casted to List<Muscle>, return it
                        @Suppress("UNCHECKED_CAST")
                        return loadedMuscles as List<Muscle>
                    } else {
                        // Data is not of the type List<*>, handle the type mismatch
                        throw ClassCastException("Data in file '$FILE_NAME' is not of type List<Muscle>")
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            // File not found, handle by loading default muscles
            println("No existing file named '$FILE_NAME'. Loading default muscles.")
            return loadDefaultMuscles()
        } catch (e: ClassCastException) {
            // Handle the case where the data does not match the expected type
            println("Error casting data from file '$FILE_NAME'. Loading default muscles.")
            return loadDefaultMuscles()
        } catch (e: Exception) {
            // General exception handling, could be IOException, etc.
            println("An error occurred while loading muscles from file '$FILE_NAME': ${e.message}")
            e.printStackTrace()
            return loadDefaultMuscles()
        }
    }

    fun editMuscle(context: Context, oldMuscle: Muscle, newMuscle: Muscle) {
        var currentMuscles = loadMuscles(context).toMutableList()
        val index = currentMuscles.indexOfFirst { it.name == oldMuscle.name }
        if (index != -1) {
            currentMuscles[index] = newMuscle
            saveMuscles(context, currentMuscles)
        }
    }

    fun updateMuscles(context: Context, newMuscles: List<Muscle>) {
        saveMuscles(context, newMuscles)
    }


    private fun loadDefaultMuscles(): List<Muscle> {
        var uuidList: MutableList<UUID> = listOf<UUID>().toMutableList()
        return listOf(
            Muscle(
                null,
                0,
                "Neck / Traps",
                listOf("muscle_front_neck_traps", "muscle_back_neck_traps")
            ),
            Muscle(null, 0, "Shoulder", listOf("muscle_front_shoulder", "muscle_back_shoulder")),
            Muscle(null, 0, "Chest", listOf("muscle_front_chest")),
            Muscle(null, 0, "Biceps", listOf("muscle_front_biceps")),
            Muscle(null, 0, "Triceps", listOf("muscle_back_triceps")),
            Muscle(null, 0, "Forearms", listOf("muscle_front_forearm", "muscle_back_forearm")),
            Muscle(null, 0, "Abs", listOf("muscle_front_abs")),
            Muscle(null, 0, "Obliques", listOf("muscle_front_obliques", "muscle_back_obliques")),
            Muscle(null, 0, "Upper Back", listOf("muscle_back_upper_back")),
            Muscle(null, 0, "Lower Back", listOf("muscle_back_lower_back")),
            Muscle(null, 0, "Inner Thigh", listOf("muscle_front_inner_thigh")),
            Muscle(null, 0, "Glutes / Buttocks", listOf("muscle_back_glutes_buttocks")),
            Muscle(null, 0, "Quadriceps", listOf("muscle_front_quadriceps")),
            Muscle(null, 0, "Hamstrings", listOf("muscle_back_hamstrings")),
            Muscle(null, 0, "Calves", listOf("muscle_front_calves", "muscle_back_calves")),
        )

    }
}