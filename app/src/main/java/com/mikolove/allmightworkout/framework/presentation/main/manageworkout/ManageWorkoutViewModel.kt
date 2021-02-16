package com.mikolove.allmightworkout.framework.presentation.main.manageworkout

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.ManageWorkoutListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.WorkoutInteractionManager
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.WorkoutInteractionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ManageWorkoutViewModel
@Inject
constructor(
    private val manageWorkoutListInteractors: ManageWorkoutListInteractors,
    private val workoutFactory: WorkoutFactory,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<ManageWorkoutViewState>(){


    /********************************************************************
        LIVE DATA INTERACTION STATE - editText , etc
    *********************************************************************/

    private val workoutInteractionManager: WorkoutInteractionManager = WorkoutInteractionManager()
    val workoutNameInteractionState: LiveData<WorkoutInteractionState>
        get() = workoutInteractionManager.workoutNameState

    /********************************************************************
        INIT BLOC
    *********************************************************************/

    override fun initNewViewState(): ManageWorkoutViewState {
        return ManageWorkoutViewState()
    }

    init {

    }

    override fun handleNewData(data: ManageWorkoutViewState) {

        data.let { viewState ->

            viewState.workout?.let { workout ->
                setWorkout(workout)
                setListExercise(workout.exercises)
            }
        }

    }


    override fun setStateEvent(stateEvent: StateEvent) {
        val job : Flow<DataState<ManageWorkoutViewState>?> = when(stateEvent){

/*
            is ManageWorkoutStateEvent.GetWorkoutByIdEvent -> {
                manageWorkoutListInteractors.getWorkoutById.getWorkoutById(
                    idWorkout = getCacheIdWorkout(),
                    stateEvent = stateEvent
                )
            }
*/

            is ManageWorkoutStateEvent.InsertWorkoutEvent -> {
                manageWorkoutListInteractors.insertWorkout.insertWorkout(
                    name = stateEvent.name,
                    stateEvent = stateEvent
                )
            }

  /*          is ManageWorkoutStateEvent.UpdateWorkoutEvent -> {
                manageWorkoutListInteractors.updateWorkout.updateWorkout(
                    workout = getWorkout(),
                    stateEvent = stateEvent
                )
            }

            is ManageWorkoutStateEvent.RemoveWorkoutEvent -> {
                manageWorkoutListInteractors.removeWorkout.removeWorkout(
                    workout = getWorkout(),
                    stateEvent = stateEvent
                )
            }*/

            is ManageWorkoutStateEvent.CreateStateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }
            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent,job)
    }


    /********************************************************************
        TRIGGER STATE EVENTS - FUNCTIONS
    *********************************************************************/
    fun loadWorkout(idCacheWorkout : String?){

    }

    /********************************************************************
        GETTERS
    *********************************************************************/

    fun getCacheIdWorkout() : String? = getCurrentViewStateOrNew().cacheIdWorkout ?: null

    fun getWorkout() : Workout? = getCurrentViewStateOrNew().workout ?: null


    /********************************************************************
        SETTERS
    *********************************************************************/

    fun setCacheIdWorkout(cacheIdWorkout : String?){
        val update = getCurrentViewStateOrNew()
        update.cacheIdWorkout = cacheIdWorkout
        setViewState(update)
    }

    fun setWorkout(workout : Workout?){
        val update = getCurrentViewStateOrNew()
        update.workout = workout
        setViewState(update)
    }

    fun setListExercise(exercises : List<Exercise>?){
        val update = getCurrentViewStateOrNew()
        update.exerciseList = exercises
        setViewState(update)
    }
}