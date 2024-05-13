package com.jeein.trainingtracker.ui.tag

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Tag(
    val id: UUID,
    val timeAdded: LocalDateTime = LocalDateTime.now(),
    val name: String = "",
    var isSelected: Boolean = false
) : Serializable {
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