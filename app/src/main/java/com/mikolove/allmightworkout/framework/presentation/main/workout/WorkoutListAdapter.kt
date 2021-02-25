package com.mikolove.allmightworkout.framework.presentation.main.workout

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemWorkoutBinding
import com.mikolove.allmightworkout.framework.presentation.common.Change
import com.mikolove.allmightworkout.framework.presentation.common.createCombinedPayload

class WorkoutListAdapter(
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val selectedWorkouts : LiveData<ArrayList<Workout>>,
    private val dateUtil: DateUtil
) : RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder>() {

    private val workouts = mutableListOf<Workout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {

        return WorkoutViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false),
            interaction,
            lifecycleOwner,
            selectedWorkouts,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workouts.get(position))
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            val combinedChange = createCombinedPayload(payloads as List<Change<Workout>>)

            val oldWorkout = combinedChange.oldData
            val newWorkout = combinedChange.newData

            if(oldWorkout.name != newWorkout.name)
                holder.binding.itemWorkoutTextName.text = newWorkout.name

            if(oldWorkout.createdAt != newWorkout.createdAt)
                holder.binding.itemWorkoutTextCreatedAt.text = newWorkout.createdAt
         }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    fun submitList(list: List<Workout>) {

        val diffCallback = WorkoutDiffCallBack(workouts,list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        workouts.clear()
        workouts.addAll(list)
    }

    class WorkoutViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val lifecycleOwner: LifecycleOwner,
        private val selectedWorkouts: LiveData<ArrayList<Workout>>,
        private val dateUtil: DateUtil
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        val binding =  ItemWorkoutBinding.bind(itemView)

        fun bind(item: Workout) = with(itemView) {

            //Add test transition
            binding.itemWorkoutContainer.transitionName = resources.getString(R.string.test_workout_item_transition_name,item.idWorkout)

            //Add clicklisteners
            itemView.setOnClickListener {
                interaction?.onItemSelected(bindingAdapterPosition, item,binding.itemWorkoutContainer)
            }
            itemView.setOnLongClickListener{
                interaction?.activateMultiSelectionMode()
                interaction?.onItemSelected(bindingAdapterPosition, item,binding.itemWorkoutContainer)
                true
            }

            //Bind values
            binding.itemWorkoutTextName.text = item.name
            binding.itemWorkoutTextCreatedAt.text = item.createdAt

            //Its seems to never crash and be efficient. Author mitch tabian
            //This is use to change the color of selected item
            selectedWorkouts.observe(lifecycleOwner, { workouts ->

              if(!workouts.isNullOrEmpty()){

                  if(workouts.contains(item)){
                      binding.itemWorkoutContainer.setChecked(true)
                  }else{
                      binding.itemWorkoutContainer.setChecked(false)
                  }
              }else{
                  binding.itemWorkoutContainer.setChecked(false)
              }

            })
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Workout, containerView : View)

        fun isMultiSelectionModeEnabled(): Boolean

        fun activateMultiSelectionMode()

        fun isWorkoutSelected(workout : Workout): Boolean
    }


}
