package com.example.trainingtracker.ui.exerciseLog

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PastLogTableAdapter(private val items: List<ExerciseLog>) : RecyclerView.Adapter<PastLogTableAdapter.TableItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableItemViewHolder {
        val context = parent.context
        val setTableLayout = TableLayout(context)
        val dateTableLayout = TableLayout(context)
        val emptyTableLayout = TableLayout(context)
        setTableLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setTableLayout.setPadding(8, 0, 8, 0)
//        dateTableLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dateTableLayout.setPadding(8, 0, 8, 0)

        // Add both TableLayouts to the parent view
        val parentLayout = LinearLayout(context)
        parentLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parentLayout.orientation = LinearLayout.VERTICAL
        parentLayout.addView(dateTableLayout)
        parentLayout.addView(setTableLayout)
        parentLayout.addView(emptyTableLayout)

        return TableItemViewHolder(parentLayout, setTableLayout, dateTableLayout, emptyTableLayout)
    }


    override fun onBindViewHolder(holder: TableItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class TableItemViewHolder(
        private val parentView: LinearLayout,
        private val setTableLayout: TableLayout,
        private val dateTableLayout: TableLayout,
        private val emptyTableLayout: TableLayout
    ) : RecyclerView.ViewHolder(parentView) {

        fun bind(item: ExerciseLog) {
            if (item.exerciseSetList.isNotEmpty()) {
                dateRow(item.dateTime)
                // Clear existing rows before binding new data
                setTableLayout.removeAllViews()

                // Bind data to views here
                for (exerciseSet in item.exerciseSetList) {
                    val setRow = TableRow(setTableLayout.context)

                    val setCountTextView = TextView(setTableLayout.context)
                    setCountTextView.text = if (exerciseSet.set != null) {
                        "${exerciseSet.set.toString()} set"
                    } else { "" }
                    val setCountParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
                    setCountTextView.layoutParams = setCountParams

                    val kgAndRepTextView = TextView(setTableLayout.context)
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
                    setTableLayout.addView(setRow)
                }

                emptyTableLayout.removeAllViews()
                val emptyView = TextView(emptyTableLayout.context)
                emptyView.text = ""
                val emptyRow = TableRow(emptyTableLayout.context)
                emptyRow.addView(emptyView)
                emptyTableLayout.addView(emptyRow)
            }
        }

        private fun dateRow(dateTime: LocalDateTime) {
            dateTableLayout.removeAllViews()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val formattedDateView = TextView(dateTableLayout.context)
            formattedDateView.text = dateTime.format(formatter)

            val dateRow = TableRow(dateTableLayout.context)
            dateRow.addView(formattedDateView)
            dateTableLayout.addView(dateRow)
        }
    }

}

