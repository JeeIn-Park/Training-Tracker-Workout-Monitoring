package com.example.trainingtracker.ui.tag

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TagAdapter(private val context: Context, private val tags: List<String>) :
    ListAdapter<String, TagAdapter.TagViewHolder>(TagDiffCallback()) {
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TagViewHolder {
        // Create a new CardView programmatically
        val context: Context = viewGroup.context
        val cardView = CardView(context)
        cardView.setCardBackgroundColor(22)
        cardView.radius = 8f

        return TagViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
//        holder.itemView.setOnClickListener {
//            onItemClick(currentItem)
//        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tags.size

    fun addItem(tag: String) {
        val updatedList = currentList.toMutableList()
        updatedList.add(tag)
        submitList(updatedList)
        TagStorage.addTag(context, tag)
    }

    fun removeItem(position: Int) {
        val updatedList = currentList.toMutableList()
        val removedTag = updatedList.removeAt(position)
        submitList(updatedList)
        TagStorage.removeTag(context, removedTag)
    }


    inner class TagViewHolder(outerCardView: CardView) : RecyclerView.ViewHolder(outerCardView) {
        //  connect layout and bind it through id
        // add bind function

        private val innerCardView: CardView = CardView(outerCardView.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCardBackgroundColor(Color.WHITE)
            radius = 8f
            cardElevation = 8f
            setContentPadding(16, 16, 16, 16)
        }

        val textView: TextView = TextView(innerCardView.context).apply {
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
            innerCardView.addView(this)
        }
    }

}


class TagDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}