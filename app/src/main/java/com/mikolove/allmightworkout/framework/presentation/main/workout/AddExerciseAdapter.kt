package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemExerciseBinding
import com.mikolove.allmightworkout.framework.presentation.main.exercise.ExerciseDiffCallBack

class AddExerciseAdapter(
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val selectedExercises : LiveData<ArrayList<Exercise>>
) : RecyclerView.Adapter<AddExerciseAdapter.AddExerciseViewHolder>() {

    private val exercises = mutableListOf<Exercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddExerciseViewHolder {

        return AddExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false),
            interaction,
            lifecycleOwner,
            selectedExercises
        )
    }

    override fun onBindViewHolder(holder: AddExerciseViewHolder, position: Int) {
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

    class AddExerciseViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val lifecycleOwner: LifecycleOwner,
        private val selectedExercises: LiveData<ArrayList<Exercise>>
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        val binding =  ItemExerciseBinding.bind(itemView)

        fun bind(item: Exercise) = with(itemView) {

            //Add clicklisteners
            itemView.setOnClickListener {
                interaction?.onItemSelected(item)
            }

            //Bind values
            binding.itemExerciseTextTitle.text = item.name
            binding.itemExerciseTextSubtitle.text = "${item.sets.size} ${item.exerciseType} sets - Bodypart : ${item.bodyPart?.name}"
            selectedExercises.observe(lifecycleOwner, { exercises ->

                if(!exercises.isNullOrEmpty()){

                    if(exercises.contains(item)){
                        binding.itemExerciseContainer.setChecked(true)
                    }else{
                        binding.itemExerciseContainer.setChecked(false)
                    }
                }else{
                    binding.itemExerciseContainer.setChecked(false)
                }

            })
        }
    }

    interface Interaction {
        fun onItemSelected(item: Exercise)

        fun isExerciseSelected(exercise : Exercise): Boolean
    }
}
