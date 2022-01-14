package com.mikolove.allmightworkout.framework.presentation.main.exercise_set

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ExerciseSetsManager {

    private val _listInteractionExerciseSetManager : MutableLiveData<ArrayList<ExerciseSetInteractionManager>> = MutableLiveData()

    val listInteractionExerciseSetManager : LiveData<ArrayList<ExerciseSetInteractionManager>>
        get() = _listInteractionExerciseSetManager

    init {
        _listInteractionExerciseSetManager.value = ArrayList()
    }

    fun addManager(manager : ExerciseSetInteractionManager){
        _listInteractionExerciseSetManager.value?.add(manager)
    }

    fun removeManager(manager : ExerciseSetInteractionManager){
        _listInteractionExerciseSetManager.value?.remove(manager)
    }

    fun clearManagers(){
        _listInteractionExerciseSetManager.value = ArrayList()
    }

    fun getItemCount() : Int {
        return _listInteractionExerciseSetManager.value?.size ?: 0
    }
}