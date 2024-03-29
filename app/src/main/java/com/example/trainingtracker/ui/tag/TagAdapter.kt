package com.example.trainingtracker.ui.tag

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R

class TagAdapter(private val context: Context, private val onItemClick: (String) -> Unit) :
    ListAdapter<String, TagAdapter.TagViewHolder>(TagDiffCallback()) {
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

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


    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        private val tagName : TextView = itemView.findViewById(R.id.tagName)

        fun bind(tagString : String) {
            tagName.text = tagString
        }
    }

}