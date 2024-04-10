package com.example.trainingtracker.ui.exerciseLog

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingtracker.R
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import java.lang.String.format
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class AddLogActivity : AppCompatActivity() {
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

        // binding
        setContentView(R.layout.activity_add_log)
        cardItem = intent.getSerializableExtra("EXTRA_CARD_ITEM") as ExerciseCard
        logStorage = LogStorage(cardItem.id)
        val pastLog: List<ExerciseLog> = logStorage.loadLogs(this)
        supportActionBar?.title = cardItem.name


        // Mid left

            // today's workout

//            // one rep max box
//        val boxView: View = findViewById(R.id.box)
//
//                //title
//        val titleTextView: TextView = boxView.findViewById(R.id.title)
//        titleTextView.text = getString(R.string.one_rep_max_pb)
//
//                //content
//        val contentTextView: TextView = boxView.findViewById(R.id.content)
//        if (pastLog.isEmpty()) {
//            contentTextView.text = "N/A"
//        } else {
//            contentTextView.text = "${String.format("%.2f", cardItem.oneRepMax).toString()} kg"
//        }
//
//                //date
//        // TODO : need to find the date of one rep max
//        // TODO : each set store one rep max
//        // TODO : each log store one rep max
//        // TODO : each card store one rep max date
//        val dateTextView: TextView = boxView.findViewById(R.id.date)
//        if ( cardItem.lastActivity != null ) {
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
//            val formattedDate = cardItem.lastActivity!!.format(formatter)
//            val currentDate = LocalDateTime.now()
//            val daysAgo = Duration.between(cardItem.lastActivity, currentDate).toDays()
//            if (daysAgo == 0.toLong()) {
//                dateTextView.text = formattedDate
//            } else if (daysAgo == 1.toLong()) {
//                dateTextView.text = "$formattedDate (1 day ago)"
//            } else {
//                val dateStringWithDaysAgo = "$formattedDate ($daysAgo days ago)"
//                dateTextView.text = dateStringWithDaysAgo
//            }
//        }
//


        // Mid Right

                // past logs
        val pastLogRecyclerView: RecyclerView = findViewById(R.id.pastRecords)
        pastLogRecyclerView.layoutManager = LinearLayoutManager(this)
        pastLogTableAdapter = PastLogTableAdapter(pastLog)
        pastLogRecyclerView.adapter = pastLogTableAdapter

                // graph



        // bottom
        val kgEditText: EditText = findViewById(R.id.kgEnterText)
        val repEditText: EditText = findViewById(R.id.repEnterText)
        val warmUpCheckBox: CheckBox = findViewById(R.id.warmUpCheck)
        val logButton: Button = findViewById(R.id.logButton)


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
            val setCountParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            setCountTextView.layoutParams = setCountParams

            kgAndRepTextView.text = "${massString} kg * ${repString}"
            val kgAndRepParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            kgAndRepTextView.layoutParams = kgAndRepParams

            setCountTextView.setPadding(16, 8, 16, 8)
            kgAndRepTextView.setPadding(16, 8, 16, 8)
            setCountTextView.gravity = Gravity.CENTER_VERTICAL
            kgAndRepTextView.gravity = Gravity.RIGHT
            setCountTextView.setBackgroundResource(R.drawable.style_textview_outline)

            tableRow.addView(setCountTextView)
            tableRow.addView(kgAndRepTextView)
            tableRow.setBackgroundResource(R.drawable.style_textview_outline)
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
            LogExercise.logExercise(
                this,
                exerciseSetList,
                cardItem,
                exerciseDate,
                logStorage)
        }
        super.onStop()
    }

}