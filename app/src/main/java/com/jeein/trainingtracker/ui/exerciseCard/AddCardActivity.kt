package com.jeein.trainingtracker.ui.exerciseCard

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.ui.muscles.Muscle
import com.jeein.trainingtracker.ui.muscles.MuscleStorage
import com.jeein.trainingtracker.ui.tag.Tag
import com.jeein.trainingtracker.ui.tag.TagStorage
import com.jeein.trainingtracker.views.MultiSelectionSpinner
import java.util.UUID


class AddCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard?
        val selectMuscle = Muscle(null, 0, getString(R.string.muscle_select), listOf())
        val muscleList = (listOf(selectMuscle)
                + MuscleStorage.loadMuscles(this))
        val tagList = (listOf(Tag.SELECT_TAG)
                + TagStorage.loadTags(this))

        // Initialize views
        val exerciseNameEditText: EditText = findViewById(R.id.exercise_name)
        val mainMusclesSpinner: MultiSelectionSpinner = findViewById(R.id.main_muscles)
        val subMusclesSpinner: MultiSelectionSpinner = findViewById(R.id.sub_muscles)
        val tagSpinner: MultiSelectionSpinner = findViewById(R.id.select_tag)
        val addButton: Button = findViewById(R.id.add_button)

        mainMusclesSpinner.setItems(muscleList.map { it.name })
        subMusclesSpinner.setItems(muscleList.map { it.name })
        tagSpinner.setItems(tagList.map { it.name })

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
            mainMusclesSpinner.setSelection(cardItem.mainMuscles.map { it.name })
            subMusclesSpinner.setSelection(cardItem.subMuscles.map { it.name })
            tagSpinner.setSelection(cardItem.tag.map { it.name })
            addButton.text = "SAVE"
        }

        // Set click listener for add/edit button
        addButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscles = getSelectedMuscles(mainMusclesSpinner.getSelectedIndices(), muscleList)
            val subMuscles = getSelectedMuscles(subMusclesSpinner.getSelectedIndices(), muscleList)
            val tags = getSelectedTags(tagSpinner.getSelectedIndices(), tagList)

            // TODO : if there is card stored with false tag, needs to change it to true.
            // TODO : data migrain when start app, need to update all the data as well

            // TODO : edit card to select multiple muscles and tags
            if (cardItem != null) {
                CardStorage.editCard(this, cardItem, ExerciseCardFactory.editExerciseCard(cardItem, exerciseName, mainMuscles, subMuscles, tags))
                finish()

            } else {
                var uniqueId: UUID
                do {
                    uniqueId = UUID.randomUUID()
                } while (CardStorage.isIdInUse(this, uniqueId))

                val card = ExerciseCardFactory.createExerciseCard(this, exerciseName)
                card.mainMuscles = mainMuscles
                card.subMuscles = subMuscles
                card.tag = tags
                CardStorage.addCard(this, card)
                finish()
            }
        }

    }

    private fun getSelectedMuscles(indices : List<Int>, muscleArray: List<Muscle>) : List<Muscle> {
        val muscles = emptyList<Muscle>().toMutableList()
        for (index in indices) {
            if (index != 0) {
                muscles.add(muscleArray[index])
            }
        }
        return muscles
    }

    private fun getSelectedTags(indices : List<Int>, tagArray: List<Tag>) : List<Tag> {
        val tags = emptyList<Tag>().toMutableList()
        for (index in indices) {
            if (index != 0) {
                val singleTag = tagArray[index]
                tags.add(Tag(
                    id = singleTag.id,
                    timeAdded = singleTag.timeAdded,
                    name = singleTag.name,
                    isSelected = true
                ))
            }
        }
        return tags
    }


    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
    // TODO : back press warning, on stop
}

