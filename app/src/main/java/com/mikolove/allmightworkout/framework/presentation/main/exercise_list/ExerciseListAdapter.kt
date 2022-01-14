package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemExerciseBinding
import com.mikolove.allmightworkout.framework.presentation.common.Change
import com.mikolove.allmightworkout.framework.presentation.common.createCombinedPayload

class ExerciseListAdapter(
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val selectedExercises : LiveData<ArrayList<Exercise>>,
    private val dateUtil: DateUtil
) : RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>() {

    private val exercises = mutableListOf<Exercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {

        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false),
            interaction,
            lifecycleOwner,
            selectedExercises,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises.get(position))
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            val combinedChange = createCombinedPayload(payloads as List<Change<Exercise>>)

            val oldExercise = combinedChange.oldData
            val newExercise = combinedChange.newData

            holder.binding.itemExerciseTextTitle.text = newExercise.name
            holder.binding.itemExerciseTextSubtitle.text = "${newExercise.sets.size} ${newExercise.exerciseType} sets - Bodypart : ${newExercise.bodyPart?.name}"

            holder.itemView.setOnClickListener {
                interaction?.onItemSelected(holder.bindingAdapterPosition, newExercise, holder.binding.itemExerciseContainer)
            }
        }
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

    class ExerciseViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val lifecycleOwner: LifecycleOwner,
        private val selectedExercises: LiveData<ArrayList<Exercise>>,
        private val dateUtil: DateUtil
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        val binding =  ItemExerciseBinding.bind(itemView)

        fun bind(item:Exercise) = with(itemView) {

            //Add test transition
            binding.itemExerciseContainer.transitionName = resources.getString(R.string.test_exercise_item_transition_name,item.idExercise)

            //Add clicklisteners
            itemView.setOnClickListener {
                interaction?.onItemSelected(bindingAdapterPosition, item, binding.itemExerciseContainer)
            }
            itemView.setOnLongClickListener{
                interaction?.activateMultiSelectionMode()
                interaction?.onItemSelected(bindingAdapterPosition, item, binding.itemExerciseContainer)
                true
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
        fun onItemSelected(position: Int, item: Exercise, containerView : View)

        fun isMultiSelectionModeEnabled(): Boolean

        fun activateMultiSelectionMode()

        fun isExerciseSelected(exercise : Exercise): Boolean
    }


}