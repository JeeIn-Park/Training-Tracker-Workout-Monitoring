package com.example.trainingtracker.ui.tag

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TagAdapter(private val tags: List<String>) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TagViewHolder {
        // Create a new CardView programmatically
        val context: Context = viewGroup.context
        val cardView = CardView(context)
        cardView.setCardBackgroundColor(22)
        cardView.radius = 8f

        return TagViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: TagViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = tags[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tags.size
    class TagViewHolder(outerCardView: CardView) : RecyclerView.ViewHolder(outerCardView) {
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