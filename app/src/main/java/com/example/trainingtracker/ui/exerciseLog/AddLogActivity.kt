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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
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

    private val exerciseDate = LocalDateTime.now()
    private var exerciseSetList: MutableList<ExerciseSet> = mutableListOf()
    private var currentSetCount : Int = 0
    private lateinit var pastLogTableAdapter: PastLogTableAdapter

    // get selected card
    private lateinit var cardItem : ExerciseCard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pastLog : List<ExerciseLog> = LogStorage.loadLogs(this)

        // layout binding
        setContentView(R.layout.activity_add_log)
        cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard


            // bottom
        val kgEditText : EditText = findViewById(R.id.kgEnterText)
        val repEditText : EditText = findViewById(R.id.repEnterText)
        val warmUpCheckBox : CheckBox = findViewById(R.id.warmUpCheck)
        val logButton : Button = findViewById(R.id.logButton)

            // Mid left
        val pastLogRecyclerView: RecyclerView = findViewById(R.id.pastRecords)
        pastLogTableAdapter = PastLogTableAdapter(this, pastLog)
        pastLogRecyclerView.adapter = pastLogTableAdapter

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

        var setNum: Int?
        logButton.setOnClickListener{
            val dateTime = LocalDateTime.now()
            val massString = kgEditText.text.toString()
            val mass = massString.toFloatOrNull()
            val repString = repEditText.text.toString()
            val rep = repString.toIntOrNull()
            if (warmUpCheckBox.isChecked) {
                setNum = null
            } else {
                currentSetCount += 1
                setNum = currentSetCount
            }

            val set = ExerciseSet(
                dateTime = dateTime,
                exerciseCard = cardItem,
                mass = mass,
                set = setNum,
                rep = rep)
            exerciseSetList.add(set)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val log = ExerciseLog(
            dateTime = exerciseDate,
            exerciseCard = cardItem,
            exerciseSetList = exerciseSetList,
            totalSet = currentSetCount,
            totalWeight = null // TODO : implement this algorithm
        )
        LogStorage.addLog(this, log)
    }
}