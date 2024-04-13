package com.example.trainingtracker.ui.home

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
import com.example.trainingtracker.R
import com.example.trainingtracker.databinding.FragmentHomeBinding
import com.example.trainingtracker.ui.exerciseCard.AddCardActivity
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseLog.AddLogActivity
import com.example.trainingtracker.ui.muscles.Muscle
import com.example.trainingtracker.ui.muscles.MuscleFactory
import com.example.trainingtracker.ui.muscles.MuscleStorage
import com.example.trainingtracker.ui.tag.Tag
import com.example.trainingtracker.ui.tag.TagAdapter
import com.example.trainingtracker.ui.tag.TagFactory
import com.example.trainingtracker.ui.tag.TagStorage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.Handler
import android.os.Looper
import android.animation.ObjectAnimator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardAdapter: HomeCardAdapter
    private lateinit var tagAdapter: TagAdapter
    private lateinit var homeViewModel: HomeViewModel

    private val handler = Handler(Looper.getMainLooper())
    private val fadeOutRunnable = Runnable {
        val animator = ObjectAnimator.ofFloat(binding.addCardButton, "alpha", 0.5f)
        animator.duration = 500  // Duration of the fade effect
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
        //todo : better animator
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
        var lastAction: Int = MotionEvent.ACTION_CANCEL

        fab.setOnTouchListener { view, event ->
            handler.removeCallbacks(fadeOutRunnable)
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                view.alpha = 1.0f  // Make FAB fully opaque when touched or moved
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
                    view.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    handler.postDelayed(fadeOutRunnable, 300)
                    val parentWidth = (view.parent as View).width.toFloat()
                    val toRight = view.x + view.width / 2 > parentWidth / 2
                    val finalPosition = if (toRight) parentWidth - view.width else 0f
                    view.animate()
                        .x(finalPosition)
                        .setDuration(300)
                        .start()
                    true
                }
                else -> false
            }
        }

        fab.setOnClickListener {
            performFabClick()
            handler.postDelayed(fadeOutRunnable, 300)  // Reset the fade out delay when clicked
        }

        // Start with the FAB visible and schedule the first fade out
        fab.alpha = 1.0f
        handler.postDelayed(fadeOutRunnable, 300)
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
                muscleView?.setBackgroundResource(getDrawableResourceIdForMuscle(muscle.status, drawableName))
            }
        }
    }

    private fun getDrawableResourceIdForMuscle(status: Int, drawableName: String): Int {
        return when (status) {
            MuscleFactory.RECOVERED -> requireContext().resources.getIdentifier("${drawableName}_recovered", "drawable", requireContext().packageName)
            MuscleFactory.RECOVERING -> requireContext().resources.getIdentifier("${drawableName}_recovering", "drawable", requireContext().packageName)
            MuscleFactory.NEED_EXERCISE -> requireContext().resources.getIdentifier("${drawableName}_need_exercise", "drawable", requireContext().packageName)
            else -> requireContext().resources.getIdentifier(drawableName, "drawable", requireContext().packageName)
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
        handler.removeCallbacks(fadeOutRunnable)  // Clean up to avoid memory leaks
        _binding = null
    }

}