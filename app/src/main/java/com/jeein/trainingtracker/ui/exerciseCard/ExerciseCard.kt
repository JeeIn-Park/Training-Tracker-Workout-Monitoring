package com.jeein.trainingtracker.ui.exerciseCard

import com.jeein.trainingtracker.ui.muscles.Muscle
import com.jeein.trainingtracker.ui.tag.Tag
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class ExerciseCard(
    val id: UUID,
    val timeAdded: LocalDateTime = LocalDateTime.now(),
    var lastActivity: LocalDateTime? = null,
    var name: String,
    var mainMuscles: List<Muscle> = emptyList(),
    var subMuscles: List<Muscle> = emptyList(),
    var tag: List<Tag> = emptyList(),
    var oneRepMaxRecord: Float? = null,
    var oneRepMaxRecordDate: LocalDateTime? = null,
) : Serializable