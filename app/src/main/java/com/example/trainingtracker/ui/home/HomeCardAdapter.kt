package com.example.trainingtracker.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.CardStorage
import com.example.trainingtracker.ExerciseCard
import com.example.trainingtracker.ExerciseCardDiffCallback
import com.example.trainingtracker.R

class HomeCardAdapter(private val context: Context, private val onItemClick: (ExerciseCard) -> Unit) :
    ListAdapter<ExerciseCard, HomeCardAdapter.HomeCardViewHolder>(ExerciseCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.home_card_item, parent, false)
        return HomeCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeCardViewHolder, position: Int) {
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

    inner class HomeCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName: TextView = itemView.findViewById(R.id.ExerciseName)
        private val tag: TextView = itemView.findViewById(R.id.Tag)
        private val lastExercise: TextView = itemView.findViewById(R.id.LastExercise)
        private val mainMuscle: TextView = itemView.findViewById(R.id.MainMuscle)
        private val subMuscle: TextView = itemView.findViewById(R.id.SubMuscle)
        private val personalRecord: TextView = itemView.findViewById(R.id.PersonalRecord)

        // reference (each parts in a card layout)
        fun bind(cardItem: ExerciseCard) {
            exerciseName.text = if (cardItem.name != null) {
                cardItem.name.toString()
            } else {
                "N/A"
            }

            tag.text = cardItem.tag.toString()

            lastExercise.text = cardItem.lastActivity.toString()

            mainMuscle.text = if (cardItem.mainMuscles != null) {
                "Main muscle : ${cardItem.mainMuscles.toString()}"
            } else {
                "Main muscle : N/A"
            }

            subMuscle.text = if (cardItem.subMuscles != null) {
                "Sub muscle :${cardItem.subMuscles.toString()}"
            } else {
                "Sub muscle : N/A"
            }

            personalRecord.text = "place holder"// need to check the whole cards
        }
    }

}

