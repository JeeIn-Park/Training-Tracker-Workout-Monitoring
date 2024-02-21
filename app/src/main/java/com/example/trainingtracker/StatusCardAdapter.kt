package com.example.trainingtracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StatusCardAdapter(private val context: Context) :
    ListAdapter<ExerciseCard, StatusCardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.status_card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
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
            textView.text = "${cardItem.name} - : ${cardItem.mainMuscles.joinToString(", ")}"
            deleteButton.setOnClickListener {
                removeItem(adapterPosition)
            }
        }
    }
}

