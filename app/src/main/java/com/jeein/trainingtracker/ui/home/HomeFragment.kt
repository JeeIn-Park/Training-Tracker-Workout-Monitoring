package com.jeein.trainingtracker.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.databinding.FragmentHomeBinding
import com.jeein.trainingtracker.ui.exerciseCard.AddCardActivity
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.exerciseLog.AddLogActivity
import com.jeein.trainingtracker.ui.muscles.Muscle
import com.jeein.trainingtracker.ui.muscles.MuscleStorage
import com.jeein.trainingtracker.ui.tag.Tag
import com.jeein.trainingtracker.ui.tag.TagAdapter
import com.jeein.trainingtracker.ui.tag.TagFactory
import com.jeein.trainingtracker.ui.tag.TagStorage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.Handler
import android.os.Looper
import android.animation.ObjectAnimator
import com.jeein.trainingtracker.ui.muscles.MuscleFactory.getDrawableResourceIdByStatus

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardAdapter: HomeCardAdapter
    private lateinit var tagAdapter: TagAdapter
    private lateinit var homeViewModel: HomeViewModel

    private val handler = Handler(Looper.getMainLooper())
    private val fadeOutRunnable = Runnable {
        val animator = ObjectAnimator.ofFloat(binding.addCardButton, "alpha", 0.5f)
        animator.duration = 200
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
                        TagStorage.addTag(requireContext(), TagFactory.createTag(requireContext(), newTagString))
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


        val exerciseRecyclerViewBinding = binding.exerciseRecyclerView
        exerciseRecyclerViewBinding.layoutManager = LinearLayoutManager(requireContext())
        exerciseRecyclerViewBinding.adapter = cardAdapter
        exerciseRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        homeViewModel.cardRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            cardAdapter.submitList(newData)
        }

        val tagRecyclerViewBinding = binding.filterBar.tagRecyclerView
        tagRecyclerViewBinding.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tagRecyclerViewBinding.adapter = tagAdapter
        tagRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        homeViewModel.tagRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            tagAdapter.submitList(newData)
        }

        return root
    }

    override fun onResume() {
        refresh()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDraggableFAB(binding.addCardButton)
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
                        // Snap to left or right side
                        val parentWidth = parentView.width
                        val toRight = view.x + view.width / 2 > parentWidth / 2
                        view.animate()
                            .x(if (toRight) (parentWidth - view.width).toFloat() else 0f)
                            .setDuration(300)
                            .start()
                    } else {
                        // If not dragged, perform the click action
                        fab.performClick()
                    }
                    handler.postDelayed(fadeOutRunnable, 2000)  // Re-initiate the fade out
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
        val intent = Intent(activity, AddCardActivity::class.java)
        startActivity(intent)
    }

    private fun updateMuscleImages(context: Context, muscles: List<Muscle>) {
        muscles.forEach { muscle ->
            for (drawableName in muscle.layout){
                val viewId = context.resources.getIdentifier(drawableName, "id", context.packageName)
                val muscleView = view?.findViewById<ImageButton>(viewId)
                muscleView?.setBackgroundResource(getDrawableResourceIdByStatus(requireContext(), muscle.status, drawableName))
            }
        }
    }

    private fun refresh() {
        val tags : MutableList<Tag> = TagStorage.loadTags(requireContext()).toMutableList()
        tags.removeAll { it == Tag.ADD_TAG }
        tags.add(Tag.ADD_TAG)
        homeViewModel.updateTagRecyclerViewData(tags)
        val selectedTags = TagStorage.getSelectedTags(requireContext())
        val cards = CardStorage.getSelectedCard(requireContext(), selectedTags)
        homeViewModel.updateCardRecyclerViewData(cards)
        val muscles =  MuscleStorage.loadMuscles(requireContext())
        println(muscles)
        updateMuscleImages(requireContext(),muscles)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(fadeOutRunnable)
        _binding = null
    }

}