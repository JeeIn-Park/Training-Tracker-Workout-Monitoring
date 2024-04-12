package com.example.trainingtracker

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object StringFormatter {

    fun getFormatDateTimeWithDiff(oldDate: LocalDateTime, newDate: LocalDateTime): String {
        var formattedDateText = ""
        val formattedDate = getFormattedDateTime(oldDate)

        formattedDateText = when (val daysAgo = getFormattedDateDiff(oldDate, newDate)) {
            0 -> formattedDate
            1 -> "$formattedDate (1 day ago)"
            else -> "$formattedDate ($daysAgo days ago)"
        }
        return formattedDateText
    }

    fun getFormattedDateTime(
        dateTime: LocalDateTime,
        pattern: String = "yyyy-MM-dd",
        locale: Locale = Locale.getDefault()
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        return dateTime.format(formatter)
    }

    fun getFormattedDateDiff(oldDate: LocalDateTime, newDate: LocalDateTime): Int {
        return Duration.between(oldDate, newDate).toDays().toInt()
    }

    fun getFormattedOneRepMaxRecordWithDate(record: Float, formattedDateText: String): SpannableString {
        val recordString = "%.2f".format(record)
        val textToShow = "1RM: $recordString kg \n$formattedDateText"
        val spannable = SpannableString(textToShow)
        val start = textToShow.indexOf(recordString)
        val end = start + recordString.length + 3 // Include " kg" in the bold span (+3 for the space and "kg")
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        return spannable
    }

    fun getFormattedOneRepMaxRecordWithDate(card: ExerciseCard, newDate: LocalDateTime): SpannableString {
        return getFormattedOneRepMaxRecordWithDate(
            card.oneRepMaxRecord ?:0F,
            getFormatDateTimeWithDiff(card.oneRepMaxRecordDate ?: LocalDateTime.now(), newDate)
            )
    }

}