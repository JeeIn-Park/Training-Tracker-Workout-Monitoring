package com.jeein.trainingtracker.ui.status

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeein.trainingtracker.FormattedStringGetter
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCardDiffCallback
import com.jeein.trainingtracker.ui.exerciseLog.LogStorage
import com.jeein.trainingtracker.ui.exerciseLog.PastLogTableAdapter
import com.jeein.trainingtracker.views.GraphViewAdapter
import com.jjoe64.graphview.GraphView
import java.time.LocalDateTime

class StatusCardAdapter(
    private val context: Context,
    private val onItemClick: (ExerciseCard) -> Unit
) :
    ListAdapter<ExerciseCard, StatusCardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_status_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    fun removeItem(position: Int) {
        val updatedList = currentList.toMutableList()
        val removedCard = updatedList.removeAt(position)
        submitList(updatedList)
        CardStorage.removeCard(context, removedCard)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseNameTextView: TextView =
            itemView.findViewById(R.id.StatusCard_ExerciseName_TextView)
        private val mainMuscleTextView: TextView =
            itemView.findViewById(R.id.StatusCard_MainMuscle_TextView)
        private val subMuscleTextView: TextView =
            itemView.findViewById(R.id.StatusCard_SubMuscle_TextView)
        private val tagTextView: TextView = itemView.findViewById(R.id.StatusCard_Tag_TextView)
        private val oneRepMaxTextView: TextView =
            itemView.findViewById(R.id.StatusCard_OneRepMax_TextView)
        private val graphView: GraphView = itemView.findViewById(R.id.StatusCard_GraphView)
        private val pastLogRecyclerView: RecyclerView =
            itemView.findViewById(R.id.StatusCard_PastLogs_RecyclerView)
        private lateinit var pastLogTableAdapter: PastLogTableAdapter

        fun bind(cardItem: ExerciseCard) {

            val logStorage = LogStorage(cardItem.id)
            val pastLog = logStorage.loadLogs(context)

            // TODO : extract this to the formatted text getter
            if (cardItem.name == "") {
                exerciseNameTextView.text = "N/A"
                exerciseNameTextView.setTypeface(null, Typeface.BOLD)
            } else {
                exerciseNameTextView.text = cardItem.name
                exerciseNameTextView.setTypeface(null, Typeface.BOLD)
            }

            if (cardItem.mainMuscles.isNotEmpty()) {
                mainMuscleTextView.text = FormattedStringGetter.mainMuscles(cardItem.mainMuscles)
            } else mainMuscleTextView.visibility = View.GONE

            if (cardItem.subMuscles.isNotEmpty()) {
                subMuscleTextView.text = FormattedStringGetter.subMuscles(cardItem.subMuscles)
            } else subMuscleTextView.visibility = View.GONE

            if (cardItem.tag.isNotEmpty()) {
                tagTextView.text = FormattedStringGetter.tags(cardItem.tag)
            } else tagTextView.text = ""

            val oneRepMaxRecordDate = cardItem.oneRepMaxRecordDate
            if (oneRepMaxRecordDate != null) {
                oneRepMaxTextView.text =
                    FormattedStringGetter.oneRepMaxRecordWithDate(cardItem, LocalDateTime.now())
            } else oneRepMaxTextView.visibility = View.GONE


            if (pastLog.size > 1) {
                GraphViewAdapter.setupGraphView(graphView, pastLog)
            } else graphView.visibility = View.GONE

            pastLogRecyclerView.layoutManager = LinearLayoutManager(context)
            pastLogTableAdapter = PastLogTableAdapter(pastLog)
            pastLogRecyclerView.adapter = pastLogTableAdapter


            itemView.setOnLongClickListener {
                showEditDeleteOptions(cardItem)
                true // Consume the long click
            }

        }

        private fun showEditDeleteOptions(cardItem: ExerciseCard) {
            val options = arrayOf("Edit", "Delete")
            AlertDialog.Builder(context)
                .setItems(options) { dialog, which ->
                    when (which) {
                        // Edit
                        0 -> {
                            // implement edit
                        }
                        // Delete
                        1 -> {
                            showDeleteWarning(cardItem)
                        }
                    }
                    dialog.dismiss()
                }
                .show()
        }

        private fun showDeleteWarning(cardItem: ExerciseCard) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.general_deletion_warning))
                .setMessage(context.getString(R.string.exercise_deletion_warning))
                .setPositiveButton("Delete") { dialog, which ->
                    val logStorage = LogStorage(cardItem.id)
                    logStorage.deleteLogs(context)
                    removeItem(adapterPosition)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}

