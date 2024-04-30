package com.jeein.trainingtracker.ui.exerciseLog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeein.trainingtracker.FormattedStringGetter
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.TableSetup
import com.jeein.trainingtracker.databinding.FragmentAddLogBinding
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.views.GraphViewAdapter


class AddLogFragment : Fragment() {

    private var _binding: FragmentAddLogBinding? = null
    private val binding get() = _binding!!

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
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLogBinding.inflate(inflater, container, false)

        cardItem = arguments?.getSerializable("exerciseCardArg") as ExerciseCard
        (requireActivity() as AppCompatActivity).supportActionBar?.title = cardItem.name
        logStorage = LogStorage(cardItem.id)
        val pastLog: List<ExerciseLog> = logStorage.loadLogs(requireContext())
        log = ExerciseLogFactory.createEmptyExerciseLog(cardItem)
        logStorage.addLog(requireContext(), log)


        val oneRepMaxBar: TextView = binding.oneRepMaxBar
        val oneRepMaxRecordDate = cardItem.oneRepMaxRecordDate
        if (oneRepMaxRecordDate != null) {
            oneRepMaxBar.text =
                FormattedStringGetter.oneRepMaxRecordWithDate(cardItem, log.dateTime)
        } else oneRepMaxBar.visibility = View.GONE


        // Mid Right

        // past logs
        val pastLogRecyclerView: RecyclerView = binding.pastRecords
        pastLogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        pastLogTableAdapter = PastLogTableAdapter(pastLog)
        pastLogRecyclerView.adapter = pastLogTableAdapter

        // graph
        val graphView = binding.graphView
        if (pastLog.size > 1) {
            GraphViewAdapter.setupGraphView(graphView, pastLog)
        } else graphView.visibility = View.GONE


        // bottom
        val kgEditText: EditText = binding.kgEnterText
        val repEditText: EditText = binding.repEnterText
        val warmUpCheckBox: CheckBox = binding.warmUpCheck
        val logButton: Button = binding.logButton


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
            log = logStorage.updateLog(requireContext(), log, exerciseSetList)

            val midLeftLayout = binding.midLeft
            val tableLayout = midLeftLayout.findViewById<TableLayout>(R.id.todaySetTable)
            tableLayout.addView(
                TableSetup.setKgRepTableRow(
                    requireContext(),
                    setNum,
                    massString,
                    repString
                )
            )

        }

        return binding.root
    }

    override fun onStop() {
        if (exerciseSetList.isNotEmpty()) {
            logStorage.updateLog(requireContext(), log, exerciseSetList)
        } else logStorage.removeLog(requireContext(), log)
        super.onStop()
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}