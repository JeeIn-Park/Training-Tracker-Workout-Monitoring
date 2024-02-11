package com.example.trainingtracker.ui.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.CardAdapter
import com.example.trainingtracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var cardAdapter: CardAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

      _binding = FragmentHomeBinding.inflate(inflater, container, false)
      val root: View = binding.root

      cardAdapter = CardAdapter(requireContext())
      val cardView = binding.exerciseCard
      cardView.adapter = cardAdapter


      homeViewModel.recyclerViewData.observe(viewLifecycleOwner) {
          newData -> cardAdapter.submitList(newData)
      }

      val textView: TextView = binding.cardTopBar
      homeViewModel.text.observe(viewLifecycleOwner) {
          textView.text = it
      }

      return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}