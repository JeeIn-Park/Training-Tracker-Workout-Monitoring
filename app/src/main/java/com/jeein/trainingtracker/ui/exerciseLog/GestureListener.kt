package com.jeein.trainingtracker.ui.exerciseLog

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import kotlin.math.abs

class GestureListenerMass(private val editText: EditText, private val increment: Float) : View.OnTouchListener {
    private var downX: Float = 0.0f
    private var downTime: Long = 0

    init {
        editText.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Record the initial position and time
                downX = event.x
                downTime = System.currentTimeMillis()
                return false // return false to allow other interactions like keyboard popup
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val deltaX = moveX - downX

                if (abs(deltaX) > 50) { // Check if the movement is long enough to be considered a swipe
                    val currentValue = editText.text.toString().toFloatOrNull() ?: 0f
                    if (deltaX > 0) {
                        editText.setText(String.format("%.2f", currentValue + increment))
                    } else {
                        editText.setText(String.format("%.2f", currentValue - increment))
                    }
                    downX = moveX // Update the initial position to the current to continue incrementing/decrementing
                }
                return true // Handle the event here
            }
            MotionEvent.ACTION_UP -> {
                val upTime = System.currentTimeMillis()
                // Check if the duration of the press indicates a tap
                if (upTime - downTime < 200 && abs(event.x - downX) < 10) {
                    // It's a tap, show the keyboard
                    editText.requestFocus()
                    return false
                }
                return true
            }
        }
        return false
    }
}

class GestureListenerRep(private val editText: EditText, private val increment: Int) : View.OnTouchListener {
    private var downX: Float = 0.0f
    private var downTime: Long = 0

    init {
        editText.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Record the initial position and time
                downX = event.x
                downTime = System.currentTimeMillis()
                return false // Allow other interactions like keyboard popup
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val deltaX = moveX - downX

                if (abs(deltaX) > 50) { // Check if the movement is long enough to be considered a swipe
                    val currentValue = editText.text.toString().toIntOrNull() ?: 0
                    if (deltaX > 0) {
                        editText.setText((currentValue + increment).toString())
                    } else {
                        editText.setText((currentValue - increment).toString())
                    }
                    downX = moveX // Update the initial position to the current to continue incrementing/decrementing
                }
                return true // Handle the event here
            }
            MotionEvent.ACTION_UP -> {
                val upTime = System.currentTimeMillis()
                // Check if the duration of the press indicates a tap
                if (upTime - downTime < 200 && abs(event.x - downX) < 10) {
                    // It's a tap, show the keyboard
                    editText.requestFocus()
                    return false
                }
                return true
            }
        }
        return false
    }
}
