package com.jeein.trainingtracker.ui.exerciseLog

import androidx.recyclerview.widget.DiffUtil

class ExerciseLogDiffCallback : DiffUtil.ItemCallback<ExerciseLog>() {
    override fun areItemsTheSame(oldItem: ExerciseLog, newItem: ExerciseLog): Boolean {
        return oldItem.exerciseSetList == newItem.exerciseSetList
    }

    override fun areContentsTheSame(oldItem: ExerciseLog, newItem: ExerciseLog): Boolean {
        return oldItem == newItem
    }
}