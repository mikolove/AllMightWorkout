package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemWorkoutInProgressBinding
import com.mikolove.allmightworkout.util.printLogD

class WorkoutInProgressAdapter(
    private val interaction: Interaction? = null,
    private val dateUtil: DateUtil) : RecyclerView.Adapter<WorkoutInProgressAdapter.WorkoutInProgressViewHolder>() {

    private val exercises = mutableListOf<Exercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutInProgressViewHolder {

        return WorkoutInProgressViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_workout_in_progress, parent, false),
            interaction,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: WorkoutInProgressViewHolder, position: Int) {
        holder.bind(exercises.get(position))
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun submitList(list: List<Exercise>) {

        val diffCallback = WorkoutInProgressDiffCallBack(exercises,list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        exercises.clear()
        exercises.addAll(list)
    }

    class WorkoutInProgressViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val dateUtil: DateUtil
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        val binding =  ItemWorkoutInProgressBinding.bind(itemView)

        fun bind(item: Exercise) = with(itemView) {
            printLogD("WorkoutInProgressAdapter","${item}")
            printLogD("WorkoutInProgressAdapter","${item.startedAt}")
            //Add clicklisteners
            itemView.setOnClickListener {
                interaction?.onItemSelected(item)
            }

            //Bind values
            val sets = item.sets?.size
            val setsDone = item.sets?.filter { it.endedAt != null }.size

            binding.wipExerciseTitle.text = item.name
            binding.wipExerciseSets.text = "${setsDone}/${sets} Sets done."
            binding.wipExerciseStarted.text = "started : ${item.startedAt}"
            binding.wipExerciseEnded.text = "ended : ${item.endedAt}"
        }
    }

    interface Interaction {
        fun onItemSelected(item: Exercise)
    }


}
