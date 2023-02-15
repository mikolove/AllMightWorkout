package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

import androidx.lifecycle.*
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.exercise.ExerciseInteractors
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val EXERCISE_DETAIL_EXIT_DIALOG_TITLE = "Exit exercise edition"
const val EXERCISE_DETAIL_EXIT_DIALOG_CONTENT = "Modification are not saved. Are you sure to quit ? work will be lost."

@HiltViewModel
class ExerciseDetailViewModel
@Inject
constructor(
    private val sessionManager : SessionManager,
    private val exerciseFactory: ExerciseFactory,
    private val exerciseSetFactory: ExerciseSetFactory,
    private val exerciseInteractors : ExerciseInteractors,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel()
{
    val state : MutableLiveData<ExerciseDetailState> = MutableLiveData(ExerciseDetailState())

    init {

        savedStateHandle.get<String>("idExercise")?.let { idExercise ->
            this.state.value = state.value?.copy(idExercise = idExercise)
        }
        onTriggerEvent(ExerciseDetailEvents.GetExerciseTypes)
        onTriggerEvent(ExerciseDetailEvents.GetWorkoutTypes)
    }

    fun onTriggerEvent(event: ExerciseDetailEvents){
        when(event){
            is ExerciseDetailEvents.CreateExercise->{
                createExercise()
            }
            is ExerciseDetailEvents.GetExerciseById->{
                getExerciseById(event.idExercise)
            }
            is ExerciseDetailEvents.GetWorkoutTypes->{
                getWorkoutTypes()
            }
            is ExerciseDetailEvents.AddSet->{
                addSet()
            }
            is ExerciseDetailEvents.UpdateIsInCache->{
                updateIsInCache(event.isInCache)
            }
            is ExerciseDetailEvents.RemoveSet->{
                removeSet(event.set)
            }
            is ExerciseDetailEvents.UpdateSet->{
                updateSet(event.set)
            }
            is ExerciseDetailEvents.GetExerciseTypes->{
                getExerciseTypes()
            }
            is ExerciseDetailEvents.GetBodyParts->{
                getBodyParts(event.idWorkoutType)
            }
            is ExerciseDetailEvents.UpdateLoadInitialValues->{
                updateLoadInitialValue(event.load)
            }
            is ExerciseDetailEvents.UpdateExerciseName->{
                updateExerciseName(event.name)
            }
            is ExerciseDetailEvents.UpdateExerciseIsActive->{
                updateExerciseIsActive(event.isActive)
            }
            is ExerciseDetailEvents.UpdateExerciseBodyPart->{
                updateExerciseBodyPart(event.bodyPart)
            }
            is ExerciseDetailEvents.UpdateExerciseExerciseType->{
                updateExerciseExerciseType(event.exerciseType)
            }
            is ExerciseDetailEvents.InsertExercise->{
                insertExercise()
            }
            is ExerciseDetailEvents.UpdateExercise->{
                updateExercise()
            }
            is ExerciseDetailEvents.OnUpdateIsPending->{
                onUpdateIsPending(event.isPending)
            }
            is ExerciseDetailEvents.Error->{

            }
            is ExerciseDetailEvents.LaunchDialog->{
                appendToMessageQueue(event.message)
            }
            is ExerciseDetailEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }



    /********************************************************************
    INTERACTORS
     *********************************************************************/

    private fun addSet(){
        state.value?.let { state ->
            state.exercise?.sets?.let { sets ->
                val set = createExerciseSet(sets.size.plus(1))
                val updatedSets = sets.toMutableList()
                updatedSets.add(set)
                val updateExercise = state.exercise.copy(sets = updatedSets)
                this.state.value = state.copy(exercise = updateExercise, isUpdatePending = true)
            }
        }
    }

    private fun removeSet(set : ExerciseSet){
        state.value?.let { state ->
            state.exercise?.sets?.let { sets ->
                val updatedSets = sets
                    .filter { it.idExerciseSet != set.idExerciseSet }
                    .map{ exerciseSet ->
                        if(exerciseSet.order > set.order){
                            exerciseSet.copy(order = exerciseSet.order.minus(1))
                        }else{
                            exerciseSet
                        }
                    }
                val updatedExercise = state.exercise.copy(sets = updatedSets)
                this.state.value = state.copy(exercise = updatedExercise, isUpdatePending = true)
            }
        }
    }

    private fun updateIsInCache(isInCache : Boolean){
        state.value?.let { state ->
            this.state.value = state.copy(isInCache = isInCache)
        }
    }

    private fun onUpdateIsPending(isPending : Boolean){
        state.value?.let { state ->
            this.state.value = state.copy(isUpdatePending = isPending)
        }
    }


    private fun updateExerciseName(name : String){
        state.value?.let { state ->
            val exercise = state.exercise?.copy(name = name)
            this.state.value = state.copy(exercise = exercise)
        }
    }

    private fun updateExerciseIsActive(isActive : Boolean){
        state.value?.let { state ->
            val exercise = state.exercise?.copy(isActive = isActive)
            this.state.value = state.copy(exercise = exercise)
        }
    }

    private fun updateExerciseBodyPart(bodyPart: BodyPart?){
        state.value?.let { state ->
            val exercise = state.exercise?.copy(bodyPart = bodyPart)
            this.state.value = state.copy(exercise = exercise)
        }
    }

    private fun updateExerciseExerciseType(exerciseType : ExerciseType){
        state.value?.let { state ->
            val exercise = state.exercise?.copy(exerciseType = exerciseType)
            this.state.value = state.copy(exercise = exercise)
        }
    }

    private fun updateSet(set : ExerciseSet){
        state.value?.let { state ->
            state.exercise?.sets?.let { sets ->
                val updatedSets = sets.toMutableList().map {
                    if(it.idExerciseSet == set.idExerciseSet) set else it
                }
                val updatedExercise = state.exercise.copy(sets = updatedSets)
                this.state.value = state.copy(exercise = updatedExercise)
            }
        }
    }

    private fun updateWorkoutType(workoutType : String){
        //TODO : Could be unneeded
    }

    private fun updateLoadInitialValue(load : Boolean){
        state.value?.let {  state ->
            this.state.value = state.copy(loadInitialValues = load)
        }
    }

    fun getExerciseWorkoutType() : WorkoutType? {
        return state.value?.let { state ->
            state.exercise?.let{ exercise ->
                state.workoutTypes.find{
                    it.bodyParts?.contains(exercise.bodyPart) == true
                }
            }
        }
    }

    private fun createExercise(){
        state.value?.let { state ->

            val sets = listOf(createExerciseSet(1))

            val newExercise = exerciseFactory.createExercise(
                idExercise = null,
                name = null,
                sets = sets,
                bodyPart = null,
                exerciseType = ExerciseType.REP_EXERCISE,
                isActive = true,
                created_at = null
            )

            this.state.value = state.copy(exercise = newExercise)

            onTriggerEvent(ExerciseDetailEvents.UpdateLoadInitialValues(true))
        }
    }


    private fun createExerciseSet(order : Int) : ExerciseSet = exerciseSetFactory.createExerciseSet(
        idExerciseSet = null,
        reps = null,
        weight = null,
        time = null,
        restTime = null,
        order = order,
        created_at = null
    )

    private fun getExerciseById(idExercise : String){
        state.value?.let { state ->

            exerciseInteractors.getExerciseById.execute(
                idExercise = idExercise
            ).onEach { dataState ->

                this.state.value = state.copy(isLoading = dataState?.isLoading)

                dataState?.data?.let { exercise ->

                    this.state.value = state.copy(exercise = exercise)

                    getExerciseWorkoutType()?.let {
                        onTriggerEvent(ExerciseDetailEvents.GetBodyParts(it.idWorkoutType))
                    }
                    onTriggerEvent(ExerciseDetailEvents.UpdateIsInCache(true))
                    onTriggerEvent(ExerciseDetailEvents.UpdateLoadInitialValues(true))
                }

                dataState?.message?.let { message ->
                    appendToMessageQueue(message)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun insertExercise(){
        state.value?.let { state ->

            state.exercise?.let{ exercise ->

                sessionManager.state.value?.idUser?.let { idUser ->

                    exerciseInteractors.insertExercise
                        .executeNew(
                            idUser = idUser,
                            idExercise = exercise.idExercise,
                            name = exercise.name,
                            sets = exercise.sets,
                            exerciseType = exercise.exerciseType,
                            bodyPart = exercise.bodyPart
                        )
                        .onEach { dataState ->
                            this.state.value = state.copy(isLoading = dataState?.isLoading)

                            dataState?.data?.let {
                                this.state.value = state.copy(isInCache = true, isUpdatePending = false, isUpdateDone = true)
                            }

                            dataState?.message?.let { message ->
                                appendToMessageQueue(message)
                            }
                        }
                        .launchIn(viewModelScope)

                }
            }
        }
    }

    private fun updateExercise(){
        state.value?.let { state ->
            state.exercise?.let { exercise ->
                exerciseInteractors.updateExercise.execute(
                    exercise
                ).onEach { dataState ->
                    this.state.value = state.copy(isLoading = dataState?.isLoading)

                    dataState?.data?.let {
                        this.state.value = state.copy(isUpdatePending = false, isUpdateDone = true)
                    }

                    dataState?.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }


    private fun getWorkoutTypes(){
        state.value?.let { state ->
            exerciseInteractors.getWorkoutTypes.execute(
                "",
                "",
                1
            ).onEach { dataState ->

                this.state.value = state.copy(isLoading = dataState?.isLoading)

                dataState?.data?.let { workoutTypes ->

                    this.state.value = state.copy(workoutTypes = workoutTypes.sortedBy { it.name })

                    if(state.idExercise.isNotBlank()){
                        onTriggerEvent(ExerciseDetailEvents.GetExerciseById(state.idExercise))
                    }else{
                        onTriggerEvent(ExerciseDetailEvents.CreateExercise)
                    }
                }

                dataState?.message?.let { message ->
                    appendToMessageQueue(message)
                }
            }.launchIn(viewModelScope)
        }
    }
    private fun getExerciseTypes(){
        state.value?.let{ state ->
            this.state.value = state.copy(exerciseTypes = ExerciseType.values().toList().sortedBy { it.type })
        }
    }
    private fun getBodyParts(idWorkoutType : String){
        state.value?.let { state ->
            if(state.workoutTypes.isNotEmpty()){
                val bodyParts = state.workoutTypes.find { it.idWorkoutType == idWorkoutType }?.bodyParts ?: listOf()
                this.state.value = state.copy(bodyParts = bodyParts.sortedBy { it.name })
            }
        }
    }
    fun isExerciseValid() : Boolean{

        state.value?.let { state ->
            state.exercise?.let { exercise ->
                return !(exercise.name.isBlank() || exercise.bodyPart == null)
            }
        }

        return false
    }


    /********************************************************************
    INTERACTIONS EXERCISE & SET STATE
     *********************************************************************/

    private val exerciseInteractionManager: ExerciseInteractionManager = ExerciseInteractionManager()

    //Exercise
    val exerciseNameInteractionState: LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.nameState

    val exerciseIsActiveInteractionState: LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.isActiveState

    val exerciseBodyPartInteractionState : LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.bodyPartState

    val exerciseWorkoutTypeInteractionState : LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.workoutTypeState

    val exerciseTypeInteractionState : LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.exerciseTypeState


    //Set Exercise
    fun setInteractionNameState(state : ExerciseInteractionState){
        exerciseInteractionManager.setNameState(state)
    }

    fun setInteractionIsActiveState(state : ExerciseInteractionState){
        exerciseInteractionManager.setIsActiveState(state)
    }

    fun setInteractionWorkoutTypeState(state : ExerciseInteractionState){
        exerciseInteractionManager.setWorkoutTypeState(state)
    }
    fun setInteractionBodyPartState(state : ExerciseInteractionState){
        exerciseInteractionManager.setBodyPartState(state)
    }

    fun setInteractionExerciseTypeState(state : ExerciseInteractionState){
        exerciseInteractionManager.setExerciseTypeState(state)
    }

    //Functions
    fun checkExerciseEditState() = exerciseInteractionManager.checkEditState()

    fun exitExerciseEditState() = exerciseInteractionManager.exitEditState()

    fun isEditingName() = exerciseInteractionManager.isEditingName()

    fun isEditingIsActive() = exerciseInteractionManager.isEditingIsActive()

    fun isEditingWorkoutType() = exerciseInteractionManager.isEditingWorkoutType()

    fun isEditingBodyPart() = exerciseInteractionManager.isEditingBodyPart()

    fun isEditingExerciseType() = exerciseInteractionManager.isEditingExerciseType()


    /********************************************************************
    QUEUE MANAGING
     *********************************************************************/

    private fun removeHeadFromQueue(){
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            }catch (e: Exception){
                printLogD("ExerciseDetailViewModel","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        state.value?.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    queue.add(messageBuild)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }

}