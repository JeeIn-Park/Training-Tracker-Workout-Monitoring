package com.example.trainingtracker.ui.exerciseLog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PastLogTableAdapter(private val context: Context, private val items: List<ExerciseLog>) : RecyclerView.Adapter<PastLogTableAdapter.TableItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableItemViewHolder {
        return TableItemViewHolder(parent.context)
    }

    override fun onBindViewHolder(holder: TableItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TableItemViewHolder(private val context: Context) : RecyclerView.ViewHolder(View(context)) {
        private val tableLayout = TableLayout(context)

        init {
            // Initialize views programmatically here
            val layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableLayout.layoutParams = layoutParams
        }

        fun bind(item: ExerciseLog) {
            // Bind data to views here
            for (exerciseLog in item.exerciseSetList) {
                val subRow = TableRow(context)

                val setView = TextView(context)
                setView.text = exerciseLog.set.toString()

                val recordView = TextView(context)
                recordView.text = "${exerciseLog.mass} kg x ${exerciseLog.rep}"

                subRow.addView(setView)
                subRow.addView(recordView)
                tableLayout.addView(subRow)
            }
        }
    }
}

