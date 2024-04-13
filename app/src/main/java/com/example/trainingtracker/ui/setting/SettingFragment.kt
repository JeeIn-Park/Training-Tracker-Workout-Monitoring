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
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

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
                sendFeedbackByEmail(feedback)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun sendFeedbackByEmail(feedback: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.feedback_mail)))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback from App")
            putExtra(Intent.EXTRA_TEXT, feedback)
        }
        try {
            startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showReportErrorDialog() {
        val editText = EditText(context)
        AlertDialog.Builder(context)
            .setTitle("Report Error")
            .setMessage("Please describe the error, including steps to reproduce it. By submitting, you agree to our Privacy Policy.")
            .setView(editText)
            .setPositiveButton("Submit") { dialog, which ->
                val description = editText.text.toString()
                collectDataAndSendEmail(description)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun collectDataAndSendEmail(description: String) {
        val logcatData = getLogcatData()
        val userData = getUserData()

        val emailBody = "Error Description: $description\n\nLogcat Data:\n$logcatData\n\nUser Data: $userData"
        sendReportByEmail(emailBody)
    }

    private fun getLogcatData(): String {
        try {
            val process = Runtime.getRuntime().exec("logcat -d -v time MyAppTag:* *:S")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))

            val log = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                log.append(line).append("\n")
            }
            return log.toString()
        } catch (ex: IOException) {
            Log.e("SettingFragment", "Error reading logcat", ex)
        }
        return "Unable to collect Logcat data."
    }


    private fun getUserData(): String {
        // Collect user data. Ensure compliance with privacy laws.
        return "User data here" // Replace this with actual user data collection logic.
    }

    private fun sendReportByEmail(emailBody: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.feedback_mail)))
            putExtra(Intent.EXTRA_SUBJECT, "Error Report from App")
            putExtra(Intent.EXTRA_TEXT, emailBody)
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
