package com.example.trainingtracker.ui.muscles

import java.io.Serializable
import java.time.LocalDateTime

data class Muscle(
    var lastActivity: LocalDateTime?,
    var status: Int,
    val name: String,
    val layout: List<String>,
) : Serializable {
    fun updateLastActivity(newDate: LocalDateTime) {
        lastActivity = newDate
    }

    fun updateStatus(newStatus: Int) {
        status = newStatus
    }
}
