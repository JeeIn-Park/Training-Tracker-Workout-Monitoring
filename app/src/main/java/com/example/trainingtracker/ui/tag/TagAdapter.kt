package com.example.trainingtracker.ui.tag

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R

class TagAdapter(private val context: Context, private val onItemClick: (Tag) -> Unit) :
    ListAdapter<Tag, TagAdapter.TagViewHolder>(TagDiffCallback()) {
    // Create new views (invoked by the layout manager)

    private var selectedTags: MutableList<Tag> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, position)
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    fun addItem(tag: Tag) {
        val updatedList = currentList.toMutableList()
        updatedList.add(tag)
        submitList(updatedList)
        TagStorage.addTag(context, tag)
    }

    fun removeItem(position: Int) {
        val updatedList = currentList.toMutableList()
        val removedTag = updatedList.removeAt(position)
        TagStorage.removeTag(context, removedTag)
        submitList(updatedList)
    }


    fun editItem(oldTag: Tag, newTag: Tag) {
        val updatedList = currentList.toMutableList()
        val index = updatedList.indexOfFirst { it == oldTag }
        if (index != -1) {
            updatedList[index] = newTag
        }
        TagStorage.editTag(context, oldTag, newTag)
        submitList(updatedList)
    }

    fun setSelectedTags(tags: List<Tag>) {
        selectedTags.clear()
        selectedTags.addAll(tags)
        notifyDataSetChanged()
    }

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        private val tagName : TextView = itemView.findViewById(R.id.tagName)

        fun bind(tag : Tag, position: Int) {
            tagName.text = tag.name
            if (selectedTags.contains(tag)) {
                // Change background color for selected tag
                itemView.setBackgroundColor(context.getColor(R.color.tag_color_selector))
            } else {
                // Reset background color for unselected tag
                itemView.setBackgroundColor(context.getColor(R.color.tag_color_selector))
            }

            if (tag != Tag.ADD_TAG) {
                itemView.setOnLongClickListener {
                    showEditDeleteOptions(tag, position)
                    true // Consume the long click
                }
                itemView.setOnClickListener {
                    onItemClick(tag) // TODO : what does it do?
                }
            }
        }

        private fun showEditDeleteOptions(tag: Tag, position: Int) {
            val options = arrayOf("Edit", "Delete")
            AlertDialog.Builder(context)
                .setItems(options) { dialog, which ->
                    when (which) {
                        // Edit
                        0 -> {
                            val inputDialog = AlertDialog.Builder(context)
                            val inputEditText = EditText(context)
                            inputEditText.setText(tag.name)
                            inputDialog.setView(inputEditText)
                            inputDialog.setTitle(context.getString(R.string.tag_enter_name))

                            inputDialog.setPositiveButton("OK") { dialog, _ ->
                                val newTagString = inputEditText.text.toString().trim()
                                if (newTagString.isNotEmpty()) {
                                    val newTag = Tag(
                                        id = tag.id,
                                        timeAdded = tag.timeAdded,
                                        name = newTagString)

                                    editItem(tag, newTag)
                                }
                                dialog.dismiss()
                            }

                            inputDialog.setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }

                            inputDialog.show()
                        }
                        // Delete
                        1 -> {
                            showDeleteWarning(tag, position)
                        }
                    }
                    dialog.dismiss()
                }
                .show()
        }

        private fun showDeleteWarning(tag: Tag, position: Int) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.general_deletion_warning))
                .setMessage(context.getString(R.string.tag_deletion_warning))
                .setPositiveButton("Delete") { dialog, which ->
                    removeItem(position)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }


}
