package com.example.trainingtracker.ui.Status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingtracker.CardAdapter
import com.example.trainingtracker.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var cardAdapter: CardAdapter


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
      val statusViewModel =
          ViewModelProvider(this).get(StatusViewModel::class.java)
      _binding = FragmentStatusBinding.inflate(inflater, container, false)
      val root: View = binding.root

      cardAdapter = CardAdapter(requireContext())
      val exerciseRecyclerView = binding.exerciseRecyclerView
      exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
      exerciseRecyclerView.adapter = cardAdapter

      val addCardButton = binding.addCardButton
      addCardButton.setOnClickListener {
      }


      return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}