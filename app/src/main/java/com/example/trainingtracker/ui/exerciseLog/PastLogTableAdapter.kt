package com.example.trainingtracker.ui.exerciseLog

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
            // Clear existing rows before binding new data
            tableLayout.removeAllViews()
            val row = TableRow(tableLayout.context)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val formattedDateView = TextView(tableLayout.context)
            formattedDateView.text = item.dateTime.format(formatter)
            row.addView(formattedDateView)
            tableLayout.addView(row)

            // Bind data to views here
            for (exerciseSet in item.exerciseSetList) {

                val setView = TextView(tableLayout.context)
                setView.text = if (exerciseSet.set != null) {
                    "${exerciseSet.set?.toString()} set"
                } else { "" }
                setView.setBackgroundResource(R.drawable.style_textview_outline)

                val recordView = TextView(tableLayout.context)
                recordView.text = "${exerciseSet.mass} kg x ${exerciseSet.rep}"
                recordView.setBackgroundResource(R.drawable.style_textview_outline)

                row.addView(setView)
                row.addView(recordView)
                tableLayout.addView(row)
            }
        }
    }
}

