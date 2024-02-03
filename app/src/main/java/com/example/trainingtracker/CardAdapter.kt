package com.example.trainingtracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val context: Context) :
    ListAdapter<ExerciseCard, CardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_item, parent, false)
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

        fun bind(cardItem: ExerciseCard) {
            textView.text = "${cardItem.name} - Muscles: ${cardItem.mainMuscles.joinToString(", ")}"
            deleteButton.setOnClickListener {
                removeItem(adapterPosition)
            }
        }
    }
}

class ExerciseCardDiffCallback : DiffUtil.ItemCallback<ExerciseCard>() {
    override fun areItemsTheSame(oldItem: ExerciseCard, newItem: ExerciseCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ExerciseCard, newItem: ExerciseCard): Boolean {
        return oldItem == newItem
    }
}
