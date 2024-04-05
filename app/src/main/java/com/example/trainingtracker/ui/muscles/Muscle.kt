package com.example.trainingtracker.ui.muscles

import java.time.LocalDateTime
import java.util.UUID

data class Muscle(
    val id : UUID,
    var lastActivity: LocalDateTime?,
    var status : Int,
    val name : String,
    val layout : List<String>,
) {

    fun updateLastActivity(newDate: LocalDateTime) {
        lastActivity = newDate
    }

    fun updateStatus(newStatus: Int) {
        status = newStatus
    }

}


