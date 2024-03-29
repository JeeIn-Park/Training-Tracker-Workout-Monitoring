package com.example.trainingtracker.ui.exerciseCard

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.trainingtracker.R
import com.example.trainingtracker.ui.tag.TagStorage
import java.time.LocalDateTime


class AddCardActivity : AppCompatActivity() {

    private lateinit var cardToEdit: ExerciseCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        // Retrieve the card to edit if this activity was launched for editing
        val cardFromIntent = intent.getSerializableExtra("edit_card") as? ExerciseCard
        Log.d("com.example.trainingtracker.ui.exerciseCard.AddCardActivity", "Card from Intent: $cardFromIntent")

        val musclesArray = resources.getStringArray(R.array.muscles_array)
        val tagArray = (listOf(resources.getString(R.string.select_tag))
                + TagStorage.loadTags(this)).toTypedArray()

        // Initialize views
        val exerciseNameEditText: EditText = findViewById(R.id.exercise_name)
        val mainMusclesSpinner: Spinner = findViewById(R.id.main_muscles)
        val subMusclesSpinner: Spinner = findViewById(R.id.sub_muscles)
        val tagMuscleSpinner: Spinner = findViewById(R.id.select_tag)
        val addButton: Button = findViewById(R.id.add_button)

        val mainMusclesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, musclesArray)
        val subMusclesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, musclesArray)
        val tagAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tagArray)

        // Set dropdown layout style
        mainMusclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subMusclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapters to spinners
        mainMusclesSpinner.adapter = mainMusclesAdapter
        subMusclesSpinner.adapter = subMusclesAdapter
        tagMuscleSpinner.adapter = tagAdapter

        // Set key listener for exercise name EditText
        exerciseNameEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Hide the keyboard
                hideKeyboard()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        // Set click listener for add/edit button
        addButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscle = listOf(mainMusclesSpinner.selectedItem.toString())
            val subMuscle = listOf(subMusclesSpinner.selectedItem.toString())
            val timeAdded = LocalDateTime.now()
            // TODO: Replace with actual values
            val tag = null

            val card = ExerciseCard(
                lastActivity = null,
                timeAdded = timeAdded,
                name = exerciseName,
                mainMuscles = mainMuscle,
                subMuscles = subMuscle,
                tag = tag,
                oneRepMax = null
            )

            CardStorage.addCard(this, card)
            finish()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

