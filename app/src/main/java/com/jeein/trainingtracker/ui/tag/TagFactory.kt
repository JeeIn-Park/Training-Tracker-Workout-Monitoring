package com.jeein.trainingtracker.ui.tag

import android.content.Context
import java.util.UUID

object TagFactory {

    fun createTag(context: Context, name: String): Tag {
        val id = generateUniqueId(context)
        return Tag(id = id, name = name)
    }

    fun editTag(originalTag: Tag, name: String): Tag {
        return Tag(
            id = originalTag.id,
            timeAdded = originalTag.timeAdded,
            name = name
        )
    }

    fun clickTag(clickedTag: Tag): Tag {
        return Tag(
            id = clickedTag.id,
            timeAdded = clickedTag.timeAdded,
            name = clickedTag.name,
            isSelected = !clickedTag.isSelected
        )
    }

    fun selectedTagOf(tag: Tag): Tag{
        return Tag(
            id = tag.id,
            timeAdded = tag.timeAdded,
            name = tag.name,
            isSelected = true
        )
    }

    fun resetSelection(context: Context) {
        val tags = TagStorage.loadTags(context)
        val updatedTags: MutableList<Tag> = mutableListOf()
        for (tag in tags) {
            updatedTags.add(
                Tag(
                    id = tag.id,
                    timeAdded = tag.timeAdded,
                    name = tag.name,
                    isSelected = false
                )
            )
        }
        if (tags.size != updatedTags.size) {
            throw error("error while resetting tag selections") // TODO : check if this throws error
        } else TagStorage.saveTags(context, updatedTags)
    }


    private fun generateUniqueId(context: Context): UUID {
        var uniqueId: UUID
        do {
            uniqueId = UUID.randomUUID()
        } while (TagStorage.isIdInUse(context, uniqueId))
        return uniqueId
    }
}