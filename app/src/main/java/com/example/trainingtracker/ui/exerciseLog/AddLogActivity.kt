package com.example.trainingtracker.ui.exerciseLog

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.TableLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import com.example.trainingtracker.FormattedStringGetter
import com.example.trainingtracker.TableSetup
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.example.trainingtracker.views.GraphViewAdapter.setupGraphView
import com.jjoe64.graphview.GraphView


class AddLogActivity : AppCompatActivity() {
    // past log
    private lateinit var pastLogTableAdapter: PastLogTableAdapter
    private lateinit var log: ExerciseLog
    private var exerciseSetList: MutableList<ExerciseSet> = mutableListOf()
    private var currentSetCount: Int = 0

    // get selected card
    private lateinit var cardItem: ExerciseCard
    private lateinit var logStorage: LogStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding
        setContentView(R.layout.activity_add_log)
        cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard
        logStorage = LogStorage(cardItem.id)
        val pastLog: List<ExerciseLog> = logStorage.loadLogs(this)
        log = ExerciseLogFactory.createEmptyExerciseLog(cardItem)
        logStorage.addLog(this, log)
        supportActionBar?.title = cardItem.name

        val oneRepMaxBar: TextView = findViewById(R.id.oneRepMaxBar)
        val oneRepMaxRecordDate = cardItem.oneRepMaxRecordDate
        if (oneRepMaxRecordDate != null) {
            oneRepMaxBar.text = FormattedStringGetter.oneRepMaxRecordWithDate(cardItem, log.dateTime)
        } else oneRepMaxBar.visibility = View.GONE


        // Mid Right

                // past logs
        val pastLogRecyclerView: RecyclerView = findViewById(R.id.pastRecords)
        pastLogRecyclerView.layoutManager = LinearLayoutManager(this)
        pastLogTableAdapter = PastLogTableAdapter(pastLog)
        pastLogRecyclerView.adapter = pastLogTableAdapter

                // graph
        val graphView = findViewById<GraphView>(R.id.graphView)
        if (pastLog.size > 1) {
            setupGraphView(graphView, pastLog)
        } else graphView.visibility = View.GONE


        // bottom
        val kgEditText: EditText = findViewById(R.id.kgEnterText)
        val repEditText: EditText = findViewById(R.id.repEnterText)
        val warmUpCheckBox: CheckBox = findViewById(R.id.warmUpCheck)
        val logButton: Button = findViewById(R.id.logButton)


        logButton.setOnClickListener {
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

            exerciseSetList.add(ExerciseSetFactory.createExerciseSet(cardItem, mass, setNum, rep))
            log = logStorage.updateLog(this, log, exerciseSetList)

            val midLeftLayout = findViewById<ConstraintLayout>(R.id.midLeft)
            val tableLayout = midLeftLayout.findViewById<TableLayout>(R.id.todaySetTable)
            tableLayout.addView(TableSetup.setKgRepTableRow(this, setNum, massString, repString))

        }
    }

    override fun onStop() {
        if (exerciseSetList.isNotEmpty()) {
            logStorage.updateLog(this, log, exerciseSetList)
        } else logStorage.removeLog(this, log)
        super.onStop()
    }

}