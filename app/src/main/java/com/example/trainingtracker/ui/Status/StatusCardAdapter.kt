package com.example.trainingtracker.ui.Status

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.example.trainingtracker.ui.exerciseCard.ExerciseCardDiffCallback
import com.example.trainingtracker.R
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class StatusCardAdapter(private val context: Context, private val onItemClick: (ExerciseCard) -> Unit) :
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

    fun addItem(cardItem: ExerciseCard) {
        val updatedList = currentList.toMutableList()
        updatedList.add(cardItem)
        submitList(updatedList)
        CardStorage.addCard(context, cardItem)
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

        private val emptyString : List<String> = listOf("Select muscle")
        // reference (each parts in a card layout)
        fun bind(cardItem: ExerciseCard) {
            exerciseName.text = if (cardItem.name != null) {
                cardItem.name.toString()
            } else {
                "N/A"
            }

            tag.text = if (cardItem.tag != null) {
                cardItem.tag.toString()
            } else {
                ""
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = cardItem.lastActivity.format(formatter)

            val currentDate = LocalDateTime.now()
            val daysAgo = Duration.between(cardItem.lastActivity, currentDate).toDays()
            if (daysAgo == 0.toLong()) {
                lastExercise.text = formattedDate
            } else if (daysAgo == 1.toLong()) {
                lastExercise.text = "$formattedDate (1 day ago)"
            } else {
                val dateStringWithDaysAgo = "$formattedDate ($daysAgo days ago)"
                lastExercise.text = dateStringWithDaysAgo
            }


            if (cardItem.mainMuscles == emptyString) {
                mainMuscle.visibility = View.INVISIBLE
            } else {
                mainMuscle.visibility = View.VISIBLE
                mainMuscle.text = if (cardItem.mainMuscles == null) {
                    "Main muscle : N/A"
                } else {
                    "Main muscle : ${cardItem.mainMuscles.toString()}"
                }
            }

            if (cardItem.subMuscles == emptyString) {
                subMuscle.visibility = View.INVISIBLE
            } else {
                subMuscle.visibility = View.VISIBLE
                subMuscle.text = if (cardItem.subMuscles == null) {
                    "Main muscle : N/A"
                } else {
                    "Sub muscle : ${cardItem.subMuscles.toString()}"
                }
            }

            personalRecord.text = "place holder"// need to check the whole cards
            // Set long click listener
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
                .setTitle("Are you sure you want to delete this item?")
                .setMessage("All records will be deleted. Once deleted, it cannot be recovered.")
                .setPositiveButton("Delete") { dialog, which ->
                    removeItem(adapterPosition)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}

