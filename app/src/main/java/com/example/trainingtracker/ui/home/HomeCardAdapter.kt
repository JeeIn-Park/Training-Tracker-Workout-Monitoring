package com.example.trainingtracker.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.example.trainingtracker.ui.exerciseCard.ExerciseCardDiffCallback
import com.example.trainingtracker.R
import com.example.trainingtracker.ui.exerciseCard.AddCardActivity
import com.example.trainingtracker.ui.exerciseLog.LogStorage
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeCardAdapter(
    private val context: Context, private val onItemClick: (ExerciseCard) -> Unit) :
    ListAdapter<ExerciseCard, HomeCardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

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

        // reference (each parts in a card layout)
        fun bind(cardItem: ExerciseCard) {
            if (cardItem.name == "") {
                exerciseName.text = "N/A"
            } else exerciseName.text = cardItem.name

            if (cardItem.tag.isNotEmpty()){
                tag.text = cardItem.tag.joinToString(prefix = "# ", separator = " ") { it.name }
            } else tag.text = ""

            if ( cardItem.lastActivity != null ) {
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
            } else {
                lastExercise.visibility = View.GONE
            }

            if (cardItem.mainMuscles.isEmpty()){
                mainMuscle.visibility = View.GONE
            } else {
                mainMuscle.text = "Main muscle : ${cardItem.mainMuscles.joinToString(separator = ", ") { it.name }}"
            }

            if (cardItem.subMuscles.isEmpty()){
                subMuscle.visibility = View.GONE
            } else {
                subMuscle.text = "Sub muscle : ${cardItem.subMuscles.joinToString(separator = ", ") { it.name }}"
            }

            personalRecord.text = context.getString(R.string.title_one_rep_max)


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
                            val intent = Intent(context, AddCardActivity::class.java).apply {
                                putExtra("EXTRA_CARD_ITEM", cardItem)
                            }
                            context.startActivity(intent)
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

