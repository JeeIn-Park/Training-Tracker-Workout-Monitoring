package com.jeein.trainingtracker.ui.exerciseLog

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.ui.exerciseSet.ExerciseSet


class TodaySetTableAdapter(private val items: List<ExerciseSet>)
    : RecyclerView.Adapter<TodaySetTableAdapter.TableItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableItemViewHolder {
        val context = parent.context
        val setTableLayout = TableLayout(context)
        setTableLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setTableLayout.setPadding(8, 0, 8, 0)

        val parentLayout = LinearLayout(context)
        parentLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        parentLayout.orientation = LinearLayout.VERTICAL
        parentLayout.addView(setTableLayout)

        return TableItemViewHolder(parentLayout, setTableLayout)
    }


    override fun onBindViewHolder(holder: TableItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return items.size
    }



    inner class TableItemViewHolder(
        private val parentView: LinearLayout,
        private val setTableLayout: TableLayout
    ) : RecyclerView.ViewHolder(parentView) {

        fun bind(item: ExerciseSet) {

            val setRow = TableRow(setTableLayout.context)

            val setCountTextView = TextView(setTableLayout.context)
            setCountTextView.text = if (item.set != null) {
                "${item.set} set"
            } else {
                ""
            }
            val setCountParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            setCountTextView.layoutParams = setCountParams

            val kgAndRepTextView = TextView(setTableLayout.context)
            kgAndRepTextView.text = "${item.mass} kg x ${item.rep}"
            val kgAndRepParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            kgAndRepTextView.layoutParams = kgAndRepParams

            setCountTextView.setPadding(16, 8, 16, 8)
            kgAndRepTextView.setPadding(16, 8, 16, 8)
            setCountTextView.gravity = Gravity.CENTER_VERTICAL
            setCountTextView.setBackgroundResource(R.drawable.style_textview_outline)

            setRow.addView(setCountTextView)
            setRow.addView(kgAndRepTextView)
            setRow.setBackgroundResource(R.drawable.style_textview_outline)
            setTableLayout.addView(setRow)

        }
    }
}