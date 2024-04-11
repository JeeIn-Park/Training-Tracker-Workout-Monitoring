package com.example.trainingtracker.ui.tag

import com.example.trainingtracker.R
import java.time.LocalDateTime
import java.io.Serializable
import java.util.UUID

data class Tag(
    val id: UUID,
    val timeAdded: LocalDateTime = LocalDateTime.now(),
    val name: String = "",
    var isSelected: Boolean = false
): Serializable {
    companion object {
        val ADD_TAG = Tag(
            id = UUID.randomUUID(),
            name = "+",
            isSelected = false
        )

        val SELECT_TAG = Tag(
            id = UUID.randomUUID(),
            name = "Select Tag",
            isSelected = false
        )
    }
}