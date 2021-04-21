package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import androidx.lifecycle.LiveData
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.WorkoutInProgressListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerManager
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerState
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


const val WIP_ARE_YOU_SURE_STOP_EXERCISE = "Are you sure to stop this exercise. It will be save at his current state."

@HiltViewModel
class WorkoutInProgressViewModel
@Inject
constructor(
    private val workoutInProgressListInteractors: WorkoutInProgressListInteractors,
    private val historyWorkoutFactory : HistoryWorkoutFactory,
    private val historyExerciseFactory: HistoryExerciseFactory,
    private val historyExerciseSetFactory: HistoryExerciseSetFactory,
    private val dateUtil: DateUtil
    ) : BaseViewModel<WorkoutInProgressViewState>() {


    override fun handleNewData(data: WorkoutInProgressViewState) {
        data.let { viewState ->

            viewState.workout?.let { workout ->
                workout.startedAt = dateUtil.getCurrentTimestamp()
                setWorkout(workout)
            }

        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job : Flow<DataState<WorkoutInProgressViewState>?> = when(stateEvent) {

            is GetWorkoutByIdEvent -> {
                workoutInProgressListInteractors.getWorkoutById.getWorkoutById(
                    idWorkout= stateEvent.idWorkout,
                    stateEvent = stateEvent
                )
            }

            is CreateStateMessageEvent -> {
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

    override fun initNewViewState(): WorkoutInProgressViewState {
        return WorkoutInProgressViewState()
    }


    /*

    Workout In Progress
    */

    fun getWorkout() : Workout? = getCurrentViewStateOrNew().workout ?: null
    fun getExerciseList() : List<Exercise> = getCurrentViewStateOrNew().exerciseList ?: ArrayList()

    private var validatedSetList : ArrayList<ExerciseSet> = ArrayList()
    fun getValidatedSetList() = validatedSetList
    private fun setValidatedSetList(list : ArrayList<ExerciseSet>){
        validatedSetList = list
    }

    fun loadNextSet(actualSet : ExerciseSet) : Boolean{

        val nextOrderSet = actualSet.order.plus(1)
        val nextSet = getSets()?.find { it.order == nextOrderSet }

        return nextSet?.let {
            setActualSet(it)
            true
        }?:false
    }

    fun addSetToValidated(set : ExerciseSet){
        var updateValidated = getValidatedSetList()
        updateValidated.add(set)
        setValidatedSetList(updateValidated)
    }

    fun cleanValidatedSetList(){
        validatedSetList = ArrayList()
    }

    fun getWorkoutById(idWorkout : String) {
        setStateEvent(GetWorkoutByIdEvent(idWorkout = idWorkout))
    }

    fun setExerciseList(exercises : List<Exercise>){
        val update = getCurrentViewStateOrNew()
        update.exerciseList = exercises
        setViewState(update)
    }

    fun setExerciseSetList(sets : List<ExerciseSet>){
        val update = getCurrentViewStateOrNew()
        update.setList = sets
        setViewState(update)
    }

    private fun setWorkout(workout : Workout){
        val update = getCurrentViewStateOrNew()
        update.workout = workout
        setViewState(update)
    }


    val chronometerManager = ChronometerManager()
    val chronometerState: LiveData<ChronometerState>
        get() = chronometerManager.getChronometerSate()


    fun getExercise() : Exercise? = getCurrentViewStateOrNew().exercise
    fun getSets() : List<ExerciseSet> = getCurrentViewStateOrNew().setList ?: ArrayList()

    fun setActualSet(set : ExerciseSet){
        val update = getCurrentViewStateOrNew()
        update.actualSet = set
        setViewState(update)
    }

    fun getActualSet() = getCurrentViewStateOrNew().actualSet

    fun geTotalSets() = getCurrentViewStateOrNew().setList?.size?.plus(1) ?: 0

    fun setExercise(exercise : Exercise ){
        val update = getCurrentViewStateOrNew()
        update.exercise = exercise
        setViewState(update)
    }


    fun saveExercise(){

        //Get validated set
        val setsValidated = getValidatedSetList()

        //Update in progress exercise
        val updateExercise = getExercise()?.copy(
             endedAt = dateUtil.getCurrentTimestamp()
        )
        setsValidated.forEach { setValidated ->
            updateExercise?.sets?.find { it.idExerciseSet == setValidated.idExerciseSet }?.apply {
                startedAt = setValidated.startedAt
                endedAt = setValidated.endedAt
            }
        }

        //Upadte list
        val updatedExerciseList = getCurrentViewStateOrNew().exerciseList
        updatedExerciseList?.find { it.idExercise == updateExercise?.idExercise }?.apply {
            startedAt = updateExercise?.startedAt
            endedAt = updateExercise?.endedAt
            sets = updateExercise?.sets ?: ArrayList()
        }

        updatedExerciseList?.let { setExerciseList(it) }
    }


}