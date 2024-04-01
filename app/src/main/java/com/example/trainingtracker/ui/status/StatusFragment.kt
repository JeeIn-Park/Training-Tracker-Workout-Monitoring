package com.example.trainingtracker.ui.status

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
import com.example.trainingtracker.ui.exerciseCard.AddCardActivity
import com.example.trainingtracker.ui.exerciseLog.AddLogActivity
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.databinding.FragmentStatusBinding
import com.example.trainingtracker.ui.tag.Tag
import com.example.trainingtracker.ui.tag.TagAdapter
import com.example.trainingtracker.ui.tag.TagStorage

class StatusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var cardAdapter: StatusCardAdapter
    private lateinit var tagAdapter: TagAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
      val statusViewModel =
          ViewModelProvider(this).get(StatusViewModel::class.java)
      _binding = FragmentStatusBinding.inflate(inflater, container, false)
      val root: View = binding.root

      cardAdapter = StatusCardAdapter(requireContext()) {clickedCard ->
          // Handle the click action here
          // Launch the com.example.trainingtracker.ui.exerciseCard.AddCardActivity with the card details for editing
          val intent = Intent(context, AddLogActivity::class.java).apply {
              putExtra("EXTRA_CARD_ITEM", clickedCard)
          }
          startActivity(intent)

      }

      tagAdapter = TagAdapter(requireContext()) { clickedTag ->
      }


      val exerciseRecyclerView = binding.exerciseRecyclerView
      exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
      exerciseRecyclerView.adapter = cardAdapter

      val tagRecyclerView = binding.filterBar.tagRecyclerView
      tagRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      tagRecyclerView.adapter = tagAdapter
      tagRecyclerView.itemAnimator = DefaultItemAnimator()
      statusViewModel.tagRecyclerViewData.observe(viewLifecycleOwner) { newData ->
          tagAdapter.submitList(newData)
          statusViewModel.updateTagRecyclerViewData(newData)
      }


      val addCardButton = binding.addCardButton
      addCardButton.setOnClickListener {
          val intent = Intent(activity, AddCardActivity::class.java)
          startActivity(intent)
      }


      return root
  }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onStop() {
        val tags = mutableListOf<Tag>()
        tags.addAll(tagAdapter.currentList)
        TagStorage.saveTags(requireContext(), tags)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refresh() {
        val cards = CardStorage.loadCards(requireContext())
        cardAdapter.submitList(cards)
        tagAdapter.submitList(TagStorage.loadTags(requireContext()))
    }

}