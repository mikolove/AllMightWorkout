package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemSetBinding
import com.mikolove.allmightworkout.framework.presentation.common.Change
import com.mikolove.allmightworkout.framework.presentation.common.createCombinedPayload
import com.mikolove.allmightworkout.util.printLogD

class ExerciseSetListAdapter (
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val exerciseType : LiveData<ExerciseType>
) : RecyclerView.Adapter<ExerciseSetListAdapter.ExerciseSetViewHolder>() {

    private val sets = mutableListOf<ExerciseSet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSetViewHolder {

        return ExerciseSetViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_set, parent, false),
            interaction,
            exerciseType,
            lifecycleOwner
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
        private val exerciseType : LiveData<ExerciseType>,
        private val lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView) {

        val binding =  ItemSetBinding.bind(itemView)

        fun bind(item: ExerciseSet) = with(itemView) {

            //Add test transition
            binding.itemSetContainer.setOnClickListener{
                interaction?.onEditClick(item)
            }

            binding.itemSetButtonDelete.setOnClickListener {
                interaction?.onDeleteClick(item)
            }

            //Bind values
            binding.itemSetTitle.text = "Set"

            exerciseType.observe(lifecycleOwner,{ exerciseType ->
                printLogD("ExerciseSetListAdapter","Set subtitle ${exerciseType}")
                if(exerciseType.equals(ExerciseType.REP_EXERCISE)){
                    binding.itemSetSubtitle.text = "${item.reps} x ${item.weight} kg - Rest time : ${item.restTime} sec"
                }else{
                    binding.itemSetSubtitle.text = "${item.time} sec - Rest time : ${item.restTime} sec"
                }
            })

        }

    }

    interface Interaction {

        fun onEditClick(item: ExerciseSet)

        fun onDeleteClick(item: ExerciseSet)
    }
}