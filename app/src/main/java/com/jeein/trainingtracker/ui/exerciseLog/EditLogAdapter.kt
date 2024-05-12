package com.jeein.trainingtracker.ui.exerciseLog


import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeein.trainingtracker.FormattedStringGetter
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.ui.exerciseLog.LogStorage
import java.time.LocalDateTime


class EditLogAdapter(
    private val context: Context,
    private val logStorage: LogStorage,
    private val navigateToEdit: (ExerciseLog) -> Unit
) : ListAdapter<ExerciseLog, EditLogAdapter.LogViewHolder>(ExerciseLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    fun removeItem(position: Int) {
        val updatedList = currentList.toMutableList()
        val removedLog = updatedList.removeAt(position)
        logStorage.removeLog(context, removedLog)
        submitList(updatedList)
    }

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName: TextView = itemView.findViewById(R.id.ExerciseName)
        private val tag: TextView = itemView.findViewById(R.id.Tag)
        private val lastExercise: TextView = itemView.findViewById(R.id.LastExercise)
        private val mainMuscle: TextView = itemView.findViewById(R.id.MainMuscle)
        private val subMuscle: TextView = itemView.findViewById(R.id.SubMuscle)
        private val personalRecord: TextView = itemView.findViewById(R.id.PersonalRecord)

        fun bind(logItem: ExerciseLog) {
//            if (logItem.name == "") {
//                exerciseName.text = "N/A"
//                exerciseName.setTypeface(null, Typeface.BOLD)
//            } else {
//                exerciseName.text = logItem.name
//                exerciseName.setTypeface(null, Typeface.BOLD)
//            }
//
//            if (logItem.tag.isNotEmpty()) {
//                tag.text = FormattedStringGetter.tags(logItem.tag)
//            } else tag.text = ""
//
//            // Create a local immutable copy of lastActivity
//            val lastActivity = logItem.lastActivity
//
//            if (lastActivity != null) {
//                lastExercise.text =
//                    FormattedStringGetter.dateTimeWithDiff(lastActivity, LocalDateTime.now())
//            } else {
//                lastExercise.visibility = View.GONE
//            }
//
//            if (logItem.mainMuscles.isEmpty()) {
//                mainMuscle.visibility = View.GONE
//            } else {
//                mainMuscle.text = FormattedStringGetter.mainMuscles(logItem.mainMuscles)
//            }
//
//            if (logItem.subMuscles.isEmpty()) {
//                subMuscle.visibility = View.GONE
//            } else {
//                subMuscle.text = FormattedStringGetter.subMuscles(logItem.subMuscles)
//            }
//
//
//            val oneRepMaxRecordDate = logItem.oneRepMaxRecordDate
//            if (oneRepMaxRecordDate != null) {
//                personalRecord.text = FormattedStringGetter.oneRepMaxRecordWithDate_ShortPB(
//                    logItem,
//                    LocalDateTime.now()
//                )
//            } else {
//                personalRecord.visibility = View.GONE
//            }

            itemView.setOnLongClickListener {
                showEditDeleteOptions(logItem)
                true
            }

        }

        private fun showEditDeleteOptions(logItem: ExerciseLog) {
            val options = arrayOf("Edit", "Delete")
            AlertDialog.Builder(context)
                .setItems(options) { dialog, which ->
                    when (which) {
                        // Edit
                        0 -> {
                            navigateToEdit(logItem)
                        }
                        // Delete
                        1 -> {
                            showDeleteWarning(logItem)
                        }
                    }
                    dialog.dismiss()
                }
                .show()
        }

        private fun showDeleteWarning(logItem: ExerciseLog) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.general_deletion_warning))
                .setMessage(context.getString(R.string.exercise_deletion_warning))
                .setPositiveButton("Delete") { dialog, which ->
                    logStorage.deleteLogs(context)
                    removeItem(adapterPosition)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}

