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
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.muscles.MuscleStorage
import com.example.trainingtracker.ui.tag.Tag
import com.example.trainingtracker.ui.tag.TagStorage
import java.time.LocalDateTime
import java.util.UUID


class AddCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard?
        val selectMuscle = Muscle(null, 0, getString(R.string.muscle_select), listOf())
        val musclesArray = (listOf(selectMuscle)
                + MuscleStorage.loadMuscles(this).toTypedArray())
        val selectTag = Tag( UUID.randomUUID(), LocalDateTime.now(), getString(R.string.tag_select))
        val tagArray = (listOf(selectTag)
                + TagStorage.loadTags(this)).toTypedArray()

        // Initialize views
        val exerciseNameEditText: EditText = findViewById(R.id.exercise_name)
        val mainMusclesSpinner: Spinner = findViewById(R.id.main_muscles)
        val subMusclesSpinner: Spinner = findViewById(R.id.sub_muscles)
        val tagSpinner: Spinner = findViewById(R.id.select_tag)
        val addButton: Button = findViewById(R.id.add_button)

        val mainMusclesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, musclesArray.map { it.name })
        val subMusclesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, musclesArray.map { it.name })
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
            val mainMuscleIndex = musclesArray.indexOfFirst { it == cardItem.mainMuscles[0] }
            mainMusclesSpinner.setSelection(mainMuscleIndex)
            val subMuscleIndex = musclesArray.indexOfFirst { it == cardItem.subMuscles[0] }
            subMusclesSpinner.setSelection(subMuscleIndex)
            val tagIndex = tagArray.indexOfFirst { it == cardItem.tag[0] }
            tagSpinner.setSelection(tagIndex)
            addButton.text = "SAVE"
        }

        // Set click listener for add/edit button
        addButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscle = listOf( musclesArray[musclesArray.indexOfFirst { it.name == mainMusclesSpinner.selectedItem.toString().trim() }] )
            val subMuscle = listOf( musclesArray[musclesArray.indexOfFirst { it.name == subMusclesSpinner.selectedItem.toString().trim() }] )
            val singleTag = tagArray[tagArray.indexOfFirst { it.name == tagSpinner.selectedItem.toString().trim() }]
            val tag = listOf(Tag(
                id = singleTag.id,
                timeAdded = singleTag.timeAdded,
                name = singleTag.name,
                isSelected = true
            ))
            // TODO : deal with multiple tags
            // TODO : if there is card stored with false tag, needs to change it to true.
            // TODO : data migrain when start app, need to update all the data as well
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
}

