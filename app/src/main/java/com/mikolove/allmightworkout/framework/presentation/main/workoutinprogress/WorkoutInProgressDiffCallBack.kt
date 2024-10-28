/*
package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import androidx.recyclerview.widget.DiffUtil
import com.mikolove.core.domain.exercise.Exercise

class WorkoutInProgressDiffCallBack (private val oldList : List<Exercise>, private val newList : List<Exercise>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idExercise == newList[newItemPosition].idExercise
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}*/
