package com.mikolove.allmightworkout.framework.presentation.main.workout

import androidx.recyclerview.widget.DiffUtil
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.presentation.common.Change

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
        //return super.getChangePayload(oldItemPosition, newItemPosition)
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return Change(
            oldItem,
            newItem
        )
    }
}