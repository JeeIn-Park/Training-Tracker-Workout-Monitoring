package com.jeein.trainingtracker.ui.exerciseLog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeein.trainingtracker.Event
import com.jeein.trainingtracker.EventManager
import com.jeein.trainingtracker.FormattedStringGetter
import com.jeein.trainingtracker.R
import com.jeein.trainingtracker.databinding.FragmentAddLogBinding
import com.jeein.trainingtracker.ui.exerciseCard.ExerciseCard
import com.jeein.trainingtracker.ui.exerciseSet.ExerciseSet
import com.jeein.trainingtracker.ui.exerciseSet.ExerciseSetFactory
import com.jeein.trainingtracker.ui.exerciseSet.SetStorage
import com.jeein.trainingtracker.views.GraphViewAdapter


@Suppress("DEPRECATION")
class AddLogFragment : Fragment() {

    private var _binding: FragmentAddLogBinding? = null
    private val binding get() = _binding!!

    private lateinit var pastLogTableAdapter: PastLogTableAdapter
    private lateinit var todaySetTableAdapter: TodaySetTableAdapter
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
            ViewModelProvider(this)[AddLogViewModel::class.java]
        _binding = FragmentAddLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cardItem = arguments?.getSerializable("exerciseCardArg") as ExerciseCard
        val currentExercise = SetStorage.getCurrentExercise(requireContext())
        if ((currentExercise != null) && (currentExercise != cardItem.id)) {
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


        // today sets
        val currentSet = SetStorage.getSets(requireContext())

        val todayOneRepMaxTextViewBinding: TextView = binding.AddLogToday1RMTextView
        todayOneRepMaxTextViewBinding.text = FormattedStringGetter.totalMassLifted(currentSet)

        val todaySetRecyclerView: RecyclerView = binding.todaySetTable
        todaySetRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        todaySetTableAdapter = TodaySetTableAdapter(currentSet)
        todaySetRecyclerView.adapter = todaySetTableAdapter
        todaySetRecyclerView.itemAnimator = DefaultItemAnimator()
        addLogViewModel.updateSetRecyclerViewData(currentSet)
        addLogViewModel.setRecyclerViewData.observe(viewLifecycleOwner) { newData ->
            todaySetTableAdapter.updateData(newData)
        }

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

            SetStorage.addSet(
                requireContext(),
                ExerciseSetFactory.createExerciseSet(
                    cardItem,
                    mass,
                    setNum,
                    rep
                )
            )
            addLogViewModel.updateSetRecyclerViewData(SetStorage.getSets(requireContext()))

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventManager.subscribe(
            requireContext().getString(R.string.event_add_set),
            ::addSetSubscriber
        )
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

    override fun onStop() {
        super.onStop()
        EventManager.unsubscribe(requireContext().getString(R.string.event_add_set),
            ::addSetSubscriber
            )
    }

    private fun addSetSubscriber(event: Event) {
        (event.data as? List<*>)?.let { list ->
            if (list.all { it is ExerciseSet }) {
                val sets = list as List<ExerciseSet>
                val todayOneRepMaxTextViewBinding: TextView = binding.AddLogToday1RMTextView
                todayOneRepMaxTextViewBinding.text = FormattedStringGetter.totalMassLifted(sets)
            } else {
                println("Unhandled type of data: ${event.data}")
            }
        }
    }

}