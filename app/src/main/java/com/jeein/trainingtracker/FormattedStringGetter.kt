package com.jeein.trainingtracker

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.muscles.Muscle
import com.jeein.trainingtracker.ui.tag.Tag
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object FormattedStringGetter {

    fun mainMuscles(mainMuscles: List<Muscle>) : String{
        return  "Main muscle : ${mainMuscles.joinToString(separator = ", ") { it.name }}"
    }

    fun subMuscles(subMuscles: List<Muscle>) : String{
        return "Sub muscle : ${subMuscles.joinToString(separator = ", ") { it.name }}"
    }

    fun tags(tags: List<Tag>) : String{
        return tags.joinToString(prefix = "# ", separator = " # ") { it.name }
    }

    fun dateTimeWithDiff(oldDate: LocalDateTime, newDate: LocalDateTime): String {
        var formattedDateText = ""
        val formattedDate = dateTime(oldDate)

        formattedDateText = when (val daysAgo = dateDiff(oldDate, newDate)) {
            0 -> formattedDate
            1 -> "$formattedDate (1 day ago)"
            else -> "$formattedDate ($daysAgo days ago)"
        }
        return formattedDateText
    }

    fun dateTime(
        dateTime: LocalDateTime,
        pattern: String = "yyyy-MM-dd",
        locale: Locale = Locale.getDefault()
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        return dateTime.format(formatter)
    }

    fun dateDiff(oldDate: LocalDateTime, newDate: LocalDateTime): Int {
        return Duration.between(oldDate, newDate).toDays().toInt()
    }


    fun dateDiffWithDaysAgo(oldDate: LocalDateTime, newDate: LocalDateTime): String {
        var formattedDateText = ""
        formattedDateText = when (val daysAgo = dateDiff(oldDate, newDate)) {
            0 -> ""
            1 -> "1 day ago"
            else -> "$daysAgo days ago"
        }
        return formattedDateText
    }

    fun oneRepMaxRecordWithDate(record: Float, formattedDateText: String): SpannableString {
        val recordString = "%.2f".format(record)
        val textToShow = "1RM: $recordString kg \n$formattedDateText"
        val spannable = SpannableString(textToShow)
        val start = textToShow.indexOf(recordString)
        val end = start + recordString.length + 3 // Include " kg" in the bold span (+3 for the space and "kg")
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        return spannable
    }

    fun oneRepMaxRecordWithDate(card: ExerciseCard, newDate: LocalDateTime): SpannableString {
        return oneRepMaxRecordWithDate(
            card.oneRepMaxRecord ?:0F,
            dateTimeWithDiff(card.oneRepMaxRecordDate ?: LocalDateTime.now(), newDate)
            )
    }

    fun oneRepMaxRecordWithDate_ShortPB(card: ExerciseCard, newDate: LocalDateTime): SpannableString {
        val recordString = "%.2f".format(card.oneRepMaxRecord)
        val dateText = dateTime(card.oneRepMaxRecordDate ?: LocalDateTime.now())
        val dateDiff = dateDiffWithDaysAgo(card.oneRepMaxRecordDate ?: LocalDateTime.now(), newDate)
        val textToShow : String = if (dateDiff == "") {
            "1RM: $recordString kg \n$dateText"
        } else "1RM: $recordString kg \n$dateText \n($dateDiff)"
        val spannable = SpannableString(textToShow)
        val start = textToShow.indexOf(recordString)
        val end = start + recordString.length + 3
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannable
    }

}