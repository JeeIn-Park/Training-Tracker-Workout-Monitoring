package com.example.trainingtracker.ui.home

import com.example.trainingtracker.ui.exerciseLog.AddLogActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.databinding.FragmentHomeBinding
import com.example.trainingtracker.ui.tag.TagAdapter
import com.example.trainingtracker.ui.tag.TagStorage
import android.app.AlertDialog
import android.widget.EditText


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

      cardAdapter = HomeCardAdapter(requireContext()) { clickedCard ->
          val intent = Intent(context, AddLogActivity::class.java).apply {
              putExtra("EXTRA_CARD_ITEM", clickedCard)
          }
          startActivity(intent)
      }
      // todo : study intent, put extra?

      tagAdapter = TagAdapter(requireContext()) { clickedTag ->
          if (clickedTag == "  +  ") {
              // Ask for user input and change "+" to it
              val inputDialog = AlertDialog.Builder(requireContext())
              val inputEditText = EditText(requireContext())
              inputDialog.setView(inputEditText)
              inputDialog.setTitle("Enter a new tag")

              inputDialog.setPositiveButton("OK") { dialog, _ ->
                  val newTag = inputEditText.text.toString().trim()
                  if (newTag.isNotEmpty()) {
                      // Add the new tag to your data source and notify the adapter
                      val updatedTags = mutableListOf<String>()
                      updatedTags.addAll(tagAdapter.currentList.dropLast(1))
                      updatedTags.add(newTag)
                      updatedTags.add("  +  ")
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
      homeViewModel.cardRecyclerViewData.observe(viewLifecycleOwner) {
              newData -> cardAdapter.submitList(newData)
          homeViewModel.updateCardRecyclerViewData(newData)
      }

      val tagRecyclerView  = binding.filterBar.tagRecyclerView
      tagRecyclerView.layoutManager = LinearLayoutManager(requireContext())
      tagRecyclerView.adapter = tagAdapter
      tagRecyclerView.itemAnimator = DefaultItemAnimator()
      homeViewModel.tagRecyclerViewData.observe(viewLifecycleOwner) {
              newData -> tagAdapter.submitList(newData)
          homeViewModel.updateTagRecyclerViewData(newData)
      }

      return root
  }

    override fun onResume() {
        super.onResume()
        val cards = CardStorage.loadCards(requireContext())
        cardAdapter.submitList(cards)
        val tags : MutableList<String> = TagStorage.loadTags(requireContext()).toMutableList()
        tags.add("  +  ")
        tagAdapter.submitList(tags)

    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}