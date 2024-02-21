package com.example.trainingtracker

import androidx.recyclerview.widget.DiffUtil

class ExerciseCardDiffCallback : DiffUtil.ItemCallback<ExerciseCard>() {
    override fun areItemsTheSame(oldItem: ExerciseCard, newItem: ExerciseCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ExerciseCard, newItem: ExerciseCard): Boolean {
        return oldItem == newItem
    }
}