package com.example.trainingtracker.ui.exerciseLog

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.example.trainingtracker.R
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class AddLogActivity : AppCompatActivity(){

    // check box
    private lateinit var warmUpCheckBox : CheckBox

    // graph
    private val mHandler = Handler(Looper.getMainLooper())
    private lateinit var mTimer1: Runnable
    private lateinit var mSeries1: LineGraphSeries<DataPoint>
    private var graph2LastXValue = 5.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // layout binding
        setContentView(R.layout.activity_add_log)

        // get selected card
        val cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard

        // title with selected card
        supportActionBar?.title = cardItem.name



        val boxView1: View = findViewById(R.id.box1)
        val titleTextView1: TextView = boxView1.findViewById(R.id.title)
        val title1 = "MAX KG"
        titleTextView1.text = title1

        val boxView2: View = findViewById(R.id.box2)
        val titleTextView2: TextView = boxView2.findViewById(R.id.title)
        val title2 = "MEAN REPS"
        titleTextView2.text = title2





        var setCount : Int = 0
        var set: Int?

        val kgEditText : EditText = findViewById(R.id.kgEnterText)
        val repEditText : EditText = findViewById(R.id.repEnterText)
        val warmUpCheckBox : CheckBox = findViewById(R.id.warmUpCheck)
        val logButton : Button = findViewById(R.id.logButton)

        logButton.setOnClickListener{
            val dateTime = LocalDateTime.now()
            val massString = kgEditText.text.toString()
            val mass = massString.toFloatOrNull()
            val repString = repEditText.text.toString()
            val rep = repString.toIntOrNull()
            if (warmUpCheckBox.isChecked) {
                set = null
            } else {
                setCount += 1
                set = setCount
            }
            val log = ExerciseSet(dateTime = dateTime, exerciseCard = cardItem, mass = mass, set = set, rep = rep) // Create a new card
            LogStorage.addLog(this, log)
        }
    }

}