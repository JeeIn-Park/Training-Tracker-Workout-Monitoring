package com.example.trainingtracker.ui.setting

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.trainingtracker.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object ReportingHandler {
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


    fun collectDataAndSendEmail(context: Context, description: String) {
        val logcatData = ReportingHandler.getLogcatData()
        val userData = getUserData()

        val emailBody = "Error Description: $description\n\nLogcat Data:\n$logcatData\n\nUser Data: $userData"
        sendReportByEmail(context, emailBody)
    }


    fun sendFeedbackByEmail(context: Context, feedback: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.feedback_mail)))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback from App")
            putExtra(Intent.EXTRA_TEXT, feedback)
        }
        try {
            context.startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendReportByEmail(context: Context, emailBody: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.feedback_mail)))
            putExtra(Intent.EXTRA_SUBJECT, "Error Report from App")
            putExtra(Intent.EXTRA_TEXT, emailBody)
        }
        try {
            context.startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

}