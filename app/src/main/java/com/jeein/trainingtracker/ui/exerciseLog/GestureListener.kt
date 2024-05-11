package com.jeein.trainingtracker.ui.exerciseLog

import android.view.MotionEvent
import android.view.View
import android.widget.EditText

class GestureListener(private val editText: EditText, private val increment: Float) : View.OnTouchListener {
    private var initialX: Float = 0.0f

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Record the position where touch was started
                initialX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                // Calculate the distance moved
                val currentX = event.x
                val deltaX = currentX - initialX

                // Adjust the value based on the direction of the swipe
                val currentValue = editText.text.toString().toFloatOrNull() ?: 0f
                if (Math.abs(deltaX) > 50) { // Ensure a minimum swipe distance
                    if (deltaX > 0) {
                        editText.setText(String.format("%.2f", currentValue + increment))
                    } else {
                        editText.setText(String.format("%.2f", currentValue - increment))
                    }
                    initialX = currentX
                }
                return true
            }
        }
        return false
    }
}
