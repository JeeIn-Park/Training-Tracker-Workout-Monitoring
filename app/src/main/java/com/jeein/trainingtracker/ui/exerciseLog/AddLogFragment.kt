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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeein.trainingtracker.FormattedStringGetter
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.TableSetup
import com.jeein.trainingtracker.databinding.FragmentAddLogBinding
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.exerciseSet.ExerciseSetFactory
import com.jeein.trainingtracker.ui.exerciseSet.SetStorage
import com.jeein.trainingtracker.views.GraphViewAdapter


class AddLogFragment : Fragment() {

    private var _binding: FragmentAddLogBinding? = null
    private val binding get() = _binding!!

    private lateinit var pastLogTableAdapter: PastLogTableAdapter
    private lateinit var addLogViewModel: AddLogViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addLogViewModel =
            ViewModelProvider(this).get(AddLogViewModel::class.java)
        _binding = FragmentAddLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cardItem = arguments?.getSerializable("exerciseCardArg") as ExerciseCard
        val currentExercise = SetStorage.getCurrentExercise(requireContext())
        if ( (currentExercise != null) && (currentExercise != cardItem.id)) {
            SetStorage.resetSets(requireContext(), currentExercise)
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = cardItem.name
        val logStorage = LogStorage(cardItem.id)
        val pastLog: List<ExerciseLog> = logStorage.loadLogs(requireContext())
        println(pastLog)


        val oneRepMaxBar: TextView = binding.oneRepMaxBar
        val oneRepMaxRecordDate = cardItem.oneRepMaxRecordDate
        if (oneRepMaxRecordDate != null) {
            oneRepMaxBar.text =
                FormattedStringGetter.oneRepMaxRecordWithDate(cardItem, oneRepMaxRecordDate)
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
            val setNum: Int? = if (warmUpCheckBox.isChecked) {
                null
            } else {
                SetStorage.getCurrentSetNum(requireContext()) + 1
            }

            SetStorage.addSet(requireContext(), ExerciseSetFactory.createExerciseSet(cardItem, mass, setNum, rep))
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

        return root
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