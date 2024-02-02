package com.example.trainingtracker


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CardAdapter (private val context: Context) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

        private val cardList: MutableList<ExerciseCard> = CardStorage.loadCards(context).toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardAdapter.CardViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun addItem(cardItem: ExerciseCard) {
        cardList.add(cardItem)
        notifyDataSetChanged()
        CardStorage.addCard(context, cardItem)
    }

    fun removeItem(position: Int) {
        val removedCard = cardList.removeAt(position)
        notifyDataSetChanged()
        CardStorage.removeCard(context, removedCard)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView : TextView = itemView.findViewById(R.id.textView)
        private val deleteButton : Button = itemView.findViewById(R.id.deleteButton)

        fun bind(cardItem: ExerciseCard) {
            textView.text = "${cardItem.name} - Muscles: ${cardItem.mainMuscles.joinToString(", ")}"
            deleteButton.setOnClickListener {
                removeItem(adapterPosition)
            }
        }

    }

    }