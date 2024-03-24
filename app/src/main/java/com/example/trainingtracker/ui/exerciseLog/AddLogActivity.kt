package com.example.trainingtracker.ui.exerciseLog

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class AddLogActivity : AppCompatActivity() {
    // check box
    private lateinit var warmUpCheckBox: CheckBox

    // graph
    private val mHandler = Handler(Looper.getMainLooper())
    private lateinit var mTimer1: Runnable
    private lateinit var mSeries1: LineGraphSeries<DataPoint>
    private var graph2LastXValue = 5.0

    // past log
    private lateinit var pastLogTableAdapter: PastLogTableAdapter
    private var exerciseSetList: MutableList<ExerciseSet> = mutableListOf()
    private val exerciseDate = LocalDateTime.now()
    private var currentSetCount: Int = 0

    // get selected card
    private lateinit var cardItem: ExerciseCard
    private lateinit var logStorage: LogStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        println(pastLog)

        // layout binding
        setContentView(R.layout.activity_add_log)
        cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard
        logStorage = LogStorage(cardItem.id)
        val pastLog: List<ExerciseLog> = logStorage.loadLogs(this)


        // bottom
        val kgEditText: EditText = findViewById(R.id.kgEnterText)
        val repEditText: EditText = findViewById(R.id.repEnterText)
        val warmUpCheckBox: CheckBox = findViewById(R.id.warmUpCheck)
        val logButton: Button = findViewById(R.id.logButton)

        // Mid left
        val pastLogRecyclerView: RecyclerView = findViewById(R.id.pastRecords)
        pastLogRecyclerView.layoutManager = LinearLayoutManager(this)
        pastLogTableAdapter = PastLogTableAdapter(pastLog)
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
        logButton.setOnClickListener {
            val dateTime = LocalDateTime.now()
            val massString = kgEditText.text.toString()
            val mass = massString.toFloatOrNull()
            val repString = repEditText.text.toString()
            val rep = repString.toIntOrNull()
            val setNum: Int?
            if (warmUpCheckBox.isChecked) {
                setNum = null
            } else {
                currentSetCount += 1
                setNum = currentSetCount
            }

            val set = ExerciseSet(
                dateTime = dateTime,
                exerciseCard = cardItem.id,
                mass = mass,
                set = setNum,
                rep = rep
            )
            exerciseSetList.add(set)


            val midLeftLayout = findViewById<ConstraintLayout>(R.id.midLeft)
            val tableLayout = midLeftLayout.findViewById<TableLayout>(R.id.todaySetTable)

            // Create a new TableRow
            val tableRow = TableRow(this)
            val setCountTextView = TextView(this)
            val kgAndRepTextView = TextView(this)

            setCountTextView.text = if (setNum != null) {
                "${setNum?.toString()} set"
            } else { "" }
            kgAndRepTextView.text = "${massString} kg * ${repString}"

            setCountTextView.setPadding(16, 8, 16, 8)
            kgAndRepTextView.setPadding(16, 8, 16, 8)
            setCountTextView.gravity = Gravity.CENTER_VERTICAL
            kgAndRepTextView.gravity = Gravity.CENTER_VERTICAL
            setCountTextView.setBackgroundResource(R.drawable.style_textview_outline)
            kgAndRepTextView.setBackgroundResource(R.drawable.style_textview_outline)

            tableRow.addView(setCountTextView)
            tableRow.addView(kgAndRepTextView)
            tableLayout.addView(tableRow)
        }

        // todo :
        // date - today
        // kg * reps
    }
//    override fun onBackPressed() {
//        saveLog()
//        super.onBackPressed()
//    }

    override fun onStop() {
        if (exerciseSetList.isNotEmpty()) {
            saveLog()
        }
        super.onStop()
    }

    private fun saveLog() {
        val updatedCard = ExerciseCard(
            id = cardItem.id,
            lastActivity = exerciseDate,
            timeAdded = cardItem.timeAdded,
            name = cardItem.name,
            mainMuscles = cardItem.mainMuscles,
            subMuscles = cardItem.subMuscles,
            tag = cardItem.tag
        )
        CardStorage.editCard(this, cardItem, updatedCard)

        val log = ExerciseLog(
            dateTime = exerciseDate,
            exerciseCard = cardItem.id,
            exerciseSetList = exerciseSetList,
            totalSet = currentSetCount,
            totalWeight = null // TODO : implement this algorithm
        )
        logStorage.addLog(this, log)

        println(updatedCard)
    }

}