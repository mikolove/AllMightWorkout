/*
package com.mikolove.allmightworkout.framework.presentation.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemHistoryWorkoutBinding
import com.mikolove.allmightworkout.framework.presentation.common.Change
import com.mikolove.allmightworkout.framework.presentation.common.createCombinedPayload

class HistoryListAdapter(
    private val interaction : Interaction? = null,
    private val dateUtil: DateUtil

) : RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>(){

    private val historyWorkouts = mutableListOf<HistoryWorkout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history_workout, parent, false),
            interaction,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyWorkouts.get(position))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            val combinedChange = createCombinedPayload(payloads as List<Change<HistoryWorkout>>)

            val oldWorkout = combinedChange.oldData
            val newWorkout = combinedChange.newData

            holder.itemView.setOnClickListener {
                interaction?.onItemSelected(holder.bindingAdapterPosition, newWorkout)
            }

        }
    }

    override fun getItemCount(): Int {
        return historyWorkouts.size
    }

    fun submitList(list: List<HistoryWorkout>) {

        val diffCallback = HistoryWorkoutDiffCallBack(historyWorkouts,list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        historyWorkouts.clear()
        historyWorkouts.addAll(list)
    }

    class HistoryViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val dateUtil: DateUtil
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        val binding =  ItemHistoryWorkoutBinding.bind(itemView)

        fun bind(item: HistoryWorkout) = with(itemView) {


            //Add clicklisteners
            itemView.setOnClickListener {
                interaction?.onItemSelected(bindingAdapterPosition, item)
            }
            //Bind values

            binding.itemHistoryTextName.text = item.name

            binding.itemHistoryTextCreatedAt.text = item.createdAt
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int?, item: HistoryWorkout)
    }

}*/
