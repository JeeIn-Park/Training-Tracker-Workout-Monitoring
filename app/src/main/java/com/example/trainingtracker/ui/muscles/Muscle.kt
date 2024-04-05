package com.example.trainingtracker.ui.muscles

import java.time.LocalDateTime

data class Muscle(
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


