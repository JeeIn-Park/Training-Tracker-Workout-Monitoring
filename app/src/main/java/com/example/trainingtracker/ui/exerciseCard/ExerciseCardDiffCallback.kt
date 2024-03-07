package com.example.trainingtracker.ui.exerciseCard

import androidx.recyclerview.widget.DiffUtil
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard

class ExerciseCardDiffCallback : DiffUtil.ItemCallback<ExerciseCard>() {
    override fun areItemsTheSame(oldItem: ExerciseCard, newItem: ExerciseCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ExerciseCard, newItem: ExerciseCard): Boolean {
        return oldItem == newItem
    }
}