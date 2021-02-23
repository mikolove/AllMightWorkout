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
import com.mikolove.allmightworkout.util.printLogD


class WorkoutListAdapter(
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val selectedWorkouts : LiveData<ArrayList<Workout>>,
    private val dateUtil: DateUtil
) : RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder>() {

    private val workouts = mutableListOf<Workout>()

/*
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Workout>() {

        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.idWorkout == newItem.idWorkout
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
*/


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
/*        when (holder) {
            is WorkoutViewHolder -> {
                //holder.bind(differ.currentList.get(position))
                holder.bind(workouts.get(position))
            }
        }*/

        holder.bind(workouts.get(position))
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
            printLogD("WorkoutListAdapter","No payload found")
        }else{

            printLogD("WorkoutListAdapter","Payload found")
            val combinedChange = createCombinedPayload(payloads as List<Change<Workout>>)

            val oldWorkout = combinedChange.oldData
            val newWorkout = combinedChange.newData

            //Bind values
            if(oldWorkout.name != newWorkout.name)
                holder.binding.itemWorkoutTextName.text = newWorkout.name

            if(oldWorkout.createdAt != newWorkout.createdAt)
                holder.binding.itemWorkoutTextCreatedAt.text = newWorkout.createdAt

         }
    }

    override fun getItemCount(): Int {
        return workouts.size
        //return differ.currentList.size
    }

    fun submitList(list: List<Workout>) {

        val diffCallback = WorkoutDiffCallBack(workouts,list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        workouts.clear()
        workouts.addAll(list)


 /*       val commitCallBack = Runnable {
            //if process died restore list position
            interaction?.restoreListPosition()
        }

        differ.submitList(list,commitCallBack)*/
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
/*        private val COLOR_UNSELECTED = R.color.design_default_color_background
        private val COLOR_SELECTED   = R.color.colorSecondary*/
        val binding =  ItemWorkoutBinding.bind(itemView)

        fun bind(item: Workout) = with(itemView) {

            //Add test transition
            binding.itemWorkoutContainer.transitionName = resources.getString(R.string.test_workout_item_transition_name,item.idWorkout)

            //Add clicklisteners
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item,binding.itemWorkoutContainer)
            }
            itemView.setOnLongClickListener{
                interaction?.activateMultiSelectionMode()
                interaction?.onItemSelected(adapterPosition, item,binding.itemWorkoutContainer)
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

        fun restoreListPosition()

        fun isMultiSelectionModeEnabled(): Boolean

        fun activateMultiSelectionMode()

        fun isWorkoutSelected(workout : Workout): Boolean
    }


}
