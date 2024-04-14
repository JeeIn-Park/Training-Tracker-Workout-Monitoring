package com.jeein.trainingtracker

import android.content.Context
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView

object TableSetup {

    fun setKgRepTableRow(context: Context, set: Int?, kg: String, rep: String) : TableRow {
        val tableRow = TableRow(context)
        val setCountTextView = TextView(context)
        val kgAndRepTextView = TextView(context)

        setCountTextView.text = if (set != null) {
            "${set?.toString()} set"
        } else {
            ""
        }
        val setCountParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        setCountTextView.layoutParams = setCountParams

        kgAndRepTextView.text = "$kg kg * $rep"
        val kgAndRepParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        kgAndRepTextView.layoutParams = kgAndRepParams

        setCountTextView.setPadding(16, 8, 16, 8)
        kgAndRepTextView.setPadding(16, 8, 16, 8)
        setCountTextView.gravity = Gravity.CENTER_VERTICAL
        kgAndRepTextView.gravity = Gravity.RIGHT
        setCountTextView.setBackgroundResource(R.drawable.style_textview_outline)

        tableRow.addView(setCountTextView)
        tableRow.addView(kgAndRepTextView)
        tableRow.setBackgroundResource(R.drawable.style_textview_outline)

        return tableRow
    }


    fun setKgRepTableRow(context: Context, set: Int?, kg: Float?, rep: Int?) : TableRow {
        val tableRow = TableRow(context)
        val setCountTextView = TextView(context)
        val kgAndRepTextView = TextView(context)

        setCountTextView.text = if (set != null) {
            "${set?.toString()} set"
        } else { "" }
        val setCountParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
        setCountTextView.layoutParams = setCountParams

        kgAndRepTextView.text = "${kg.toString()} kg * ${rep.toString()}"
        val kgAndRepParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        kgAndRepTextView.layoutParams = kgAndRepParams

        setCountTextView.setPadding(16, 8, 16, 8)
        kgAndRepTextView.setPadding(16, 8, 16, 8)
        setCountTextView.gravity = Gravity.CENTER_VERTICAL
        kgAndRepTextView.gravity = Gravity.RIGHT
        setCountTextView.setBackgroundResource(R.drawable.style_textview_outline)

        tableRow.addView(setCountTextView)
        tableRow.addView(kgAndRepTextView)
        tableRow.setBackgroundResource(R.drawable.style_textview_outline)

        return tableRow
    }

}