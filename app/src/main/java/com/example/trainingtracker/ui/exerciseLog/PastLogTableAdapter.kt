package com.example.trainingtracker.ui.exerciseLog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

            // Bind data to views here
            for (exerciseSet in item.exerciseSetList) {
                val subRow = TableRow(tableLayout.context)

                val setView = TextView(tableLayout.context)
                setView.text = exerciseSet.set.toString()

                val recordView = TextView(tableLayout.context)
                recordView.text = "${exerciseSet.mass} kg x ${exerciseSet.rep}"

                subRow.addView(setView)
                subRow.addView(recordView)
                tableLayout.addView(subRow)
            }
        }
    }
}

