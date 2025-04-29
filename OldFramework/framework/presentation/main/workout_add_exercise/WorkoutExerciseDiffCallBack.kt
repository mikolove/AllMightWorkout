/*
package com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise

import androidx.recyclerview.widget.DiffUtil
import com.mikolove.allmightworkout.framework.presentation.common.Change


class WorkoutExerciseDiffCallBack(private val oldList : List<WorkoutExercise>, private val newList : List<WorkoutExercise>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].exercise.idExercise == newList[newItemPosition].exercise.idExercise
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
}*/
