package com.jeein.trainingtracker.ui.home

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jeein.trainingtracker.Event
import com.jeein.trainingtracker.EventManager
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.databinding.FragmentHomeBinding
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.muscles.Muscle
import com.jeein.trainingtracker.ui.muscles.MuscleFactory.getDrawableResourceIdByStatus
import com.jeein.trainingtracker.ui.muscles.MuscleStorage
import com.jeein.trainingtracker.ui.tag.Tag
import com.jeein.trainingtracker.ui.tag.TagAdapter
import com.jeein.trainingtracker.ui.tag.TagFactory
import com.jeein.trainingtracker.ui.tag.TagStorage

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardAdapter: HomeCardAdapter
    private lateinit var tagAdapter: TagAdapter
    private lateinit var homeViewModel: HomeViewModel

    private val handler = Handler(Looper.getMainLooper())
    private val fadeOutRunnable = Runnable {
        val animator = ObjectAnimator.ofFloat(binding.addCardButton, "alpha", 0.5f)
        animator.duration = 500
        animator.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addCardButtonBinding = binding.addCardButton
        addCardButtonBinding.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCardFragment)
        }

        cardAdapter = HomeCardAdapter(requireContext(), ::navAddLog, ::navEditCard)
        tagAdapter = TagAdapter(requireContext(), ::handleTagClick)
        // todo : study intent, put extra?

        val exerciseRecyclerViewBinding = binding.exerciseRecyclerView
        exerciseRecyclerViewBinding.layoutManager = LinearLayoutManager(requireContext())
        exerciseRecyclerViewBinding.adapter = cardAdapter
        exerciseRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        homeViewModel.cardRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            cardAdapter.submitList(newData)
            if (newData.isEmpty()) {
                EventManager.publish(
                    Event(
                        getString(R.string.event_empty_log), true
                    )
                )
            } else {
                EventManager.publish(
                    Event(
                        getString(R.string.event_empty_log), false
                    )
                )
            }
        }

        val tagRecyclerViewBinding = binding.filterBar.tagRecyclerView
        tagRecyclerViewBinding.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tagRecyclerViewBinding.adapter = tagAdapter
        tagRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        homeViewModel.tagRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            tagAdapter.submitList(newData)
        }

