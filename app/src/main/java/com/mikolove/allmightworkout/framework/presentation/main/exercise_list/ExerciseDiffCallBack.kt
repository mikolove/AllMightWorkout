/*
package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

import androidx.recyclerview.widget.DiffUtil
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.allmightworkout.framework.presentation.common.Change

class ExerciseDiffCallBack(private val oldList : List<Exercise>, private val newList : List<Exercise>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idExercise == newList[newItemPosition].idExercise
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
