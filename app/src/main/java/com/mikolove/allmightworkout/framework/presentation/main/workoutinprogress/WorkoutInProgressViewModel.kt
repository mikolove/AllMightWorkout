package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import androidx.lifecycle.LiveData
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistory
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryWorkout
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.WorkoutInProgressListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerManager
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerState
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


const val WIP_ARE_YOU_SURE_STOP_EXERCISE = "Are you sure to stop this workout ? It will be save at his current state."
const val WIP_ARE_YOU_SURE_QUIT_NO_SAVE = "Are you sure to quit this workout ? It will not be save."
const val WIP_ARE_YOU_SURE_STOP_WORKOUT = "Are you sure to stop this workout. It will save at his current state."


@HiltViewModel
class WorkoutInProgressViewModel
@Inject
constructor(
    private val workoutInProgressListInteractors: WorkoutInProgressListInteractors,
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

            is InsertHistoryEvent -> {
                workoutInProgressListInteractors.insertHistory.insertHistory(
                    workout = stateEvent.workout,
                    idHistoryWorkout = null,
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

    fun loadNextSet(actualSet : ExerciseSet) : Boolean{

        val nextOrderSet = actualSet.order.plus(1)
        val nextSet = getSets()?.find { it.order == nextOrderSet }

        return nextSet?.let {
            setActualSet(it)
            true
        }?:false
    }


    fun getWorkoutById(idWorkout : String) {
        setStateEvent(GetWorkoutByIdEvent(idWorkout = idWorkout))
    }

    fun setExerciseList(exercises : List<Exercise>?){
        val update = getCurrentViewStateOrNew()
        update.exerciseList = exercises
        setViewState(update)
    }

    fun setExerciseSetList(sets : List<ExerciseSet>?){
        val update = getCurrentViewStateOrNew()
        update.setList = sets
        setViewState(update)
    }

    fun setWorkout(workout : Workout?){
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

    fun setExercise(exercise : Exercise?){
        val update = getCurrentViewStateOrNew()
        update.exercise = exercise
        setViewState(update)
    }


    fun saveSet(set :ExerciseSet){

        val updateExercise = getExercise()

        updateExercise?.let {
            it?.sets?.find { it.idExerciseSet == set.idExerciseSet }?.apply {
                startedAt = set.startedAt
                endedAt = set.endedAt
            }
            setExercise(it)
        }
    }

    fun saveExercise(){
        val updateExercise = getExercise()?.copy(
             endedAt = dateUtil.getCurrentTimestamp()
        )
        val updatedExerciseList = getCurrentViewStateOrNew().exerciseList
        updatedExerciseList?.find { it.idExercise == updateExercise?.idExercise }?.apply {
            startedAt = updateExercise?.startedAt
            endedAt = updateExercise?.endedAt
            sets = updateExercise?.sets ?: ArrayList()
        }

        updatedExerciseList?.let {
            setExerciseList(it)
            var isDone = true
            for(exercise in it){
                if(exercise.startedAt == null || exercise.endedAt == null)
                    isDone = false
            }
            setIsWorkoutDone(isDone)
        }
    }

    fun setIsWorkoutDone(isDone : Boolean?){
        val update = getCurrentViewStateOrNew()
        update.isWorkoutDone = isDone
        setViewState(update)
    }


    fun isWorkoutDone() = getCurrentViewStateOrNew().isWorkoutDone ?: false

    fun saveWorkout(workout : Workout, exercises: List<Exercise>){

        val exercisesDone = exercises.filter { it.endedAt != null }

        val updateWorkout = workout.copy(
            endedAt = dateUtil.getCurrentTimestamp(),
            exercises = exercisesDone
        )
        setStateEvent(InsertHistoryEvent(workout = updateWorkout))
    }

}