//        if (! isCardEmpty) {
//            binding.HomeDescriptionTextView.visibility = View.GONE
//        }

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            restoreFABPosition()
        }
        setupDraggableFAB(binding.addCardButton)

        EventManager.subscribe(
            requireContext().getString(R.string.event_delete_tag),
            ::deleteTagSubscriber
        )

        EventManager.subscribe(
            requireContext().getString(R.string.event_edit_tag),
            ::editTagSubscriber
        )

        EventManager.subscribe(
            requireContext().getString(R.string.event_empty_log)
        ) { event ->
            when (event.data as? Boolean) {
                true -> {
                    binding.HomeDescriptionTextView.visibility = View.VISIBLE
                }
                false -> {
                    binding.HomeDescriptionTextView.visibility = View.GONE
                }
                else -> {
                    // unhandled type passed
                }
            }
        }

    }

    override fun onResume() {
        refresh()
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        saveFABPosition(binding.addCardButton.x, binding.addCardButton.y)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventManager.unsubscribe(
            requireContext().getString(R.string.event_delete_tag),
            ::deleteTagSubscriber
        )

        EventManager.unsubscribe(
            requireContext().getString(R.string.event_edit_tag),
            ::editTagSubscriber
        )
        handler.removeCallbacks(fadeOutRunnable)
        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDraggableFAB(fab: FloatingActionButton) {
        var dX = 0f
        var dY = 0f
        var isDrag = false

        fab.setOnTouchListener { view, event ->
            val parentView = view.parent as View
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                    isDrag = false
                    handler.removeCallbacks(fadeOutRunnable)  // Cancel any pending fade-out actions
                    view.alpha = 1.0f  // Ensure the button is fully visible when touched
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY

                    // Restrict the FAB's movement within the bounds of the parent view
                    val leftBound = 0f
                    val rightBound = parentView.width - view.width
                    val topBound = 0f
                    val bottomBound = parentView.height - view.height

                    // Apply constraints to newX and newY
                    val constrainedX = newX.coerceIn(leftBound, rightBound.toFloat())
                    val constrainedY = newY.coerceIn(topBound, bottomBound.toFloat())

                    if (Math.abs(constrainedX - view.x) > 10 || Math.abs(constrainedY - view.y) > 10) {
                        isDrag = true
                        view.animate()
                            .x(constrainedX)
                            .y(constrainedY)
                            .setDuration(0)
                            .start()
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (isDrag) {
                        val parentWidth = parentView.width
                        val toRight = view.x + view.width / 2 > parentWidth / 2
                        val finalX = if (toRight) (parentWidth - view.width).toFloat() else 0f
                        view.animate()
                            .x(finalX)
                            .setDuration(300)
                            .withEndAction {
                                saveFABPosition(view.x, view.y)
                            }
                            .start()
                    } else {
                        fab.performClick()
                    }
                    handler.postDelayed(fadeOutRunnable, 2000)
                    true
                }

                else -> false
            }
        }

        fab.setOnClickListener {
            performFabClick()
        }

        handler.postDelayed(fadeOutRunnable, 2000)  // Initial delayed fade out
    }

    private fun performFabClick() {
        findNavController().navigate(R.id.action_homeFragment_to_addCardFragment)
    }


    private fun saveFABPosition(x: Float, y: Float) {
        Log.d("FABPosition", "Saving position: x=$x, y=$y")
        val sharedPrefs = requireActivity().getSharedPreferences("FABPosition", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putFloat("fabX", x)
            putFloat("fabY", y)
            apply()
        }
    }

    private fun restoreFABPosition() {
        val sharedPrefs = requireActivity().getSharedPreferences("FABPosition", Context.MODE_PRIVATE)
        val defaultX = -1.0f
        val defaultY = -1.0f
        val x = sharedPrefs.getFloat("fabX", defaultX)
        val y = sharedPrefs.getFloat("fabY", defaultY)
        if (x != defaultX && y != defaultY) {
            binding.addCardButton.x = x
            binding.addCardButton.y = y
        } else {
            Log.d("HomeFragment", "Default values are being used: x=$x, y=$y")
        }
    }


    private fun updateMuscleImages(context: Context, muscles: List<Muscle>) {
        muscles.forEach { muscle ->
            for (drawableName in muscle.layout) {
                val viewId =
                    context.resources.getIdentifier(drawableName, "id", context.packageName)
                val muscleView = view?.findViewById<ImageButton>(viewId)
                muscleView?.setBackgroundResource(
                    getDrawableResourceIdByStatus(
                        requireContext(),
                        muscle.status,
                        drawableName
                    )
                )
            }
        }
    }


    private fun refresh() {
        val tags: MutableList<Tag> = TagStorage.loadTags(requireContext()).toMutableList()
        tags.removeAll { it == Tag.ADD_TAG }
        tags.add(Tag.ADD_TAG)
        homeViewModel.updateTagRecyclerViewData(tags)
        val selectedTags = TagStorage.getSelectedTags(requireContext())
        val cards = CardStorage.getSelectedCard(requireContext(), selectedTags)
        homeViewModel.updateCardRecyclerViewData(cards)
        val muscles = MuscleStorage.loadMuscles(requireContext())
        updateMuscleImages(requireContext(), muscles)
    }


    private fun navAddLog(cardItem: ExerciseCard) {
        val bundle = bundleOf("exerciseCardArg" to cardItem)
        findNavController().navigate(R.id.action_homeFragment_to_addLogFragment, bundle)
    }


    private fun navEditCard(cardItem: ExerciseCard) {
        val bundle = bundleOf("exerciseCardArg" to cardItem)
        findNavController().navigate(R.id.action_homeFragment_to_addCardFragment, bundle)
    }


    private fun handleTagClick(clickedTag: Tag) {
        if (clickedTag.name == Tag.ADD_TAG.name) {
            val inputDialog = AlertDialog.Builder(requireContext())
            val inputEditText = EditText(requireContext())
            inputDialog.setView(inputEditText)
            inputDialog.setTitle(getString(R.string.tag_enter_name))

            inputDialog.setPositiveButton("OK") { dialog, _ ->
                val newTagString = inputEditText.text.toString().trim()
                if (newTagString.isNotEmpty()) {
                    TagStorage.addTag(
                        requireContext(),
                        TagFactory.createTag(requireContext(), newTagString)
                    )
                    refresh()
                }
                dialog.dismiss()
            }

            inputDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            inputDialog.show()
        } else {
            tagAdapter.editItem(clickedTag, TagFactory.clickTag(clickedTag))
            refresh()
        }
    }


    private fun deleteTagSubscriber(event: Event){
        val selectedTags = TagStorage.getSelectedTags(requireContext())
        val cards = CardStorage.getSelectedCard(requireContext(), selectedTags)
        homeViewModel.updateCardRecyclerViewData(cards)
    }


    private fun editTagSubscriber(event: Event){
        val selectedTags = TagStorage.getSelectedTags(requireContext())
        val cards = CardStorage.getSelectedCard(requireContext(), selectedTags)
        homeViewModel.updateCardRecyclerViewData(cards)
    }

}