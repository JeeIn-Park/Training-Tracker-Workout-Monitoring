package com.example.trainingtracker.ui.muscles

import java.time.LocalDateTime
import java.util.UUID

data class Muscle(
    var lastActivity: LocalDateTime?,
    var status : Int,
    val name : String,
    val layout : List<String>,
) {
    val RECOVERED = 0
    val RECOVERING = 1
    val NEED_EXERCISE = 2

    fun updateLastActivity(newDate: LocalDateTime) {
        lastActivity = newDate
    }

    fun updateStatus(newStatus: Int) {
        status = newStatus
    }

}


