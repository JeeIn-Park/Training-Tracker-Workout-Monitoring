package com.example.trainingtracker.ui.tag

import java.time.LocalDateTime
import java.io.Serializable
import java.util.UUID

data class Tag(
    val id: UUID = UUID.randomUUID(),
    val timeAdded: LocalDateTime,
    val name: String,
): Serializable {
    companion object {
        val ADD_TAG = Tag(
            id = UUID.randomUUID(),
            timeAdded = LocalDateTime.now(),
            name = "+"
        )
    }
}