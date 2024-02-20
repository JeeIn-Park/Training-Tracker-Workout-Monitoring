package com.example.trainingtracker.ui.Status

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.trainingtracker.CardStorage
import com.example.trainingtracker.ExerciseCard
import com.example.trainingtracker.R
import java.time.LocalDateTime

class AddCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //layout
        setContentView(R.layout.activity_add_card)

        val exerciseNameEditText: EditText = findViewById(R.id.exercise_name)

        val mainMusclesSpinner: Spinner = findViewById(R.id.main_muscles)
        ArrayAdapter.createFromResource(
            this,
            R.array.muscles_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mainMusclesSpinner.adapter = adapter
        }

        val subMusclesSpinner: Spinner = findViewById(R.id.sub_muscles)
        ArrayAdapter.createFromResource(
            this,
            R.array.muscles_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            subMusclesSpinner.adapter = adapter
        }


        val addButton: Button = findViewById(R.id.add_button)


        addButton.setOnClickListener {

            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscle = listOf(mainMusclesSpinner.selectedItem.toString())
            val subMuscle = listOf(subMusclesSpinner.selectedItem.toString())
            val lastActivity = LocalDateTime.now()
            val timeAdded = LocalDateTime.now()
            // TODO: Replace with actual values
            val tag = null

            val card = ExerciseCard(lastActivity = lastActivity, timeAdded = timeAdded, name = exerciseName, mainMuscles = mainMuscle, subMuscles = subMuscle, tag = tag) // Create a new card
            CardStorage.addCard(this, card)
            finish()

        }


    }
}
