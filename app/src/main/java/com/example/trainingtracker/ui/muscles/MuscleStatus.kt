package com.example.trainingtracker.ui.muscles

import android.content.Context
import com.example.trainingtracker.R
//import com.example.trainingtracker.databinding.FragmentMuscleBackBinding
//import com.example.trainingtracker.databinding.FragmentMuscleFrontBinding
import com.example.trainingtracker.ui.exerciseCard.CardStorage
import com.example.trainingtracker.ui.exerciseCard.ExerciseCard
import com.example.trainingtracker.ui.exerciseLog.ExerciseLog
import com.example.trainingtracker.ui.exerciseLog.ExerciseSet
import com.example.trainingtracker.ui.exerciseLog.LogStorage
import com.example.trainingtracker.ui.exerciseLog.OneRepMax
import java.time.LocalDateTime
import java.time.Duration

object MuscleStatus {
    const val RECOVERED = 0
    const val RECOVERING = 1
    const val NEED_EXERCISE = 2

    private const val IN_SECONDS = 0
    private const val IN_MINUTES = 1
    private const val IN_HOURS = 2
    private const val IN_DAYS = 3

    fun muscleState(muscle: Muscle) : Int{
        if (muscle.lastActivity == null) {
            return RECOVERED
        } else if (restingTime(muscle.lastActivity, IN_HOURS) < 50) {
            return RECOVERING
        } else if (10 < restingTime(muscle.lastActivity, IN_DAYS)) {
            return NEED_EXERCISE
        }
        return RECOVERED
    }

    private fun restingTime(lastActivity: LocalDateTime?, mode: Int) : Long {
        val currentDateTime = LocalDateTime.now()
        val duration = Duration.between(lastActivity, currentDateTime)
        return when (mode) {
            IN_SECONDS -> { duration.seconds }
            IN_MINUTES -> { duration.toMinutes() }
            IN_HOURS -> { duration.toHours() }
            IN_DAYS -> { duration.toDays() }
            else -> -1
        }
    }

    fun refreshMuscle(context : Context){
        val muscleList = MuscleStorage.loadMuscles(context)
        val updatedMuscleList : MutableList<Muscle> = listOf<Muscle>().toMutableList()

        for(muscle in muscleList) {
            updatedMuscleList.add(
                Muscle(
                    muscle.lastActivity,
                    muscleState(muscle),
                    muscle.name,
                    muscle.layout
                )
            )
        }
        MuscleStorage.saveMuscles(context, updatedMuscleList)
    }

    fun logExercise(
        context: Context,
        exerciseSetList: List<ExerciseSet>,
        exerciseCard: ExerciseCard,
        exerciseDate: LocalDateTime,
        logStorage: LogStorage) {

        var muscles : MutableList<Muscle> = MuscleStorage.loadMuscles(context).toMutableList()
        val log = ExerciseLog(
            dateTime = exerciseDate,
            exerciseCard = exerciseCard.id,
            exerciseSetList = exerciseSetList,
            totalSet = null,
            totalWeight = null
        )
        logStorage.addLog(context, log)

        val updatedCard = ExerciseCard(
            id = exerciseCard.id,
            lastActivity = exerciseDate,
            timeAdded = exerciseCard.timeAdded,
            name = exerciseCard.name,
            mainMuscles = exerciseCard.mainMuscles,
            subMuscles = exerciseCard.subMuscles,
            tag = exerciseCard.tag,
            oneRepMax = OneRepMax.oneRepMaxRecord_pb(logStorage.loadLogs(context))
        )
        CardStorage.editCard(context, exerciseCard, updatedCard)

        for (muscle in exerciseCard.mainMuscles) {
            val updatedMuscle = Muscle(
                exerciseDate,
                RECOVERING,
                muscle.name,
                muscle.layout
            )
            MuscleStorage.updateMuscle(context, muscle, updatedMuscle)
        }
        for (muscle in exerciseCard.subMuscles) {
            val updatedMuscle = Muscle(
                exerciseDate,
                RECOVERING,
                muscle.name,
                muscle.layout
            )
            MuscleStorage.updateMuscle(context, muscle, updatedMuscle)
        }
    }

//    fun setMuscleColor(context: Context, frontMuscleView: FragmentMuscleFrontBinding, backMuscleView: FragmentMuscleBackBinding){
//        val muscleList = MuscleStorage.loadMuscles(context)
//        frontMuscleView.muscleFrontNeckTraps.setBackgroundColor(getMuscleColor(context, muscleList[0]))
//        backMuscleView.muscleBackNeckTraps.setBackgroundColor(getMuscleColor(context, muscleList[0]))
//        frontMuscleView.muscleFrontShoulder.setBackgroundColor(getMuscleColor(context, muscleList[1]))
//        backMuscleView.muscleBackShoulder.setBackgroundColor(getMuscleColor(context, muscleList[1]))
//        frontMuscleView.muscleFrontChest.setBackgroundColor(getMuscleColor(context, muscleList[2]))
//        frontMuscleView.muscleFrontBiceps.setBackgroundColor(getMuscleColor(context, muscleList[4]))
//        backMuscleView.muscleBackTriceps.setBackgroundColor(getMuscleColor(context, muscleList[5]))
//        frontMuscleView.muscleFrontForearm.setBackgroundColor(getMuscleColor(context, muscleList[6]))
//        backMuscleView.muscleBackForearm.setBackgroundColor(getMuscleColor(context, muscleList[6]))
//        frontMuscleView.muscleFrontAbs.setBackgroundColor(getMuscleColor(context, muscleList[7]))
//        frontMuscleView.muscleFrontObliques.setBackgroundColor(getMuscleColor(context, muscleList[8]))
//        backMuscleView.muscleBackObliques.setBackgroundColor(getMuscleColor(context, muscleList[8]))
//        backMuscleView.muscleBackUpperBack.setBackgroundColor(getMuscleColor(context, muscleList[10]))
//        backMuscleView.muscleBackLowerBack.setBackgroundColor(getMuscleColor(context, muscleList[11]))
//        frontMuscleView.muscleFrontInnerThigh.setBackgroundColor(getMuscleColor(context, muscleList[13]))
//        backMuscleView.muscleBackGlutesButtocks.setBackgroundColor(getMuscleColor(context, muscleList[14]))
//        frontMuscleView.muscleFrontQuadriceps.setBackgroundColor(getMuscleColor(context, muscleList[15]))
//        backMuscleView.muscleBackHamstrings.setBackgroundColor(getMuscleColor(context, muscleList[16]))
//        frontMuscleView.muscleFrontCalves.setBackgroundColor(getMuscleColor(context, muscleList[17]))
//        backMuscleView.muscleBackCalves.setBackgroundColor(getMuscleColor(context, muscleList[17]))
//    }

    fun getMuscleColor(context: Context, muscle: Muscle) : Int {
        return when (muscle.status) {
            RECOVERED     -> context.getColor(R.color.turquoise)
            RECOVERING    -> context.getColor(R.color.grey)
            NEED_EXERCISE -> context.getColor(R.color.cafeLatte)
            else -> {context.getColor(R.color.red)}
        }
    }
}
