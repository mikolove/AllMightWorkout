package com.mikolove.allmightworkout.framework.presentation.main.exercise_set

import androidx.recyclerview.widget.DiffUtil
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.presentation.common.Change
import com.mikolove.allmightworkout.util.printLogD

class ExerciseSetDiffCallBack(private val oldList : List<ExerciseSet>, private val newList : List<ExerciseSet>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idExerciseSet == newList[newItemPosition].idExerciseSet
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}