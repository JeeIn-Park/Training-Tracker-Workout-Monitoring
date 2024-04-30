package com.jeein.trainingtracker.ui.status

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.databinding.FragmentStatusBinding
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.tag.Tag
import com.jeein.trainingtracker.ui.tag.TagAdapter
import com.jeein.trainingtracker.ui.tag.TagFactory
import com.jeein.trainingtracker.ui.tag.TagStorage

class StatusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
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

        cardAdapter = StatusCardAdapter(requireContext()) { clickedCard ->
//          val bundle = bundleOf("exerciseCardArg" to clickedCard)
//          findNavController().navigate(R.id.action_statusFragment_to_addLogFragment, bundle)
        }

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

        val exerciseRecyclerViewBinding = binding.spreadSheetRecyclerView
        exerciseRecyclerViewBinding.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        exerciseRecyclerViewBinding.adapter = cardAdapter
        exerciseRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        statusViewModel.cardRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            cardAdapter.submitList(newData)
            statusViewModel.updateCardRecyclerViewData(newData)
        }

        val tagRecyclerViewBinding = binding.filterBar.tagRecyclerView
        tagRecyclerViewBinding.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tagRecyclerViewBinding.adapter = tagAdapter
        tagRecyclerViewBinding.itemAnimator = DefaultItemAnimator()
        statusViewModel.tagRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            tagAdapter.submitList(newData)
            statusViewModel.updateTagRecyclerViewData(newData)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refresh() {
        val tags: MutableList<Tag> = TagStorage.loadTags(requireContext()).toMutableList()
        tags.removeAll { it == Tag.ADD_TAG }
        tags.add(Tag.ADD_TAG)
        tagAdapter.submitList(tags)
        val selectedTags = TagStorage.getSelectedTags(requireContext())
        val cards = CardStorage.getSelectedCard(requireContext(), selectedTags)
        cardAdapter.submitList(cards)
    }

}