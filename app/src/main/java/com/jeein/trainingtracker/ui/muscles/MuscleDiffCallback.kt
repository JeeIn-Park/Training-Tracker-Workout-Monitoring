package com.jeein.trainingtracker.ui.muscles

import androidx.recyclerview.widget.DiffUtil

class MuscleDiffCallback : DiffUtil.ItemCallback<Muscle>(){
    override fun areItemsTheSame(oldItem: Muscle, newItem: Muscle): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Muscle, newItem: Muscle): Boolean {
        return oldItem == newItem
    }
}