package com.jeein.trainingtracker.views

import com.jeein.trainingtracker.ui.exerciseLog.ExerciseLog
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

object GraphViewAdapter {
    fun setupGraphView(graph: GraphView, logs: List<ExerciseLog>) {
        // Reverse the list so that it goes from oldest to most recent and take the last 8 entries
        val relevantLogs = logs.reversed().take(8)

        val series = LineGraphSeries<DataPoint>()
        relevantLogs.mapIndexed { index, log ->  // Use index as x-value
            if (log.oneRepMax != null) {
                val oneRepMax = log.oneRepMax ?: 0F
                DataPoint(index.toDouble(), oneRepMax.toDouble())
            } else {
                null
            }
        }.filterNotNull().forEach { series.appendData(it, true, relevantLogs.size) }

        graph.addSeries(series)
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)  // Start at 0 since we are only showing up to 8 points
        graph.viewport.setMaxX(relevantLogs.size - 1.toDouble())  // Set max to the size of relevantLogs minus one for proper indexing
        graph.viewport.isScalable =
            false  // You may want to disable scaling to keep the view fixed on these 8 points
        graph.viewport.isScrollable =
            false  // You may want to disable scrolling to keep the view fixed

        // Custom label formatter to display month and day
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                if (isValueX) {
                    val index = value.toInt()
                    if (index < relevantLogs.size) {
                        val dateTime = relevantLogs[index].dateTime
                        return "${dateTime.dayOfMonth}/${dateTime.monthValue}"  // Format as "Day/Month"
                    }
                }
                return super.formatLabel(value, isValueX)
            }
        }

        graph.gridLabelRenderer.numHorizontalLabels = relevantLogs.size  // One label for each point
    }
}