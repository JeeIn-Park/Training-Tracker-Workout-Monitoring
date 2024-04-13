package com.example.trainingtracker.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.trainingtracker.R
import java.io.BufferedReader
import java.io.File
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


    private fun getUserData(context: Context): List<Uri> {
        val filesToAttach = mutableListOf<Uri>()
//        "log_${specificId}.dat",
        // TODO : store list of id in separate place so it can ensure it has those ids
        val fileNames = listOf("exercise_cards.dat", "muscles.dat", "tags.dat")

        fileNames.forEach { fileName ->
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val fileUri = Uri.fromFile(file)
                filesToAttach.add(fileUri)
            } else {
                Log.e("SettingsFragment", "File not found: $fileName")
            }
        }
        return filesToAttach
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


    fun sendReportByEmail(context: Context, description: String) {
        val logcatData = getLogcatData()
        val userData = getUserData(context)
        val emailBody = "Error Description: $description\n\nLogcat Data:\n$logcatData\n\nUser Data: $userData"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.feedback_mail)))
            putExtra(Intent.EXTRA_SUBJECT, "Error Report from App")
            putExtra(Intent.EXTRA_TEXT, emailBody)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(userData))
        }
        try {
            context.startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

}