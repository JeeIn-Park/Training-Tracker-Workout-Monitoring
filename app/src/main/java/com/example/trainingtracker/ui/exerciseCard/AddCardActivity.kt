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
import com.example.trainingtracker.ui.tag.Tag
import com.example.trainingtracker.ui.tag.TagStorage
import java.time.LocalDateTime
import java.util.UUID


class AddCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard?

        val musclesArray = (listOf<String>(getString(R.string.select_tag))
                + resources.getStringArray(R.array.muscles_array)).toTypedArray()
        val selectTag = Tag(
        id = UUID.randomUUID(),
        timeAdded = LocalDateTime.now(),
        name = getString(R.string.select_tag))
        val tagArray = (listOf(selectTag)
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
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tagArray.map { it.name })

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
            val mainMuscleIndex = findMuscleIndex(cardItem.mainMuscles[0], musclesArray)
            mainMusclesSpinner.setSelection(mainMuscleIndex)
            val subMuscleIndex = findMuscleIndex(cardItem.subMuscles[0], musclesArray)
            subMusclesSpinner.setSelection(subMuscleIndex)
            val tagIndex = findTagIndex(cardItem.tag[0], tagArray)
            tagSpinner.setSelection(tagIndex)
            addButton.text = "SAVE"
        }

        // Set click listener for add/edit button
        addButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscle = listOf(mainMusclesSpinner.selectedItem.toString())
            val subMuscle = listOf(subMusclesSpinner.selectedItem.toString())
            val tag = listOf(tagArray[tagArray.indexOfFirst { it.name == tagSpinner.selectedItem.toString() }])
            val timeAdded = LocalDateTime.now()

            if (cardItem != null) {
                val card = ExerciseCard(
                    id = cardItem.id,
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
                var uniqueId: UUID
                do {
                    uniqueId = UUID.randomUUID()
                } while (CardStorage.isIdInUse(this, uniqueId))

                val card = ExerciseCard(
                    id = uniqueId,
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


    // TODO : make general
    private fun findMuscleIndex(item: String, resourceArray: Array<String>) : Int {
        for (i in resourceArray.indices) {
            if (resourceArray[i] == item) {
                return i
            }
        }
        return 0
    }

    private fun findTagIndex(item: Tag, resourceArray: Array<Tag>) : Int {
        for (i in resourceArray.indices) {
            if (resourceArray[i] == item) {
                return i
            }
        }
        return 0
    }

}

