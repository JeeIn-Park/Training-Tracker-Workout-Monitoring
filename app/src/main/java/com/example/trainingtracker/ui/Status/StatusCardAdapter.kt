package com.example.trainingtracker.ui.Status

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.CardStorage
import com.example.trainingtracker.ExerciseCard
import com.example.trainingtracker.ExerciseCardDiffCallback
import com.example.trainingtracker.R

class StatusCardAdapter(private val context: Context, private val onItemClick: (ExerciseCard) -> Unit) :
    ListAdapter<ExerciseCard, StatusCardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.status_card_item, parent, false)
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
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        // reference (each parts in a card layout)
        fun bind(cardItem: ExerciseCard) {
            textView.text = "${cardItem.name} - : ${cardItem.mainMuscles?.joinToString(", ")}"
            deleteButton.setOnClickListener {
            }

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

