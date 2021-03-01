package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemSetBinding
import com.mikolove.allmightworkout.framework.presentation.common.Change
import com.mikolove.allmightworkout.framework.presentation.common.createCombinedPayload
import com.mikolove.allmightworkout.framework.presentation.main.ExerciseSetDiffCallBack

class ExerciseSetListAdapter (
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val exercise : Exercise,
    private val dateUtil: DateUtil
) : RecyclerView.Adapter<ExerciseSetListAdapter.ExerciseSetViewHolder>() {

    private val sets = mutableListOf<ExerciseSet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSetViewHolder {

        return ExerciseSetViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_set, parent, false),
            interaction,
            exercise,
            lifecycleOwner,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: ExerciseSetViewHolder, position: Int) {
        holder.bind(sets.get(position))
    }

    override fun onBindViewHolder(holder: ExerciseSetViewHolder, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            val combinedChange = createCombinedPayload(payloads as List<Change<ExerciseSet>>)

            val oldSet = combinedChange.oldData
            val newSet = combinedChange.newData

            if(oldSet.reps != newSet.reps)
                holder.binding.itemSetTextRep.editText?.setText(newSet.reps.toString())
            if(oldSet.weight != newSet.weight)
                holder.binding.itemSetTextWeight.editText?.setText(newSet.weight.toString())
            if(oldSet.time != newSet.time)
                holder.binding.itemSetTextTime.editText?.setText(newSet.time.toString())
            if(oldSet.reps != newSet.reps)
                holder.binding.itemSetTextRest.editText?.setText(newSet.restTime.toString())
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    fun submitList(list: List<ExerciseSet>) {

        val diffCallback = ExerciseSetDiffCallBack(sets,list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        sets.clear()
        sets.addAll(list)
    }

    class ExerciseSetViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val exercise : Exercise,
        private val lifecycleOwner: LifecycleOwner,
        private val dateUtil: DateUtil
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        val binding =  ItemSetBinding.bind(itemView)

        fun bind(item: ExerciseSet) = with(itemView) {

            //Add test transition

            binding.itemSetButtonEdit.setOnClickListener {
                interaction?.onEditClick(binding.itemSetContentExpandable)
                interaction?.activateEditMode()
            }

            binding.itemSetButtonDelete.setOnClickListener {
                interaction?.onDeleteClick(item)
            }

            //Bind values
            binding.itemSetTitle.text = "Set"
            if(exercise.exerciseType.equals(ExerciseType.REP_EXERCISE)) {
                binding.itemSetSubtitle.text = "${item.reps} x ${item.weight} kg - Rest time : ${item.restTime} sec"
                binding.itemSetTextTime.isEnabled = false
                binding.itemSetTextRep.isEnabled = true
            }else {
                binding.itemSetSubtitle.text = "${item.reps} x ${item.time} sec - Rest time : ${item.restTime} sec"
                binding.itemSetTextTime.isEnabled = true
                binding.itemSetTextRep.isEnabled = false
            }

            binding.itemSetTextRep.editText?.setText(item.reps.toString())
            binding.itemSetTextWeight.editText?.setText(item.weight.toString())
            binding.itemSetTextTime.editText?.setText(item.time.toString())
            binding.itemSetTextRest.editText?.setText(item.restTime.toString())

        }
    }

    interface Interaction {

        fun onEditClick(expandableView: View)

        fun onDeleteClick(item: ExerciseSet)

        fun isEditModeEnabled(): Boolean

        fun activateEditMode()
    }


}