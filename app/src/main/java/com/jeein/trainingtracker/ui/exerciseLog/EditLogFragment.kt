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
import com.jeein.trainingtracker.databinding.FragmentEditLogBinding
import com.jeein.trainingtracker.ui.exerciseCard.CardStorage
import com.jeein.trainingtracker.ui.exerciseSet.ExerciseSet
import com.jeein.trainingtracker.ui.exerciseSet.ExerciseSetFactory
import com.jeein.trainingtracker.ui.exerciseSet.SetStorage
import java.time.format.DateTimeFormatter


@Suppress("DEPRECATION")
class EditLogFragment : Fragment() {

    private var _binding: FragmentEditLogBinding? = null
    private val binding get() = _binding!!

    private lateinit var editLogViewModel: EditLogViewModel
    private lateinit var logStorage: LogStorage
    private lateinit var pastLogAdapter: EditLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editLogViewModel =
            ViewModelProvider(this)[EditLogViewModel::class.java]
        _binding = FragmentEditLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val logItem = arguments?.getSerializable("exerciseLogArg") as ExerciseLog
        val cardItem = CardStorage.getCard(requireContext(), logItem.exerciseCard)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = cardItem.name

        logStorage = LogStorage(logItem.exerciseCard)

        val editLogDateDatePickerBinding = binding.EditLogDate
        editLogDateDatePickerBinding.updateDate(logItem.dateTime.year, logItem.dateTime.monthValue -1, logItem.dateTime.dayOfMonth)

        pastLogAdapter = EditLogAdapter(requireContext(), logStorage, )
        val editLogPastLogRecyclerViewBinding = binding.EditLogPastLog
        editLogPastLogRecyclerViewBinding.layoutManager = LinearLayoutManager(requireContext())
        editLogPastLogRecyclerViewBinding.adapter = pastLogAdapter
        editLogDateDatePickerBinding.itemAnimator = DefaultItemAnimator()
        editLogViewModel.setRecyclerViewData.observe(viewLifecycleOwner) {

        }

        val editLogSaveButtonBinding = binding.EditLogSaveButton

        // bottom
        val kgEditText: EditText = binding.kgEnterText
        val repEditText: EditText = binding.repEnterText
        val warmUpCheckBox: CheckBox = binding.warmUpCheck
        val logButton: Button = binding.logButton

        if (pastLog.isNotEmpty()){
            kgEditText.setText(
                pastLog[0].exerciseSetList[0].mass?.toString() ?: "0"
            )
            repEditText.setText(
                pastLog[0].exerciseSetList[0].rep?.toString() ?: "0"
            )
        }

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
                    logItem,
                    mass,
                    setNum,
                    rep
                )
            )
            editLogViewModel.updateSetRecyclerViewData(SetStorage.getSets(requireContext()))

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventManager.subscribe(
            requireContext().getString(R.string.event_add_set),
            ::addSetSubscriber
        )
        setupSwipeListeners()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventManager.unsubscribe(requireContext().getString(R.string.event_add_set),
            ::addSetSubscriber)
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


    private fun setupSwipeListeners() {
        val kgEditText: EditText = binding.kgEnterText
        val repEditText: EditText = binding.repEnterText

        // Initialize the swipe listener with the appropriate increment value
        kgEditText.setOnTouchListener(GestureListenerMass(kgEditText, 1.25f))
        repEditText.setOnTouchListener(GestureListenerRep(repEditText, 1))
    }

}