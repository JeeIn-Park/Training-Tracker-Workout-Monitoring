package com.jeein.trainingtracker.ui.home

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeein.trainingtracker.FormattedStringGetter
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCardDiffCallback
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.ui.exerciseLog.LogStorage
import java.io.Serializable
import java.time.LocalDateTime

class HomeCardAdapter(
    private val context: Context,
    private val onItemClick: (ExerciseCard) -> Unit,
    private val navigateToEdit: (ExerciseCard) -> Unit
) : ListAdapter<ExerciseCard, HomeCardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_home_card, parent, false)
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
        private val exerciseName: TextView = itemView.findViewById(R.id.ExerciseName)
        private val tag: TextView = itemView.findViewById(R.id.Tag)
        private val lastExercise: TextView = itemView.findViewById(R.id.LastExercise)
        private val mainMuscle: TextView = itemView.findViewById(R.id.MainMuscle)
        private val subMuscle: TextView = itemView.findViewById(R.id.SubMuscle)
        private val personalRecord: TextView = itemView.findViewById(R.id.PersonalRecord)

        // reference (each parts in a card layout)
        fun bind(cardItem: ExerciseCard) {
            if (cardItem.name == "") {
                exerciseName.text = "N/A"
                exerciseName.setTypeface(null, Typeface.BOLD)
            } else {
                exerciseName.text = cardItem.name
                exerciseName.setTypeface(null, Typeface.BOLD)
            }

            if (cardItem.tag.isNotEmpty()){
                tag.text = cardItem.tag.joinToString(prefix = "# ", separator = " ") { it.name }
            } else tag.text = ""

            // Create a local immutable copy of lastActivity
            val lastActivity = cardItem.lastActivity

            if (lastActivity != null) {
                lastExercise.text = FormattedStringGetter.dateTimeWithDiff(lastActivity, LocalDateTime.now())
            } else {
                lastExercise.visibility = View.GONE
            }

            if (cardItem.mainMuscles.isEmpty()){
                mainMuscle.visibility = View.GONE
            } else {
                mainMuscle.text = FormattedStringGetter.mainMuscles(cardItem.mainMuscles)
            }

            if (cardItem.subMuscles.isEmpty()){
                subMuscle.visibility = View.GONE
            } else {
                subMuscle.text = FormattedStringGetter.subMuscles(cardItem.subMuscles)
            }


            val oneRepMaxRecordDate = cardItem.oneRepMaxRecordDate
            if (oneRepMaxRecordDate != null) {
                personalRecord.text = FormattedStringGetter.oneRepMaxRecordWithDate_ShortPB(cardItem, LocalDateTime.now())
            } else {
                personalRecord.visibility = View.GONE
            }

            itemView.setOnLongClickListener {
                showEditDeleteOptions(cardItem)
                true
            }

        }

        private fun showEditDeleteOptions(cardItem: ExerciseCard) {
            val options = arrayOf("Edit", "Delete")
            AlertDialog.Builder(context)
                .setItems(options) { dialog, which ->
                    when (which) {
                        // Edit
                        0 -> {
                            navigateToEdit(cardItem)
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

