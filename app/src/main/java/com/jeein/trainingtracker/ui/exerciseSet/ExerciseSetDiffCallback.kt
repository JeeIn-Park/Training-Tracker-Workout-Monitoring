package com.jeein.trainingtracker.ui.exerciseSet

import androidx.recyclerview.widget.DiffUtil

class ExerciseSetDiffCallback: DiffUtil.ItemCallback<ExerciseSet>() {
    override fun areItemsTheSame(oldItem: ExerciseSet, newItem: ExerciseSet): Boolean {
        return ((oldItem.set == newItem.set) &&
                (oldItem.exerciseCard == newItem.exerciseCard))
    }

    override fun areContentsTheSame(oldItem: ExerciseSet, newItem: ExerciseSet): Boolean {
        return oldItem == newItem
    }
}