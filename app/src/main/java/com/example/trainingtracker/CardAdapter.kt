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
import androidx.viewpager2.widget.ViewPager2

//TODO : this card adapter is the one especially for the home, there will be another for the status and I think I need to put it in different package
class CardAdapter(private val context: Context) :
    ListAdapter<ExerciseCard, CardAdapter.CardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_container, parent, false)
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
        private val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        private val logButton: Button = itemView.findViewById(R.id.logButton)

        // reference (each parts in a card layout)
        fun bind(cardItem: ExerciseCard) {
            exerciseName.text = cardItem.name
            logButton.setOnClickListener {
                removeItem(adapterPosition)
                //TODO : this need to add database the log user entered
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
