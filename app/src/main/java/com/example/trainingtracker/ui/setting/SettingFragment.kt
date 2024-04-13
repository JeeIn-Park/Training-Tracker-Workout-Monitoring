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
import android.content.Intent
import android.widget.EditText
import android.widget.Toast

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
    }

    private fun showFeedbackDialog() {
        val editText = EditText(context)
        AlertDialog.Builder(context)
            .setTitle("Send Feedback")
            .setMessage("Please enter your feedback below:")
            .setView(editText)
            .setPositiveButton("Submit") { dialog, which ->
                val feedback = editText.text.toString()
                sendFeedbackByEmail(feedback)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun sendFeedbackByEmail(feedback: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("trainingtrackermailbox@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback from App")
            putExtra(Intent.EXTRA_TEXT, feedback)
        }
        try {
            startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
