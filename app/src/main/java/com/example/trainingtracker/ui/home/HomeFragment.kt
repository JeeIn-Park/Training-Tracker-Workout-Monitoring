package com.example.trainingtracker.ui.home

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


class HomeFragment : Fragment() {

    // TODO : should I store requireContext()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var cardAdapter: HomeCardAdapter
    private lateinit var tagAdapter: TagAdapter
    private lateinit var homeViewModel: HomeViewModel

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
        // Continue with your existing setup...
    }
    private fun setupDraggableFAB(fab: FloatingActionButton) {
        var dX = 0f
        var dY = 0f
        var lastAction: Int = MotionEvent.ACTION_CANCEL

        fab.setOnTouchListener { view, event ->
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

                    // Calculate boundaries, replace "actionBarHeight" and "bottomNavHeight" with actual measurements
                    val actionBarHeight = resources.getDimension(
                        androidx.transition.R.dimen.abc_action_bar_default_height_material).toInt()  // define this in your dimens.xml
                    val bottomNavHeight = resources.getDimension(
                        com.google.android.material.R.dimen.design_bottom_navigation_height).toInt()  // define this in your dimens.xml

                    // Ensure the FAB doesn't go above the bottom of the ActionBar or below the top of the Bottom Navigation
                    if (newY + view.height > parentView.height - bottomNavHeight + view.height
                        || newY < actionBarHeight - view.height) {
                        lastAction = MotionEvent.ACTION_MOVE
                        true  // Still consume the event but don't move the view
                    } else {
                        // Apply new coordinates within bounds
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
                    if (lastAction == MotionEvent.ACTION_DOWN) {
                        view.performClick()
                    }
                    true
                }
                else -> false
            }
        }

        fab.setOnClickListener {
            // Handle what happens when the fab is actually clicked
            performFabClick()
        }
    }

    private fun performFabClick() {
        // Perform actions when the FAB is clicked, such as showing a dialog or navigating
        val intent = Intent(activity, AddCardActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}