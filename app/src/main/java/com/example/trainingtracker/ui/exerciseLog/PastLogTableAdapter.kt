package com.example.trainingtracker.ui.exerciseLog

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import java.time.format.DateTimeFormatter
import java.util.Locale

class PastLogTableAdapter(private val items: List<ExerciseLog>) : RecyclerView.Adapter<PastLogTableAdapter.TableItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableItemViewHolder {
        val context = parent.context
        val tableLayout = TableLayout(context)
        tableLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tableLayout.setPadding(8, 0, 8, 0)
        return TableItemViewHolder(tableLayout)
    }

    override fun onBindViewHolder(holder: TableItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class TableItemViewHolder(private val tableLayout: TableLayout) : RecyclerView.ViewHolder(tableLayout) {

        fun bind(item: ExerciseLog) {
            if (item.exerciseSetList.isNotEmpty()) {
                // Clear existing rows before binding new data
                tableLayout.removeAllViews()

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                val formattedDateView = TextView(tableLayout.context)
                formattedDateView.text = item.dateTime.format(formatter)
//
//                val dateRow = TableRow(tableLayout.context)
//                dateRow.addView(formattedDateView)
//                tableLayout.addView(dateRow)

                // Bind data to views here
                for (exerciseSet in item.exerciseSetList) {
                    val setRow = TableRow(tableLayout.context)

                    val setCountTextView = TextView(tableLayout.context)
                    setCountTextView.text = if (exerciseSet.set != null) {
                        "${exerciseSet.set.toString()} set"
                    } else { "" }
                    val setCountParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
                    setCountTextView.layoutParams = setCountParams

                    val kgAndRepTextView = TextView(tableLayout.context)
                    kgAndRepTextView.text = "${exerciseSet.mass} kg x ${exerciseSet.rep}"
                    val kgAndRepParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
                    kgAndRepTextView.layoutParams = kgAndRepParams

                    setCountTextView.setPadding(16, 8, 16, 8)
                    kgAndRepTextView.setPadding(16, 8, 16, 8)
                    setCountTextView.gravity = Gravity.CENTER_VERTICAL
                    setCountTextView.setBackgroundResource(R.drawable.style_textview_outline)

                    setRow.addView(setCountTextView)
                    setRow.addView(kgAndRepTextView)
                    setRow.setBackgroundResource(R.drawable.style_textview_outline)
                    tableLayout.addView(setRow)

                }
            }
        }
    }

}

