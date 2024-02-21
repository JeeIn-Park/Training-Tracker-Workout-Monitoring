package com.example.trainingtracker.ui.Home

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

//TODO : this card adapter is the one especially for the status, there will be another for the status and I think I need to put it in different package
class HomeCardAdapter(private val context: Context) :
    ListAdapter<ExerciseCard, HomeCardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.home_card_item, parent, false)
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
        private val logButton: Button = itemView.findViewById(R.id.logButton)

        // reference (each parts in a card layout)
        fun bind(cardItem: ExerciseCard) {
            textView.text = "${cardItem.name} - Muscles: ${cardItem.mainMuscles.joinToString(", ")}"
            logButton.setOnClickListener {
                removeItem(adapterPosition)
                // TODO : log, not delete the card item
            }
        }
    }
}

