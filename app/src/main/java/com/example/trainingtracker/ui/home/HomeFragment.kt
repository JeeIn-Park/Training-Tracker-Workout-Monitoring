package com.example.trainingtracker.ui.Home

import com.example.trainingtracker.ui.exerciseLog.AddLogActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var cardAdapter: HomeCardAdapter

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
          // Handle the click action here
          // Launch the com.example.trainingtracker.ui.exerciseCard.AddCardActivity with the card details for editing
          val intent = Intent(context, AddLogActivity::class.java).apply {
              putExtra("EXTRA_CARD_ITEM", clickedCard)
          }
          startActivity(intent)
      }

      val exerciseRecyclerView = binding.exerciseRecyclerView
      exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
      exerciseRecyclerView.adapter = cardAdapter
      exerciseRecyclerView.itemAnimator = DefaultItemAnimator()

      val textView: TextView = binding.textAboveRecyclerView

      //observer pattern
      homeViewModel.recyclerViewData.observe(viewLifecycleOwner) {
              newData -> cardAdapter.submitList(newData)
      }
      homeViewModel.text.observe(viewLifecycleOwner) {
          textView.text = it
      }

      return root
  }

    override fun onResume() {
        super.onResume()
        val cards = CardStorage.loadCards(requireContext())
        cardAdapter.submitList(cards)
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}