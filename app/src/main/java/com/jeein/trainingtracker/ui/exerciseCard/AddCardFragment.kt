package com.jeein.trainingtracker.ui.exerciseCard

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.databinding.FragmentAddCardBinding
import com.jeein.trainingtracker.ui.muscles.Muscle
import com.jeein.trainingtracker.ui.muscles.MuscleStorage
import com.jeein.trainingtracker.ui.tag.Tag
import com.jeein.trainingtracker.ui.tag.TagStorage
import com.jeein.trainingtracker.views.MultiSelectionSpinner
import java.util.UUID


class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)

        val exerciseNameEditText: EditText = binding.exerciseName
        val mainMusclesSpinner: MultiSelectionSpinner = binding.mainMuscles
        val subMusclesSpinner: MultiSelectionSpinner = binding.subMuscles
        val tagSpinner: MultiSelectionSpinner = binding.selectTag
        val addButton: Button = binding.addButton

        val cardItem = arguments?.getSerializable("exerciseCardArg") as? ExerciseCard
        val selectMuscle = Muscle(null, 0, getString(R.string.muscle_select), listOf())
        val muscleList = (listOf(selectMuscle)
                + MuscleStorage.loadMuscles(requireContext()))
        val tagList = (listOf(Tag.SELECT_TAG)
                + TagStorage.loadTags(requireContext()))

        mainMusclesSpinner.setItems(muscleList.map { it.name })
        subMusclesSpinner.setItems(muscleList.map { it.name })
        tagSpinner.setItems(tagList.map { it.name })

        // Set key listener for exercise name EditText
        exerciseNameEditText.setOnKeyListener { v, keyCode, event ->
            event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
        }

        if (cardItem != null) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = cardItem.name
            exerciseNameEditText.setText(cardItem.name)
            mainMusclesSpinner.setSelection(cardItem.mainMuscles.map { it.name })
            if (mainMusclesSpinner.getSelectedIndices().isEmpty()) {
                mainMusclesSpinner.setItems(muscleList.map { it.name })
            }
            subMusclesSpinner.setSelection(cardItem.subMuscles.map { it.name })
            if (subMusclesSpinner.getSelectedIndices().isEmpty()) {
                subMusclesSpinner.setItems(muscleList.map { it.name })
            }
            tagSpinner.setSelection(cardItem.tag.map { it.name })
            if (tagSpinner.getSelectedIndices().isEmpty()) {
                tagSpinner.setItems(tagList.map { it.name })
            }
            addButton.text = "SAVE"
        }

        addButton.setOnClickListener {
            val exerciseName = exerciseNameEditText.text.toString()
            val mainMuscles =
                getSelectedMuscles(mainMusclesSpinner.getSelectedIndices(), muscleList)
            val subMuscles = getSelectedMuscles(subMusclesSpinner.getSelectedIndices(), muscleList)
            val tags = getSelectedTags(tagSpinner.getSelectedIndices(), tagList)

            // TODO : if there is card stored with false tag, needs to change it to true.
            // TODO : data migrain when start app, need to update all the data as well

            // TODO : edit card to select multiple muscles and tags

            if (cardItem != null) {
                CardStorage.editCard(
                    requireContext(),
                    cardItem,
                    ExerciseCardFactory.editExerciseCard(
                        cardItem,
                        exerciseName,
                        mainMuscles,
                        subMuscles,
                        tags
                    )
                )
                findNavController().navigateUp()

            } else {
                var uniqueId: UUID
                do {
                    uniqueId = UUID.randomUUID()
                } while (CardStorage.isIdInUse(requireContext(), uniqueId))

                val card = ExerciseCardFactory.createExerciseCard(requireContext(), exerciseName)
                card.mainMuscles = mainMuscles
                card.subMuscles = subMuscles
                card.tag = tags
                CardStorage.addCard(requireContext(), card)
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

private fun getSelectedMuscles(indices: List<Int>, muscleArray: List<Muscle>): List<Muscle> {
    val muscles = emptyList<Muscle>().toMutableList()
    for (index in indices) {
        if (index != 0) {
            muscles.add(muscleArray[index])
        }
    }
    return muscles
}

private fun getSelectedTags(indices: List<Int>, tagArray: List<Tag>): List<Tag> {
    val tags = emptyList<Tag>().toMutableList()
    for (index in indices) {
        if (index != 0) {
            val singleTag = tagArray[index]
            tags.add(
                Tag(
                    id = singleTag.id,
                    timeAdded = singleTag.timeAdded,
                    name = singleTag.name,
                    isSelected = true
                )
            )
        }
    }
    return tags
}


