package com.mikolove.allmightworkout.framework.presentation.main.workout

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemWorkoutBinding
import com.mikolove.allmightworkout.util.printLogD

class WorkoutDiffCallBack(private val oldList : List<Workout>, private val newList : List<Workout>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idWorkout == newList[newItemPosition].idWorkout
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}

class WorkoutListAdapter(
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val selectedWorkouts : LiveData<ArrayList<Workout>>,
    private val dateUtil: DateUtil
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val workouts = ArrayList<Workout>()

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Workout>() {

        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.idWorkout == newItem.idWorkout
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return WorkoutViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false),
            interaction,
            lifecycleOwner,
            selectedWorkouts,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WorkoutViewHolder -> {
                //holder.bind(differ.currentList.get(position))
                holder.bind(workouts.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return workouts.size
        //return differ.currentList.size
    }

    fun submitList(list: List<Workout>) {

        val diffCallback = WorkoutDiffCallBack(workouts,list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        workouts.clear()
        workouts.addAll(list)

        diffResult.dispatchUpdatesTo(this)


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
        private val binding =  ItemWorkoutBinding.bind(itemView)

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
