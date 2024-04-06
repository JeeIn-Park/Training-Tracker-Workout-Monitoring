package com.example.trainingtracker.ui.muscles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.trainingtracker.R
import com.example.trainingtracker.ui.muscles.MuscleStatus.getMuscleColor


class FrontMuscleAdapter(private val context: Context, private val onItemClick: (Button) -> Unit) :
    ListAdapter<Muscle, FrontMuscleAdapter.FrontMuscleViewHolder>(MuscleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrontMuscleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_tag, parent, false)
        return FrontMuscleViewHolder(view)
    }

    override fun onBindViewHolder(holder: FrontMuscleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

//    private val muscleFrontNeckTraps : Button = itemView.findViewById(R.id.muscle_front_neck_traps)
//    private val muscleBackNeckTraps : Button = itemView.findViewById(R.id.muscle_back_neck_traps)
//    private val muscleFrontShoulder : Button = itemView.findViewById(R.id.muscle_front_shoulder)
//    private val muscleBackShoulder : Button = itemView.findViewById(R.id.muscle_back_shoulder)
//    private val muscleFrontChest : Button = itemView.findViewById(R.id.muscle_front_chest)
//    private val muscleFrontBiceps : Button = itemView.findViewById(R.id.muscle_front_biceps)
//    private val muscleBackTriceps : Button = itemView.findViewById(R.id.muscle_back_triceps)
//    private val muscleFrontForearm : Button = itemView.findViewById(R.id.muscle_front_forearm)
//    private val muscleBackForearm : Button = itemView.findViewById(R.id.muscle_back_forearm)
//    private val muscleFrontAbs : Button = itemView.findViewById(R.id.muscle_front_abs)
//    private val muscleFrontObliques : Button = itemView.findViewById(R.id.muscle_front_obliques)
//    private val muscleBackObliques : Button = itemView.findViewById(R.id.muscle_back_obliques)
//    private val muscleBackUpperBack : Button = itemView.findViewById(R.id.muscle_back_upper_back)
//    private val muscleBackLowerBack : Button = itemView.findViewById(R.id.muscle_back_lower_back)
//    private val muscleFrontInnerThigh : Button = itemView.findViewById(R.id.muscle_front_inner_thigh)
//    private val muscleBackGlutesButtocks : Button = itemView.findViewById(R.id.muscle_back_glutes_buttocks)
//    private val muscleFrontQuadriceps : Button = itemView.findViewById(R.id.muscle_front_quadriceps)
//    private val muscleBackHamstrings : Button = itemView.findViewById(R.id.muscle_back_hamstrings)
//    private val muscleFrontCalves : Button = itemView.findViewById(R.id.muscle_front_calves)
//    private val muscleBackCalves : Button = itemView.findViewById(R.id.muscle_back_calves)

    inner class FrontMuscleViewHolder(itemView: View) : ViewHolder(itemView) {
        private val muscleFrontNeckTraps : Button = itemView.findViewById(R.id.muscle_front_neck_traps)
        private val muscleFrontShoulder : Button = itemView.findViewById(R.id.muscle_front_shoulder)
        private val muscleFrontChest : Button = itemView.findViewById(R.id.muscle_front_chest)
        private val muscleFrontBiceps : Button = itemView.findViewById(R.id.muscle_front_biceps)
        private val muscleFrontForearm : Button = itemView.findViewById(R.id.muscle_front_forearm)
        private val muscleFrontAbs : Button = itemView.findViewById(R.id.muscle_front_abs)
        private val muscleFrontObliques : Button = itemView.findViewById(R.id.muscle_front_obliques)
        private val muscleFrontInnerThigh : Button = itemView.findViewById(R.id.muscle_front_inner_thigh)
        private val muscleFrontQuadriceps : Button = itemView.findViewById(R.id.muscle_front_quadriceps)
        private val muscleFrontCalves : Button = itemView.findViewById(R.id.muscle_front_calves)


        fun bind(muscles : List<Muscle>) {
            muscleFrontNeckTraps.setBackgroundColor(getMuscleColor(context, muscles[0]))
            muscleFrontShoulder.setBackgroundColor(getMuscleColor(context, muscles[1]))
            muscleFrontChest.setBackgroundColor(getMuscleColor(context, muscles[2]))
            muscleFrontBiceps.setBackgroundColor(getMuscleColor(context, muscles[4]))
            muscleFrontForearm.setBackgroundColor(getMuscleColor(context, muscles[6]))
            muscleFrontAbs.setBackgroundColor(getMuscleColor(context, muscles[7]))
            muscleFrontObliques.setBackgroundColor(getMuscleColor(context, muscles[8]))
            muscleFrontInnerThigh.setBackgroundColor(getMuscleColor(context, muscles[13]))
            muscleFrontQuadriceps.setBackgroundColor(getMuscleColor(context, muscles[15]))
            muscleFrontCalves.setBackgroundColor(getMuscleColor(context, muscles[17]))
        }
    }

}