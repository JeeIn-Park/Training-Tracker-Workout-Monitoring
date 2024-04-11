package com.example.trainingtracker.ui.tag

import android.content.Context
import java.util.UUID

object TagFactory {

    fun createTag(context: Context, name: String): Tag{
        val id = generateUniqueId(context)
        return Tag(id = id, name = name)
    }

    fun clickTag(clickedTag: Tag): Tag {
        return Tag(
            id = clickedTag.id,
            timeAdded = clickedTag.timeAdded,
            name = clickedTag.name,
            isSelected = !clickedTag.isSelected
        )
    }

    private fun generateUniqueId(context: Context): UUID {
        var uniqueId: UUID
        do {
            uniqueId = UUID.randomUUID()
        } while (TagStorage.isIdInUse(context, uniqueId))
        return uniqueId
    }
}