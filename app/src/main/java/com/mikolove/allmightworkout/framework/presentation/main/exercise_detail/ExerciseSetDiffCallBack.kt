/*
package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

import androidx.recyclerview.widget.DiffUtil
import com.mikolove.core.domain.exercise.ExerciseSet

class ExerciseSetDiffCallBack(
    private val oldList : List<ExerciseSet>,
    private val newList : List<ExerciseSet>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idExerciseSet == newList[newItemPosition].idExerciseSet
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

*/
/*    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //return super.getChangePayload(oldItemPosition, newItemPosition)
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return Change(
            oldItem,
            newItem
        )
    }*//*

}*/
