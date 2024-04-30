package com.jeein.trainingtracker.ui.home

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
            findNavController().navigate(R.id.action_homeFragment_to_addCardFragment)
        }

        cardAdapter = HomeCardAdapter(requireContext(), ::handleItemClick, ::navigateToEdit)
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


        val exerciseRecyclerViewBinding = binding.exerciseRecyclerView
        exerciseRecyclerViewBinding.layoutManager = LinearLayoutManager(requireContext())
        exerciseRecyclerViewBinding.adapter = cardAdapter
        exerciseRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        homeViewModel.cardRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            cardAdapter.submitList(newData)
        }

        val tagRecyclerViewBinding = binding.filterBar.tagRecyclerView
        tagRecyclerViewBinding.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        var lastAction: Int = MotionEvent.ACTION_CANCEL

        fab.setOnTouchListener { view, event ->
            handler.removeCallbacks(fadeOutRunnable)
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                view.alpha = 1.0f
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                    lastAction = MotionEvent.ACTION_DOWN
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY
                    val parentView = view.parent as View
                    val actionBarHeight = resources.getDimension(R.dimen.action_bar_height)
                    val bottomNavHeight = resources.getDimension(R.dimen.bottom_nav_height)
                    if (newY + view.height > parentView.height - bottomNavHeight + view.height
                        || newY < actionBarHeight - view.height
                    ) {
                        true  // Still consume the event but don't move the view
                    } else {
                        view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start()
                        lastAction = MotionEvent.ACTION_MOVE
                        true
                    }
                }

                MotionEvent.ACTION_UP -> {
                    handler.postDelayed(fadeOutRunnable, 300)
                    val parentWidth = (view.parent as View).width.toFloat()
                    val toRight = view.x + view.width / 2 > parentWidth / 2
                    val finalPosition = if (toRight) parentWidth - view.width else 0f
                    if (lastAction == MotionEvent.ACTION_MOVE) {
                        view.animate()
                            .x(finalPosition)
                            .setDuration(300)
                            .start()
                    } else {
                        performFabClick()
                    }
                    true
                }

                else -> false
            }
        }

        fab.setOnClickListener {
            performFabClick()
            handler.postDelayed(fadeOutRunnable, 300)
        }

        fab.alpha = 1.0f
        handler.postDelayed(fadeOutRunnable, 300)
    }

    private fun performFabClick() {
        findNavController().navigate(R.id.action_homeFragment_to_addCardFragment)
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
        println(muscles)
        updateMuscleImages(requireContext(), muscles)
    }

    private fun handleItemClick(cardItem: ExerciseCard) {
        val bundle = bundleOf("exerciseCardArg" to cardItem)
        findNavController().navigate(R.id.action_homeFragment_to_addLogFragment, bundle)
    }

    private fun navigateToEdit(cardItem: ExerciseCard) {
        val bundle = bundleOf("exerciseCardArg" to cardItem)
        findNavController().navigate(R.id.action_homeFragment_to_addCardFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(fadeOutRunnable)
        _binding = null
    }

}