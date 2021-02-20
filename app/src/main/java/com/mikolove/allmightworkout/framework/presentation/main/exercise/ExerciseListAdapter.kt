package com.mikolove.allmightworkout.framework.presentation.main.exercise

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.ItemExerciseBinding
import com.mikolove.allmightworkout.framework.presentation.common.changeColor
import com.mikolove.allmightworkout.util.printLogD

class ExerciseListAdapter(
    private val interaction: Interaction? = null,
    private val lifecycleOwner : LifecycleOwner,
    private val selectedExercises : LiveData<ArrayList<Exercise>>,
    private val dateUtil: DateUtil
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Exercise>() {

        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.idExercise == newItem.idExercise
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_exercise,
                parent,
                false),
            interaction,
            lifecycleOwner,
            selectedExercises,
            dateUtil
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExerciseViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Exercise>) {
        val commitCallBack = Runnable {
            //if process died restore list position
            interaction?.restoreListPosition()
        }
        printLogD("ExerciseListAdapter","size : ${list.size}")
        differ.submitList(list,commitCallBack)
    }

    class ExerciseViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val lifecycleOwner : LifecycleOwner,
        private val selectedExercises : LiveData<ArrayList<Exercise>>,
        private val dateUtil: DateUtil
    ) : RecyclerView.ViewHolder(itemView) {

        //Maybe change place for this
        private val COLOR_UNSELECTED = R.color.design_default_color_background
        private val COLOR_SELECTED   = R.color.colorSecondary
        private val binding = ItemExerciseBinding.bind(itemView)

        fun bind(item: Exercise) = with(itemView) {
            //Add clicklisteners
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.setOnLongClickListener{
                interaction?.activateMultiSelectionMode()
                interaction?.onItemSelected(adapterPosition,item)
                true
            }

            //Bind values
            binding.itemExerciseTextName.text = item.name
            binding.itemExerciseTextBodyPart.text = item.bodyPart?.name
            binding.itemExerciseTextExerciseType.text = item.exerciseType.name
            binding.itemExerciseTextSets.text = item.sets?.size.toString()
            binding.itemExerciseTextCreatedAt.text = item.createdAt

            selectedExercises.observe(lifecycleOwner, { exercises ->

                if(!exercises.isNullOrEmpty()){

                    if(exercises.contains(item)){
                        changeColor(newColor = COLOR_SELECTED)
                    }else{
                        changeColor(newColor = COLOR_UNSELECTED)
                    }
                }else{
                    changeColor(newColor = COLOR_UNSELECTED)
                }
            })
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Exercise)

        fun restoreListPosition()

        fun isMultiSelectionModeEnabled(): Boolean

        fun activateMultiSelectionMode()

        fun isExerciseSelected(exercise: Exercise): Boolean
    }
}
