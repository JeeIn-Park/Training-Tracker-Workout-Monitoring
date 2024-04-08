package com.example.trainingtracker.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.R
import com.example.trainingtracker.databinding.FragmentHomeBinding
import com.example.trainingtracker.ui.exerciseCard.AddCardActivity
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseLog.AddLogActivity
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.muscles.MuscleStatus
import com.example.trainingtracker.ui.muscles.MuscleStorage
import com.example.trainingtracker.ui.tag.Tag
import com.example.trainingtracker.ui.tag.TagAdapter
import com.example.trainingtracker.ui.tag.TagStorage
import java.time.LocalDateTime
import java.util.UUID


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var cardAdapter: HomeCardAdapter
    private lateinit var tagAdapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val addCardButtonBinding = binding.addCardButton
        addCardButtonBinding.setOnClickListener {
            val intent = Intent(activity, AddCardActivity::class.java)
            startActivity(intent)
        }


        cardAdapter = HomeCardAdapter(requireContext()) { clickedCard ->
            val intent = Intent(context, AddLogActivity::class.java).apply {
                putExtra("EXTRA_CARD_ITEM", clickedCard)
            }
            startActivity(intent)
        }
        // todo : study intent, put extra?


        tagAdapter = TagAdapter(requireContext()) { clickedTag ->
            if (clickedTag.name == Tag.ADD_TAG.name) {
                val inputDialog = AlertDialog.Builder(requireContext())
                val inputEditText = EditText(requireContext())
                inputDialog.setView(inputEditText)
                inputDialog.setTitle(getString(R.string.tag_enter_name))

                inputDialog.setPositiveButton("OK") { dialog, _ ->
                    val newTagString = inputEditText.text.toString().trim()
                    if (newTagString.isNotEmpty()) {
                        var uniqueId: UUID
                        do {
                            uniqueId = UUID.randomUUID()
                        } while (TagStorage.isIdInUse(requireContext(), uniqueId))

                        // Add the new tag to your data source and notify the adapter

                        val newTag = Tag(
                            id = uniqueId,
                            timeAdded = LocalDateTime.now(),
                            name = newTagString)

                        val newTags : MutableList<Tag> = TagStorage.loadTags(requireContext()).toMutableList()
                        newTags.add(newTag)
                        newTags.removeAll { it == Tag.ADD_TAG }
                        newTags.add(Tag.ADD_TAG)
                        TagStorage.saveTags(requireContext(), newTags)
                        refresh()
                    }
                    dialog.dismiss()
                }

                inputDialog.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

                inputDialog.show()
            } else {
                val newTag = Tag(
                    id = clickedTag.id,
                    timeAdded = clickedTag.timeAdded,
                    name = clickedTag.name,
                    isSelected = !clickedTag.isSelected
                )
                tagAdapter.editItem(clickedTag, newTag)
                refresh()
            }
        }


        val exerciseRecyclerViewBinding = binding.exerciseRecyclerView
        exerciseRecyclerViewBinding.layoutManager = LinearLayoutManager(requireContext())
        exerciseRecyclerViewBinding.adapter = cardAdapter
        exerciseRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        //todo : better animator
        homeViewModel.cardRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            cardAdapter.submitList(newData)
            homeViewModel.updateCardRecyclerViewData(newData)
        }

        val tagRecyclerViewBinding = binding.filterBar.tagRecyclerView
        tagRecyclerViewBinding.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tagRecyclerViewBinding.adapter = tagAdapter
        tagRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        homeViewModel.tagRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            tagAdapter.submitList(newData)
            homeViewModel.updateTagRecyclerViewData(newData)
        }

        val frontMuscleBinding = binding.muscleFront
        val backMuscleBinding = binding.muscleBack

        homeViewModel.muscleViewData.observe(viewLifecycleOwner) { muscles ->
            muscles.forEach { muscle ->
                val buttons = getButtonsByMuscleName(muscle.name)
                val drawableIds = getDrawableIdsByMuscleName(muscle.name)
                buttons.zip(drawableIds).forEach { (button, drawableId) ->
                    val drawable = ContextCompat.getDrawable(requireContext(), drawableId)?.mutate() as? VectorDrawable
                    drawable?.let {
                        val color = getColorByStatus(muscle.status)
                        DrawableCompat.setTint(it, ContextCompat.getColor(requireContext(), color))
                        button.setImageDrawable(it)
                    }
                }
            }
        }
        // Initialize or refresh muscle data when the view is created
        homeViewModel.updateMuscleViewData(MuscleStorage.loadMuscles(requireContext()))
        // Setup UI interactions and LiveData observers
        homeViewModel.muscleViewData.observe(viewLifecycleOwner) { muscles ->
            // TODO : how to submit?
            updateMuscleUI(muscles)
        }
        return root
    }


    private fun updateMuscleUI(muscles: List<Muscle>) {
        muscles.forEach { muscle ->
            val buttons = getButtonsByMuscleName(muscle.name)
            val drawableIds = getDrawableIdsByMuscleName(muscle.name)
            buttons.zip(drawableIds).forEach { (button, drawableId) ->
                val drawable = ContextCompat.getDrawable(requireContext(), drawableId)?.mutate() as? VectorDrawable
                drawable?.let {
                    val color = getColorByStatus(muscle.status)
                    DrawableCompat.setTint(it, ContextCompat.getColor(requireContext(), color))
                    button.setImageDrawable(it)
                }
            }
        }
    }


    override fun onResume() {
        refresh()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onStop() {
//        TagStorage.resetSelection(requireContext())
        // TODO : reset tag selection when stop the app
        super.onStop()
    }

    private fun refresh() {
        val tags : MutableList<Tag> = TagStorage.loadTags(requireContext()).toMutableList()
        tags.removeAll { it == Tag.ADD_TAG }
        tags.add(Tag.ADD_TAG)
        tagAdapter.submitList(tags)
        val selectedTags = TagStorage.getSelectedTags(requireContext())
        val cards = CardStorage.getSelectedCard(requireContext(), selectedTags)
        cardAdapter.submitList(cards)
        updateMuscleUI(MuscleStorage.loadMuscles(requireContext()))
    }

    private fun getColorByStatus(status: Int): Int {
        return when (status) {
            MuscleStatus.RECOVERED -> R.color.turquoise
            MuscleStatus.RECOVERING -> R.color.grey
            MuscleStatus.NEED_EXERCISE -> R.color.cafeLatte
            else -> R.color.red
        }
    }

    private fun getDrawableIdsByMuscleName(name: String): List<Int> {
        return when (name) {
            "Neck / Traps" -> listOf(R.drawable.muscle_front_neck_traps, R.drawable.muscle_back_neck_traps)
            "Shoulder" -> listOf(R.drawable.muscle_front_shoulder, R.drawable.muscle_back_shoulder)
            "Chest" -> listOf(R.drawable.muscle_front_chest)
            "Arm" -> listOf(R.drawable.muscle_front_biceps, R.drawable.muscle_back_triceps, R.drawable.muscle_front_forearm, R.drawable.muscle_back_forearm)
            "Biceps" -> listOf(R.drawable.muscle_front_biceps)
            "Triceps" -> listOf(R.drawable.muscle_back_triceps)
            "Forearms" -> listOf(R.drawable.muscle_front_forearm, R.drawable.muscle_back_forearm)
            "Abs" -> listOf(R.drawable.muscle_front_abs)
            "Obliques" -> listOf(R.drawable.muscle_front_obliques, R.drawable.muscle_back_obliques)
            "Back" -> listOf(R.drawable.muscle_back_upper_back, R.drawable.muscle_back_lower_back)
            "Upper Back" -> listOf(R.drawable.muscle_back_upper_back)
            "Lower Back" -> listOf(R.drawable.muscle_back_lower_back)
            "Leg" -> listOf(R.drawable.muscle_front_inner_thigh, R.drawable.muscle_front_quadriceps, R.drawable.muscle_back_hamstrings, R.drawable.muscle_front_calves, R.drawable.muscle_back_calves)
            "Inner Thigh" -> listOf(R.drawable.muscle_front_inner_thigh)
            "Glutes / Buttocks" -> listOf(R.drawable.muscle_back_glutes_buttocks)
            "Quadriceps" -> listOf(R.drawable.muscle_front_quadriceps)
            "Hamstrings" -> listOf(R.drawable.muscle_back_hamstrings)
            "Calves" -> listOf(R.drawable.muscle_front_calves, R.drawable.muscle_back_calves)
            else -> listOf(R.drawable.muscle_front_calves, R.drawable.muscle_back_calves)
        }
    }



    private fun getButtonsByMuscleName(name: String): List<ImageButton> {
        val muscleToButtonIds = mapOf(
            "Neck / Traps" to listOf(R.id.muscle_front_neck_traps, R.id.muscle_back_neck_traps),
            "Shoulder" to listOf(R.id.muscle_front_shoulder, R.id.muscle_back_shoulder),
            "Chest" to listOf(R.id.muscle_front_chest),
            "Arm" to listOf(R.id.muscle_front_biceps, R.id.muscle_back_triceps, R.id.muscle_front_forearm, R.id.muscle_back_forearm),
            "Biceps" to listOf(R.id.muscle_front_biceps),
            "Triceps" to listOf(R.id.muscle_back_triceps),
            "Forearms" to listOf(R.id.muscle_front_forearm, R.id.muscle_back_forearm),
            "Abs" to listOf(R.id.muscle_front_abs),
            "Obliques" to listOf(R.id.muscle_front_obliques, R.id.muscle_back_obliques),
            "Back" to listOf(R.id.muscle_back_upper_back, R.id.muscle_back_lower_back),
            "Upper Back" to listOf(R.id.muscle_back_upper_back),
            "Lower Back" to listOf(R.id.muscle_back_lower_back),
            "Leg" to listOf(R.id.muscle_front_inner_thigh, R.id.muscle_front_quadriceps, R.id.muscle_back_hamstrings, R.id.muscle_front_calves, R.id.muscle_back_calves),
            "Inner Thigh" to listOf(R.id.muscle_front_inner_thigh),
            "Glutes / Buttocks" to listOf(R.id.muscle_back_glutes_buttocks),
            "Quadriceps" to listOf(R.id.muscle_front_quadriceps),
            "Hamstrings" to listOf(R.id.muscle_back_hamstrings),
            "Calves" to listOf(R.id.muscle_front_calves, R.id.muscle_back_calves)
        )

        val buttonIds = muscleToButtonIds[name] ?: emptyList()
        return buttonIds.mapNotNull { id -> view?.findViewById<ImageButton>(id) }
    }


}