package com.mikolove.allmightworkout.framework.presentation.main.manageworkout

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.InsertWorkout
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.ManageWorkoutListInteractors
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.UpdateWorkout
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.WorkoutInteractionManager
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.WorkoutInteractionState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



const val MANAGE_WORKOUT_ERRROR_RETRIEVING_ID_WORKOUT = "Error retrieving idWorkout from bundle."
const val MANAGE_WORKOUT_ID_WORKOUT_BUNDLE_KEY = "idWorkout"
const val WORKOUT_NAME_CANNOT_BE_EMPTY = "Workout name cannot be empty."


@HiltViewModel
class ManageWorkoutViewModel
@Inject
constructor(
    private val manageWorkoutListInteractors: ManageWorkoutListInteractors
) : BaseViewModel<ManageWorkoutViewState>(){


    /********************************************************************
        LIVE DATA INTERACTION STATE - editText , switch
    *********************************************************************/

    private val workoutInteractionManager: WorkoutInteractionManager = WorkoutInteractionManager()

    val workoutNameInteractionState: LiveData<WorkoutInteractionState>
        get() = workoutInteractionManager.workoutNameState

    val workoutIsActiveInteractionState: LiveData<WorkoutInteractionState>
        get() = workoutInteractionManager.workoutIsActiveState

    /********************************************************************
        INIT BLOC
    *********************************************************************/

    override fun initNewViewState(): ManageWorkoutViewState {
        return ManageWorkoutViewState()
    }

    override fun handleNewData(data: ManageWorkoutViewState) {

        data.let { viewState ->

            viewState.workout?.let { workout ->
                printLogD("ManageWorkoutViewModel","Workout updated ${workout}")
                setWorkout(workout)
                setListExercise(workout.exercises)
            }
        }
    }


    override fun setStateEvent(stateEvent: StateEvent) {
        val job : Flow<DataState<ManageWorkoutViewState>?> = when(stateEvent){

            is ManageWorkoutStateEvent.GetWorkoutByIdEvent -> {
                manageWorkoutListInteractors.getWorkoutById.getWorkoutById(
                    idWorkout = stateEvent.idWorkout,
                    stateEvent = stateEvent
                )
            }

          is ManageWorkoutStateEvent.UpdateWorkoutEvent -> {
              if(!isWorkoutNameNull()){
                  manageWorkoutListInteractors.updateWorkout.updateWorkout(
                      workout = getWorkout()!!,
                      stateEvent = stateEvent
                  )
              }else{
                emitStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = UpdateWorkout.UPDATE_WORKOUT_FAILED,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error())),
                    stateEvent= stateEvent
                )
              }
            }

            is ManageWorkoutStateEvent.RemoveWorkoutEvent -> {
                manageWorkoutListInteractors.removeWorkout.removeWorkout(
                    workout = getWorkout()!!,
                    stateEvent = stateEvent
                )
            }

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

    fun updateWorkout(name: String?, isActive: Boolean){
        updateWorkoutName(name)
        updateWorkoutIsActive(isActive)
    }

    fun updateWorkoutIsActive(isActive: Boolean){
        val update = getCurrentViewStateOrNew()
        val updatedWorkout = update.workout?.copy(
            isActive = isActive
        )
        update.workout = updatedWorkout
        setViewState(update)
    }

    fun isWorkoutNameNull() : Boolean{
        val name = getWorkout()?.name
        if (name.isNullOrBlank()) {
            setStateEvent(
                ManageWorkoutStateEvent.CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = WORKOUT_NAME_CANNOT_BE_EMPTY,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
            return true
        } else {
            return false
        }
    }

    fun updateWorkoutName(name: String?){
        if(name.isNullOrBlank()){
            setStateEvent(
                ManageWorkoutStateEvent.CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = WorkoutCacheEntity.nullNameError(),
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        )
                    )
                )
            )
        }
        else{
            val update = getCurrentViewStateOrNew()
            val updatedWorkout = update.workout?.copy(
                name = name
            )
            update.workout = updatedWorkout
            setViewState(update)
        }
    }

    /********************************************************************
        GETTERS
    *********************************************************************/

    fun getWorkout() : Workout? = getCurrentViewStateOrNew().workout ?: null

    fun getIsUpdatePending(): Boolean = getCurrentViewStateOrNew().isUpdatePending?: false

    /********************************************************************
        SETTERS
    *********************************************************************/

    fun setIsUpdatePending(isPending: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isUpdatePending = isPending
        setViewState(update)
    }

    fun setWorkout(workout: Workout?){
        val update = getCurrentViewStateOrNew()
        update.workout = workout
        setViewState(update)
    }


    fun setListExercise(exercises : List<Exercise>?){
        val update = getCurrentViewStateOrNew()
        update.exerciseList = exercises
        setViewState(update)
    }


    /********************************************************************
        INTERACTIONS
    *********************************************************************/

    fun setWorkoutInteractionNameState(state : WorkoutInteractionState){
        workoutInteractionManager.setWorkoutNameState(state)
    }

    fun setWorkoutInteractionIsActiveState(state : WorkoutInteractionState){
        workoutInteractionManager.setWorkoutIsActiveState(state)
    }

    fun checkEditState() = workoutInteractionManager.checkEditState()

    fun exitEditState() = workoutInteractionManager.exitEditState()

    fun isEditingName() = workoutInteractionManager.isEditingName()

    fun isEditingIsActive() = workoutInteractionManager.isEditingIsActive()


}