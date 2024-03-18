package com.example.trainingtracker.ui.exerciseCard

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.trainingtracker.R
import java.time.LocalDateTime

class AddCardActivity : AppCompatActivity() {

    private var isEditMode = false
    private lateinit var cardToEdit: ExerciseCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        // Retrieve the card to edit if this activity was launched for editing
        val cardFromIntent = intent.getSerializableExtra("edit_card") as? ExerciseCard
        Log.d("com.example.trainingtracker.ui.exerciseCard.AddCardActivity", "Card from Intent: $cardFromIntent")

        // Check if the activity is in edit mode
        isEditMode = intent.getBooleanExtra("is_edit_mode", false)

        // Initialize views
        val musclesArray = resources.getStringArray(R.array.muscles_array)
        val exerciseNameEditText: EditText = findViewById(R.id.exercise_name)
        val mainMusclesSpinner: Spinner = findViewById(R.id.main_muscles)
        val subMusclesSpinner: Spinner = findViewById(R.id.sub_muscles)
        val addButton: Button = findViewById(R.id.add_button)

        val mainMusclesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, musclesArray)
        val subMusclesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, musclesArray)

        // Set dropdown layout style
        mainMusclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subMusclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapters to spinners
        mainMusclesSpinner.adapter = mainMusclesAdapter
        subMusclesSpinner.adapter = subMusclesAdapter

        // Populate views with data of the card being edited?
        if (isEditMode) {
            exerciseNameEditText.setText(cardToEdit.name)
            // Set selection for spinners
            // (You need to implement this based on your logic)
        }

        // Set click listener for add/edit button
        addButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscle = listOf(mainMusclesSpinner.selectedItem.toString())
            val subMuscle = listOf(subMusclesSpinner.selectedItem.toString())
            val lastActivity = null
            val timeAdded = LocalDateTime.now()
            // TODO: Replace with actual values
            val tag = null

            val card = ExerciseCard(
                lastActivity = lastActivity,
                timeAdded = timeAdded,
                name = exerciseName,
                mainMuscles = mainMuscle,
                subMuscles = subMuscle,
                tag = tag
            )

            if (isEditMode) {
                // Replace the edited card in storage
                CardStorage.editCard(this, cardToEdit, card)
            } else {
                // Add the new card to storage
                CardStorage.addCard(this, card)
            }

            finish()
        }
    }
}
