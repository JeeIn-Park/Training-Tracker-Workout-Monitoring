package com.example.trainingtracker.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.databinding.FragmentHomeBinding
import com.example.trainingtracker.ui.exerciseCard.AddCardActivity
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseLog.AddLogActivity
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

    private val addTag: Tag = Tag(
        id = UUID.randomUUID(),
        timeAdded = LocalDateTime.now(),
        name = "+"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addCardButton = binding.addCardButton
        addCardButton.setOnClickListener {
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
            if (clickedTag.name == "+") {
                // Ask for user input and change "+" to it
                val inputDialog = AlertDialog.Builder(requireContext())
                val inputEditText = EditText(requireContext())
                inputDialog.setView(inputEditText)
                inputDialog.setTitle("Enter a new tag")

                inputDialog.setPositiveButton("OK") { dialog, _ ->
                    val newTagString = inputEditText.text.toString().trim()
                    if (newTagString.isNotEmpty()) {
                        var uniqueId: UUID
                        do {
                            uniqueId = UUID.randomUUID()
                        } while (CardStorage.isIdInUse(requireContext(), uniqueId))

                        // Add the new tag to your data source and notify the adapter

                        val newTag = Tag(
                            id = uniqueId,
                            timeAdded = LocalDateTime.now(),
                            name = newTagString)

                        val updatedTags = mutableListOf<Tag>()
                        updatedTags.addAll(tagAdapter.currentList.filter { it != addTag })
                        updatedTags.add(newTag)
                        updatedTags.add(addTag)
                        tagAdapter.submitList(updatedTags)
                    }
                    dialog.dismiss()
                }

                inputDialog.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

                inputDialog.show()
            } else {
                // Change the recyclerView item background color
            }
        }


        val exerciseRecyclerView = binding.exerciseRecyclerView
        exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        exerciseRecyclerView.adapter = cardAdapter
        exerciseRecyclerView.itemAnimator = DefaultItemAnimator()
        //todo : better animator
        homeViewModel.cardRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            cardAdapter.submitList(newData)
            homeViewModel.updateCardRecyclerViewData(newData)
        }

        val tagRecyclerView = binding.filterBar.tagRecyclerView
        tagRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tagRecyclerView.adapter = tagAdapter
        tagRecyclerView.itemAnimator = DefaultItemAnimator()
        homeViewModel.tagRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            tagAdapter.submitList(newData)
            homeViewModel.updateTagRecyclerViewData(newData)
        }

        return root
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
        val tags = tagAdapter.currentList.filter { it != addTag }
        TagStorage.saveTags(requireContext(), tags)
        super.onStop()
    }

    private fun refresh() {
        val cards = CardStorage.loadCards(requireContext())
        cardAdapter.submitList(cards)

        // Load tags and remove existing addTag if present, then add it again
        val tags: MutableList<Tag> = TagStorage.loadTags(requireContext()).toMutableList()
        tags.removeAll { it == addTag } // Remove existing addTag if present
        tags.add(addTag) // Add addTag
        tagAdapter.submitList(tags)
    }


}