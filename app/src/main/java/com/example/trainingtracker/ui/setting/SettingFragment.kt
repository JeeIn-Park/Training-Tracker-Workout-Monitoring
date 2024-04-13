package com.example.trainingtracker.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trainingtracker.R
import com.example.trainingtracker.databinding.FragmentSettingBinding
import android.app.AlertDialog
import android.widget.EditText


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Existing Privacy Policy button setup
        binding.SettingPrivacyPolicyButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_privacyPolicyFragment)
        }

        // Setup for the Send Feedback button
        binding.SettingSendFeedbackButton.setOnClickListener {
            showFeedbackDialog()
        }

        // Setup for the Report Error button
        binding.SettingReportErrorButton.setOnClickListener {
            showReportErrorDialog()
        }
    }

    private fun showFeedbackDialog() {
        val editText = EditText(context)
        AlertDialog.Builder(context)
            .setTitle("Send Feedback")
            .setMessage("Please enter your feedback below:")
            .setView(editText)
            .setPositiveButton("Submit") { dialog, which ->
                val feedback = editText.text.toString()
                ReportingHandler.sendFeedbackByEmail(requireContext(), feedback)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showReportErrorDialog() {
        val editText = EditText(context)
        AlertDialog.Builder(context)
            .setTitle("Report Error")
            .setMessage("Please describe the error, including steps to reproduce it. By submitting, you agree to our Privacy Policy.")
            .setView(editText)
            .setPositiveButton("Submit") { dialog, which ->
                val description = editText.text.toString()
                ReportingHandler.sendReportByEmail(requireContext(), description)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
