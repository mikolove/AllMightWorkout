package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.databinding.ItemExerciseBinding
import com.mikolove.allmightworkout.framework.presentation.main.exercise.ExerciseDiffCallBack

class WorkoutExercisesAdapter() : RecyclerView.Adapter<WorkoutExercisesAdapter.WorkoutExercisesViewHolder>() {

    private val exercises = mutableListOf<Exercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutExercisesViewHolder {

        return WorkoutExercisesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false))
    }

    override fun onBindViewHolder(holder: WorkoutExercisesViewHolder, position: Int) {
        holder.bind(exercises.get(position))
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun submitList(list: List<Exercise>) {

        val diffCallback = ExerciseDiffCallBack(exercises,list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        exercises.clear()
        exercises.addAll(list)
    }

    class WorkoutExercisesViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        val binding =  ItemExerciseBinding.bind(itemView)

        fun bind(item: Exercise) = with(itemView) {
            //Bind values
            binding.itemExerciseTextTitle.text = item.name
            binding.itemExerciseTextSubtitle.text = "${item.sets.size} ${item.exerciseType} sets - Bodypart : ${item.bodyPart?.name}"
         }
    }

}
