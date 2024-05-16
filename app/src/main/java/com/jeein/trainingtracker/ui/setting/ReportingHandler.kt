package com.jeein.trainingtracker.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.jeein.trainingtracker.R
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

object ReportingHandler {

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


//    fun sendReportByEmail(context: Context, description: String) {
//        val logcatData = getLogcatData()
//        val userData = getUserData(context)
//        val emailBody =
//            "Error Description: $description\n\nLogcat Data:\n$logcatData\n\nUser Data: $userData"
//
//        val intent = Intent(Intent.ACTION_SEND).apply {
//            type = "message/rfc822"
//            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.feedback_mail)))
//            putExtra(Intent.EXTRA_SUBJECT, "Error Report from App")
//            putExtra(Intent.EXTRA_TEXT, emailBody)
//            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(userData))
//        }
//        try {
//            context.startActivity(Intent.createChooser(intent, "Send email using..."))
//        } catch (ex: android.content.ActivityNotFoundException) {
//            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun sendReportByEmail(context: Context, description: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.feedback_mail)))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback from App")
            putExtra(Intent.EXTRA_TEXT, description)
        }
        try {
            context.startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

}