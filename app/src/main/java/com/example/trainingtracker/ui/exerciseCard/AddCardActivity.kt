package com.example.trainingtracker.ui.exerciseCard

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard?

        val musclesArray = resources.getStringArray(R.array.muscles_array)
        val tagArray = (listOf(resources.getString(R.string.select_tag))
                + TagStorage.loadTags(this)).toTypedArray()

        // Initialize views
        val exerciseNameEditText: EditText = findViewById(R.id.exercise_name)
        val mainMusclesSpinner: Spinner = findViewById(R.id.main_muscles)
        val subMusclesSpinner: Spinner = findViewById(R.id.sub_muscles)
        val tagSpinner: Spinner = findViewById(R.id.select_tag)
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
        tagSpinner.adapter = tagAdapter

        // Set key listener for exercise name EditText
        exerciseNameEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Hide the keyboard
                hideKeyboard()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        if (cardItem != null) {
            supportActionBar?.title = cardItem.name
            exerciseNameEditText.setText(cardItem.name)
            val mainMuscleIndex = find(cardItem.mainMuscles[0], musclesArray)
            mainMusclesSpinner.setSelection(mainMuscleIndex)
            val subMuscleIndex = find(cardItem.subMuscles[0], musclesArray)
            subMusclesSpinner.setSelection(subMuscleIndex)
            val tagIndex = find(cardItem.tag[0], tagArray)
            tagSpinner.setSelection(tagIndex)
            addButton.text = "SAVE"
        }

        // Set click listener for add/edit button
        addButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscle = listOf(mainMusclesSpinner.selectedItem.toString())
            val subMuscle = listOf(subMusclesSpinner.selectedItem.toString())
            val tag = listOf(tagSpinner.selectedItem.toString())
            val timeAdded = LocalDateTime.now()

            if (cardItem != null) {
                val card = ExerciseCard(
                    lastActivity = cardItem.lastActivity,
                    timeAdded = timeAdded,
                    name = exerciseName,
                    mainMuscles = mainMuscle,
                    subMuscles = subMuscle,
                    tag = tag,
                    oneRepMax = cardItem.oneRepMax
                )

                CardStorage.editCard(this, cardItem, card)
                finish()

            } else {
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

    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    // TODO : back press warning, on stop

    private fun find(item: String, resourceArray: Array<String>) : Int {
        for (i in resourceArray.indices) {
            if (resourceArray[i] == item) {
                return i
            }
        }
        return 0
    }
}

