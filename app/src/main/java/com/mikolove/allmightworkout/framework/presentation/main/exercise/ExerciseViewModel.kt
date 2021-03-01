package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.exercise.ExerciseInteractors
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.UpdateExercise
import com.mikolove.allmightworkout.framework.datasource.cache.database.*
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.common.ListInteractionManager
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseInteractionManager
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseInteractionState
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val EXERCISE_ERRROR_RETRIEVING_ID_WORKOUT = "Error retrieving idExercise from bundle."
const val EXERCISE_ID_WORKOUT_BUNDLE_KEY = "idExercise"
const val EXERCISE_NAME_CANNOT_BE_EMPTY = "Exercise name cannot be empty."
const val INSERT_EXERCISE_ERROR_NO_NAME = "You must set a name to create an exercise."

@HiltViewModel
class ExerciseViewModel
@Inject
constructor(
    private val exerciseInteractors: ExerciseInteractors,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences,
    private val exerciseFactory: ExerciseFactory
) : BaseViewModel<ExerciseViewState>()
{

    override fun initNewViewState(): ExerciseViewState {
        return ExerciseViewState()
    }

    /********************************************************************
        TOOLBAR SELECTION VARS
    *********************************************************************/

    val exerciseListInteractionManager = ListInteractionManager<Exercise>()

    val exerciseToolbarState: LiveData<ListToolbarState>
        get() = exerciseListInteractionManager.toolbarState

    private val exerciseInteractionManager: ExerciseInteractionManager = ExerciseInteractionManager()

    val exerciseNameInteractionState: LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.exerciseNameState

    val exerciseIsActiveInteractionState: LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.exerciseIsActiveState

    /********************************************************************
    INIT BLOC
     *********************************************************************/

    init {
        setExerciseListFilter(
            sharedPreferences.getString(
                PreferenceKeys.EXERCISE_LIST_FILTER,
                EXERCISE_FILTER_DATE_CREATED
            )
        )

        setExerciseOrder(
            sharedPreferences.getString(
                PreferenceKeys.EXERCISE_LIST_ORDER,
                EXERCISE_ORDER_DESC
            )
        )
    }


    override fun handleNewData(data: ExerciseViewState) {
       data.let { viewState ->

            viewState.exerciseToInsert?.let { insertedExercise ->
                setInsertedExercise(insertedExercise)
            }
            viewState.exerciseSelected?.let { exerciseSelected ->
                setExerciseSelected(exerciseSelected)
            }
            viewState.listExercises?.let { listExercises ->
                setListExercises(listExercises)
            }
            viewState.listWorkoutTypes?.let { listWorkoutTypes ->
                setListWorkoutTypes(listWorkoutTypes)
            }
            viewState.listBodyParts?.let { listBodyParts ->
                setListBodyParts(listBodyParts)
            }
            viewState.totalExercises?.let { totalExercises ->
                setTotalExercises(totalExercises)
            }
            viewState.totalBodyParts?.let { totalBodyParts ->
                setTotalBodyParts(totalBodyParts)
            }
            viewState.totalBodyPartsByWorkoutType?.let { totalBodyPartsByWorkoutType ->
                setTotalBodyPartsByWorkoutType(totalBodyPartsByWorkoutType)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job : Flow<DataState<ExerciseViewState>?> = when(stateEvent){

            is InsertExerciseEvent -> {
                exerciseInteractors.insertExercise.insertExercise(
                    name = stateEvent.name,
                    exerciseType = stateEvent.exerciseType,
                    bodyPart = stateEvent.bodyPart,
                    stateEvent = stateEvent
                )
            }

            is GetExerciseByIdEvent -> {
                exerciseInteractors.getExerciseById.getExerciseById(
                    idExercise = stateEvent.idExercise,
                    stateEvent = stateEvent
                )
            }

            is GetExercisesEvent -> {
                exerciseInteractors.getExercises.getExercises(
                    query = getSearchQueryExercises(),
                    filterAndOrder = getOrderExercises()+getFilterExercises(),
                    page = getPageExercises(),
                    stateEvent = stateEvent
                )
            }

            is UpdateExerciseEvent -> {
                if(!isExerciseNameNull()){
                    exerciseInteractors.updateExercise.updateExercise(
                        exercise = getExerciseSelected()!!,
                        stateEvent = stateEvent
                    )
                }else {
                    emitStateMessageEvent(
                        stateMessage = StateMessage(
                            response = Response(
                                message = UpdateExercise.UPDATE_EXERCISE_FAILED,
                                uiComponentType = UIComponentType.Dialog(),
                                messageType = MessageType.Error())
                        ),
                        stateEvent= stateEvent
                    )
                }
            }

            is RemoveExerciseEvent -> {
                exerciseInteractors.removeExercise.removeExercise(
                    exercise = getExerciseSelected()!!,
                    stateEvent = stateEvent
                )
            }

            is RemoveMultipleExercisesEvent -> {
                exerciseInteractors.removeMultipleExercises.removeMultipleExercises(
                    exercises = stateEvent.exercises,
                    stateEvent = stateEvent
                )
            }
            is GetWorkoutTypesEvent -> {
                exerciseInteractors.getWorkoutTypes.getWorkoutTypes(
                    query = "",
                    filterAndOrder = "",
                    1,
                    stateEvent = stateEvent
                )
            }

            is GetBodyPartEvent -> {
                exerciseInteractors.getBodyParts.getBodyParts(
                    query = "",
                    filterAndOrder = "",
                    page = 1,
                    stateEvent = stateEvent
                )
            }

            is GetTotalExercisesEvent -> {
                exerciseInteractors.getTotalExercises.getTotalExercises(stateEvent)
            }

            is GetTotalBodyPartsEvent -> {
                exerciseInteractors.getTotalBodyParts.getTotalBodyParts(stateEvent)
            }

            is GetTotalBodyPartsByWorkoutTypeEvent -> {
                exerciseInteractors.getTotalBodyPartsByWorkoutType.getTotalBodyPartsByWorkoutType(
                    idWorkoutType = stateEvent.idWorkoutType,
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

        launchJob(stateEvent, job)
    }


    /********************************************************************
    TRIGGER STATE EVENTS - FUNCTIONS
     *********************************************************************/

    fun getExerciseById( idExercise : String){
        setStateEvent(GetExerciseByIdEvent(idExercise = idExercise))
    }

    fun createExercise(name : String, exerciseType: ExerciseType, bodyPart: BodyPart) : Exercise = exerciseFactory.createExercise(
        idExercise = null,
        name = name,
        sets = null,
        bodyPart = bodyPart,
        exerciseType = exerciseType,
        isActive = true,
        created_at = null
    )

    fun updateExercise(name : String?, bodyPart: BodyPart,exerciseType: ExerciseType,isActive : Boolean){
        updateExerciseName(name)
        updateExerciseBodyPart(bodyPart)
        updateExerciseExerciseType(exerciseType)
        updateExerciseIsActive(isActive)
    }

    fun isExerciseNameNull() : Boolean{
        val name = getExerciseSelected()?.name
        if(name.isNullOrBlank()) {
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = EXERCISE_NAME_CANNOT_BE_EMPTY,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
            return true
        }else{
            return false
        }
    }


    /********************************************************************
        LIST EXERCISES MANAGING
     *********************************************************************/

    //Launch actual query reseting Pagination
    fun exercisesStartNewSearch(){
        setExerciseQueryExhausted(false)
        resetPageExercises()
        loadExercises()
    }

    //Launch actual query keeping Pagination
    fun refreshExerciseSearchQuery(){
        setExerciseQueryExhausted(false)
        loadExercises()
    }

    fun loadExercises(){
        setStateEvent(GetExercisesEvent())
    }

    /********************************************************************
        OTHERS LIST MANAGING
    *********************************************************************/

    fun reloadBodyParts(){
        clearListBodyParts()
        loadBodyParts()
    }

    fun reloadWorkoutTypes(){
        clearListWorkoutTypes()
        loadWorkoutTypes()
    }

    fun loadTotalExercises(){
        setStateEvent(GetTotalExercisesEvent())
    }

    fun loadTotalWorkoutTypes(idWorkoutType : String){
        setStateEvent(GetTotalBodyPartsByWorkoutTypeEvent(idWorkoutType))
    }

    fun loadTotalBodyParts(){
        setStateEvent(GetTotalBodyPartsEvent())
    }

    fun loadWorkoutTypes(){
        setStateEvent(GetWorkoutTypesEvent())
    }

    fun loadBodyParts(){
        printLogD("HomeViewModel","Load body parts")
        setStateEvent(GetBodyPartEvent())
    }

    fun saveFilterExercisesOptions(filter: String, order: String){
        editor.putString(PreferenceKeys.EXERCISE_LIST_FILTER, filter)
        editor.apply()

        editor.putString(PreferenceKeys.EXERCISE_LIST_ORDER, order)
        editor.apply()
    }

    /********************************************************************
        GETTERS - QUERY
    *********************************************************************/

    fun getFilterExercises(): String {
        return getCurrentViewStateOrNew().exercise_list_filter
            ?: EXERCISE_FILTER_NAME
    }

    fun getOrderExercises(): String {
        return getCurrentViewStateOrNew().exercise_list_order
            ?: EXERCISE_ORDER_BY_ASC_NAME
    }

    fun getSearchQueryExercises(): String {
        return getCurrentViewStateOrNew().searchQueryExercises
            ?: return ""
    }

    private fun getPageExercises(): Int{
        return getCurrentViewStateOrNew().pageExercises
            ?: return 1
    }

    /********************************************************************
        GETTERS - VIEWSTATE AND OTHER
    *********************************************************************/

    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun getExercises() = getCurrentViewStateOrNew().listExercises

    fun getWorkoutTypes() = getCurrentViewStateOrNew().listWorkoutTypes

    fun getBodyParts() = getCurrentViewStateOrNew().listBodyParts

    fun getTotalExercises() = getCurrentViewStateOrNew().totalExercises ?: 0

    fun getTotalBodyParts() = getCurrentViewStateOrNew().totalBodyParts

    fun getTotalBodyPartByWorkoutType() = getCurrentViewStateOrNew().totalBodyPartsByWorkoutType

    fun getWorkoutTypeSelected() = getCurrentViewStateOrNew().workoutTypeSelected

    fun getExerciseToInsert() = getCurrentViewStateOrNew().exerciseToInsert ?: null

    fun getExerciseSelected() = getCurrentViewStateOrNew().exerciseSelected ?: null

    fun getIsUpdatePending() : Boolean = getCurrentViewStateOrNew().isUpdatePending ?:false

    fun getExercisesListSize() = getCurrentViewStateOrNew().listExercises?.size?: 0

    fun isExercisesPaginationExhausted() = getExercisesListSize() >= getTotalExercises()

    fun isSearchActive() = getCurrentViewStateOrNew().searchActive ?: false

    /********************************************************************
    SETTERS - VIEWSTATE AND OTHER
     *********************************************************************/

    fun clearListExercises(){
        val update = getCurrentViewStateOrNew()
        update.listExercises = ArrayList()
        setViewState(update)
    }

    fun clearListWorkoutTypes(){
        val update = getCurrentViewStateOrNew()
        update.listWorkoutTypes = ArrayList()
        setViewState(update)
    }

    fun clearListBodyParts(){
        val update = getCurrentViewStateOrNew()
        update.listBodyParts = ArrayList()
        setViewState(update)
    }

    fun updateExerciseName(name :String?){
        if(name.isNullOrBlank()){
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = ExerciseCacheEntity.nullNameError(),
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        )
                    )
                )
            )
        }else{
            val update = getCurrentViewStateOrNew()
            val updateExercise = update.exerciseSelected?.copy(
                name = name
            )
            update.exerciseSelected = updateExercise
            setViewState(update)
        }
    }

    fun updateExerciseIsActive(isActive : Boolean){
        val update = getCurrentViewStateOrNew()
        val updateExercise = update.exerciseSelected?.copy(
            isActive = isActive
        )
        update.exerciseSelected = updateExercise
        setViewState(update)
    }

    fun updateExerciseBodyPart(bodyPart: BodyPart){
        val update = getCurrentViewStateOrNew()
        val updateExercise = update.exerciseSelected?.copy(
            bodyPart = bodyPart
        )
        update.exerciseSelected = updateExercise
        setViewState(update)
    }

    fun updateExerciseExerciseType(exerciseType: ExerciseType){
        val update = getCurrentViewStateOrNew()
        val updateExercise = update.exerciseSelected?.copy(
            exerciseType = exerciseType
        )
        update.exerciseSelected = updateExercise
        setViewState(update)
    }

    fun setIsSearchActive(isActive: Boolean){
        val update = getCurrentViewStateOrNew()
        update.searchActive = isSearchActive()
        setViewState(update)
    }

    fun setIsUpdatePending(isPending: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isUpdatePending = isPending
        setViewState(update)
    }

    fun setExerciseSelected(exercise : Exercise?){
        val update = getCurrentViewStateOrNew()
        update.exerciseSelected = exercise
        setViewState(update)
    }

    fun setExerciseListFilter(filter : String?){
        filter?.let {
            val update = getCurrentViewStateOrNew()
            update.exercise_list_filter = filter
            setViewState(update)
        }
    }

    fun setInsertedExercise(exercise :Exercise?){
        val update = getCurrentViewStateOrNew()
        update.exerciseToInsert = exercise
        setViewState(update)
    }

    fun setExerciseOrder(order: String?){
        val update = getCurrentViewStateOrNew()
        update.exercise_list_order = order
        setViewState(update)
    }

    private fun setListExercises(exercises : ArrayList<Exercise>){
        val update = getCurrentViewStateOrNew()
        update.listExercises = exercises
        setViewState(update)
    }

    private fun setListWorkoutTypes(workoutTypes : ArrayList<WorkoutType>){
        val update = getCurrentViewStateOrNew()
        update.listWorkoutTypes = workoutTypes
        setViewState(update)
    }

    private fun setListBodyParts(bodyParts : ArrayList<BodyPart>){
        val update = getCurrentViewStateOrNew()
        update.listBodyParts = bodyParts
        setViewState(update)
    }

    private fun setTotalBodyParts(total : Int){
        val update = getCurrentViewStateOrNew()
        update.totalBodyParts = total
        setViewState(update)
    }

    private fun setTotalExercises(total : Int){
        val update = getCurrentViewStateOrNew()
        update.totalExercises = total
        setViewState(update)
    }

    private fun setTotalBodyPartsByWorkoutType(total : Int){
        val update = getCurrentViewStateOrNew()
        update.totalBodyPartsByWorkoutType = total
        setViewState(update)
    }

    fun setQueryExercises(string : String?){
        val update = getCurrentViewStateOrNew()
        update.searchQueryExercises = string
        setViewState(update)
    }

    private fun setWorkoutTypeSelected(workoutType : WorkoutType){
        val update = getCurrentViewStateOrNew()
        update.workoutTypeSelected = workoutType
        setViewState(update)
    }

    /********************************************************************
    TOOLBARS GETTERS AND SETTERS - EXERCISES
     *********************************************************************/


    fun getSelectedExercises() = exerciseListInteractionManager.getSelectedItems()

    fun setExerciseToolbarState(state: ListToolbarState)
            = exerciseListInteractionManager.setToolbarState(state)

    fun isExerciseMultiSelectionStateActive()
            = exerciseListInteractionManager.isMultiSelectionStateActive()

    fun addOrRemoveExerciseFromSelectedList(exercise: Exercise)
            = exerciseListInteractionManager.addOrRemoveItemFromSelectedList(exercise)

    fun isExerciseSelected(exercise: Exercise): Boolean
            = exerciseListInteractionManager.isItemSelected(exercise)

    fun clearSelectedExercises() = exerciseListInteractionManager.clearSelectedItems()

    private fun removeSelectedExercisesFromList(){
        val update = getCurrentViewStateOrNew()
        update.listExercises?.removeAll(getSelectedExercises())
        setViewState(update)
        clearSelectedExercises()
    }

    fun deleteExercises(){
        if(getSelectedExercises().size > 0){
            setStateEvent(RemoveMultipleExercisesEvent(getSelectedExercises()))
            removeSelectedExercisesFromList()
        }
        else{
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = RemoveMultipleExercises.DELETE_EXERCISES_YOU_MUST_SELECT,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
        }
    }

    /********************************************************************
        ListExercises PageManagement
     *********************************************************************/

    fun resetPageExercises(){
        val update = getCurrentViewStateOrNew()
        update.pageExercises = 1
        setViewState(update)
    }

    private fun incrementPageExercisesNumber(){
        val update = getCurrentViewStateOrNew()
        val page = update.copy().pageExercises ?: 1
        update.pageExercises = page.plus(1)
        setViewState(update)
    }

    fun nextPageExercises(){
        if(!isExercisesQueryExhausted()){
            incrementPageExercisesNumber()
            setStateEvent(GetExercisesEvent())
        }
    }

    fun isExercisesQueryExhausted(): Boolean{
        return getCurrentViewStateOrNew().isExercisesQueryExhausted?: true
    }

    fun setExerciseQueryExhausted(isExhausted : Boolean){
        val update = getCurrentViewStateOrNew()
        update.isExercisesQueryExhausted = isExhausted
        setViewState(update)
    }

    /********************************************************************
    INTERACTIONS
     *********************************************************************/

    fun setExerciseInteractionNameState(state : ExerciseInteractionState){
        exerciseInteractionManager.setExerciseNameState(state)
    }

    fun setExerciseInteractionIsActiveState(state : ExerciseInteractionState){
        exerciseInteractionManager.setExerciseIsActiveState(state)
    }

    fun setExerciseInteractionBodyPartState(state : ExerciseInteractionState){
        exerciseInteractionManager.setExerciseBodyPartState(state)
    }

    fun setExerciseInteractionExerciseTypeState(state : ExerciseInteractionState){
        exerciseInteractionManager.setExerciseExerciseTypeState(state)
    }

    fun checkEditState() = exerciseInteractionManager.checkEditState()

    fun exitEditState() = exerciseInteractionManager.exitEditState()

    fun isEditingName() = exerciseInteractionManager.isEditingName()

    fun isEditingIsActive() = exerciseInteractionManager.isEditingIsActive()

    fun isEditingBodyPart() = exerciseInteractionManager.isEditingBodyPart()

    fun isEditingExerciseType() = exerciseInteractionManager.isEditingExerciseType()

}

