package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.exercise.ExerciseInteractors
import com.mikolove.allmightworkout.business.interactors.main.exercise.InsertExercise.Companion.INSERT_EXERCISE_FAILED
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.UpdateExercise
import com.mikolove.allmightworkout.framework.datasource.cache.database.*
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseSetCacheEntity
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.common.ListInteractionManager
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val EXERCISE_ERRROR_RETRIEVING_ID_WORKOUT = "Error retrieving idExercise from bundle."
const val EXERCISE_ID_WORKOUT_BUNDLE_KEY = "idExercise"
const val EXERCISE_INCOMPLETE = "Exercise is incomplete, please fill the form values."
const val INSERT_EXERCISE_ERROR_NO_NAME = "You must set a name to create an exercise."

@HiltViewModel
class ExerciseViewModel
@Inject
constructor(
    private val exerciseInteractors: ExerciseInteractors,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences,
    private val exerciseFactory: ExerciseFactory,
    private val exerciseSetFactory: ExerciseSetFactory
) : BaseViewModel<ExerciseViewState>()
{

    override fun initNewViewState(): ExerciseViewState {
        return ExerciseViewState()
    }

    /********************************************************************
    LIVEDATA
     *********************************************************************/

    private val _exerciseTypeState : MutableLiveData<ExerciseType> = MutableLiveData()

    val exerciseTypeState : LiveData<ExerciseType>
        get() = _exerciseTypeState

    fun getExerciseTypeState() : ExerciseType = _exerciseTypeState.value?: ExerciseType.REP_EXERCISE

    fun setExerciseTypeState(exerciseType: ExerciseType){
        _exerciseTypeState.value =  exerciseType
    }
    fun clearExerciseTypeState(){
        _exerciseTypeState.value = null
    }

    /********************************************************************
    TOOLBAR SELECTION VARS
     *********************************************************************/

    val exerciseListInteractionManager = ListInteractionManager<Exercise>()

    val exerciseToolbarState: LiveData<ListToolbarState>
        get() = exerciseListInteractionManager.toolbarState

    private val exerciseInteractionManager: ExerciseInteractionManager = ExerciseInteractionManager()

    private val exerciseSetInteractionManager: ExerciseSetInteractionManager = ExerciseSetInteractionManager()

    val exerciseNameInteractionState: LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.nameState

    val exerciseBodyPartInteractionState : LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.bodyPartState

    val exerciseWorkoutTypeInteractionState : LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.workoutTypeState

    val exerciseTypeInteractionState : LiveData<ExerciseInteractionState>
        get() = exerciseInteractionManager.exerciseTypeState


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

            printLogD("ExerciseViewModel","handleNewData")

            viewState.isExistExercise?.let { isExistExercise ->
                setIsExistExercise(isExistExercise)
            }
            viewState.exerciseSelected?.let { exerciseSelected ->
                setExerciseSelected(exerciseSelected)
            }

            viewState.cachedExerciseSetsByIdExercise?.let { cachedSets ->
                setCachedExerciseSetsByIdExercise(cachedSets)
                setCachedExercisesSetsExhausted(true)
            }
            viewState.listExercises?.let { listExercises ->
                setListExercises(listExercises)
            }
            viewState.listWorkoutTypes?.let { listWorkoutTypes ->
                setListWorkoutTypes(listWorkoutTypes)
                setWorkoutTypeExhausted(true)
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

            is InsertExerciseSetEvent -> {

                exerciseInteractors.insertExerciseSet.insertExerciseSet(
                    idExerciseSet = stateEvent.exerciseSet.idExerciseSet,
                    reps = stateEvent.exerciseSet.reps,
                    weight = stateEvent.exerciseSet.weight,
                    time = stateEvent.exerciseSet.time,
                    restTime = stateEvent.exerciseSet.restTime,
                    idExercise = stateEvent.idExercise,
                    stateEvent = stateEvent
                )
            }

            is InsertMultipleExerciseSetEvent ->  {
                exerciseInteractors.insertMultipleExerciseSet.insertMultipleExerciseSet(
                    sets = stateEvent.sets,
                    idExercise =  stateEvent.idExercise,
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

            is InsertExerciseEvent -> {
                if(isExerciseValid()){
                    exerciseInteractors.insertExercise.insertExercise(
                        name = stateEvent.name,
                        exerciseType = stateEvent.exerciseType,
                        bodyPart = stateEvent.bodyPart,
                        stateEvent = stateEvent,
                        sets = getExerciseSelected()?.sets
                    )
                }else{
                    emitStateMessageEvent(
                        stateMessage = StateMessage(
                            response = Response(
                                message = INSERT_EXERCISE_FAILED,
                                uiComponentType = UIComponentType.Toast(),
                                messageType = MessageType.Error())
                        ),
                        stateEvent= stateEvent
                    )
                }
            }


            is UpdateExerciseEvent -> {
                if(isExerciseValid()){
                    exerciseInteractors.updateExercise.updateExercise(
                        exercise = getExerciseSelected()!!,
                        stateEvent = stateEvent
                    )
                }else {
                    emitStateMessageEvent(
                        stateMessage = StateMessage(
                            response = Response(
                                message = UpdateExercise.UPDATE_EXERCISE_FAILED,
                                uiComponentType = UIComponentType.Toast(),
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

            is GetBodyPartByWorkoutTypeEvent ->{
                exerciseInteractors.getBodyPartsByWorkoutType.getBodyPartsByWorkoutType(
                    idWorkoutType = stateEvent.idWorkoutType,
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

            is GetExerciseSetByIdExerciseEvent -> {
                exerciseInteractors.getExerciseSetByIdExercise.getExerciseSetByIdExercise(
                    idExercise = stateEvent.idExercise,
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

    fun createExercise() : Exercise = exerciseFactory.createExercise(
        idExercise = null,
        name = null,
        sets = null,
        bodyPart = null,
        exerciseType = ExerciseType.REP_EXERCISE,
        isActive = true,
        created_at = null
    )

    fun createExerciseSet() : ExerciseSet = exerciseSetFactory.createExerciseSet(
        idExerciseSet = null,
        reps = null,
        weight = null,
        time = null,
        restTime = null,
        created_at = null
    )

    fun isExerciseValid() : Boolean{

        val exercise = getExerciseSelected() ?: return false

        val name = exercise.name
        val bodyPart = exercise.bodyPart

        if(name.isNullOrBlank() || bodyPart == null) {
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = EXERCISE_INCOMPLETE,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
            return false
        }else{
            return true
        }
    }



    fun insertExercise(){
        val exercise = getExerciseSelected()
        exercise?.let {
            if(isExerciseValid()){
                if( it.bodyPart != null){
                    setStateEvent(InsertExerciseEvent(
                        it.name,
                        it.exerciseType,
                        it.bodyPart!!))
                }
            }
        }
    }

    fun updateExercise(){
        val exercise = getExerciseSelected()
        exercise?.let {
            if(isExerciseValid()){
                if( it.bodyPart != null){
                    setStateEvent(UpdateExerciseEvent(

                    ))
                }
            }
        }
    }

    fun deleteExercise(){
        val exercise = getExerciseSelected()
        exercise?.let {
            setStateEvent(RemoveExerciseEvent())
        }
    }

    fun insertSets() {
        val idExerciseSet = getExerciseSelected()?.idExercise
        val sets = ArrayList(getExerciseSelected()?.sets)
        if(idExerciseSet != null){
            if( sets != null){
                setStateEvent(InsertMultipleExerciseSetEvent(
                    sets = sets,
                    idExercise = idExerciseSet)
                )
            }
        }
    }

    fun addSet(){
        val set = createExerciseSet()
        val sets = ArrayList(getExerciseSelected()?.sets)
        sets.add(set)
        updateExerciseSets(sets)
    }

    fun removeSet(exerciseSet : ExerciseSet){
        var sets = ArrayList(getExerciseSelected()?.sets)
        sets.remove(exerciseSet)
        updateExerciseSets(sets)
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
        setWorkoutTypeExhausted(false)
        setStateEvent(GetWorkoutTypesEvent())
    }

    fun loadCachedExerciseSets(idExercise: String){
        setCachedExercisesSetsExhausted(false)
        setStateEvent(GetExerciseSetByIdExerciseEvent(idExercise = idExercise))
    }

    fun loadBodyPartsByWorkoutTypes(idWorkoutType: String){
        setStateEvent(GetBodyPartByWorkoutTypeEvent(idWorkoutType = idWorkoutType))
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

    fun isWorkoutTypesExhausted() = getCurrentViewStateOrNew().isWorkoutTypesExhausted ?: false

    fun isExistExercise() = getCurrentViewStateOrNew().isExistExercise ?: false

    fun getExerciseSelected() = getCurrentViewStateOrNew().exerciseSelected ?: null

    fun getIsUpdatePending() : Boolean = getCurrentViewStateOrNew().isUpdatePending ?:false

    fun getExercisesListSize() = getCurrentViewStateOrNew().listExercises?.size?: 0

    fun isExercisesPaginationExhausted() = getExercisesListSize() >= getTotalExercises()

    fun isSearchActive() = getCurrentViewStateOrNew().searchActive ?: false

    fun getExerciseSelectedWorkoutType() : WorkoutType? {
        val exerciseSelected = getExerciseSelected()
        val workoutTypes = getWorkoutTypes()
        return workoutTypes?.let { workoutTypes ->
            exerciseSelected?.let { exercise ->
                val workoutType = workoutTypes.find{ workoutType ->
                    workoutType.bodyParts?.contains(exercise.bodyPart) == true
                }
                workoutType
            }
        } ?: null
    }

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
                            uiComponentType = UIComponentType.Toast(),
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

    fun updateExerciseBodyPart(bodyPart: BodyPart?){
        val update = getCurrentViewStateOrNew()
        val updateExercise = update.exerciseSelected?.copy(
            bodyPart = bodyPart
        )
        update.exerciseSelected = updateExercise
        setViewState(update)
    }

    fun updateExerciseSets(sets : ArrayList<ExerciseSet>){
        if(sets.isNullOrEmpty()){
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = ExerciseSetCacheEntity.notEmptyError(),
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        )
                    )
                )
            )
        }else{
            val update = getCurrentViewStateOrNew()
            val updateExercise = update.exerciseSelected?.copy(
                sets = sets
            )
            update.exerciseSelected = updateExercise
            setViewState(update)
        }
    }

    fun updateExerciseExerciseType(exerciseType: ExerciseType){

        //Update viewstate
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

    fun setIsExistExercise(isExist : Boolean?){
        val update = getCurrentViewStateOrNew()
        update.isExistExercise = isExist
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

    fun setWorkoutTypeExhausted(isExhausted: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isWorkoutTypesExhausted = isExhausted
        setViewState(update)
    }

    private fun setCachedExerciseSetsByIdExercise(cachedSets: ArrayList<ExerciseSet>) {
        val update = getCurrentViewStateOrNew()
        update.cachedExerciseSetsByIdExercise = cachedSets
        setViewState(update)
    }

    fun setCachedExercisesSetsExhausted(isExhausted: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isCachedExercisesSetsExhausted = isExhausted
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

    fun checkExerciseEditState() = exerciseInteractionManager.checkEditState()

    fun exitExerciseEditState() = exerciseInteractionManager.exitEditState()

    fun isEditingName() = exerciseInteractionManager.isEditingName()

    fun isEditingIsActive() = exerciseInteractionManager.isEditingIsActive()

    fun isEditingWorkoutType() = exerciseInteractionManager.isEditingWorkoutType()

    fun isEditingBodyPart() = exerciseInteractionManager.isEditingBodyPart()

    fun isEditingExerciseType() = exerciseInteractionManager.isEditingExerciseType()


    /*
        driving mad about this dont understand
     */

    fun getDetailWorkoutTypesExhausted() : Boolean = getCurrentViewStateOrNew().detailWorkoutTypesExshauted ?: false

    fun getDetailBodyPartExhausted() : Boolean = getCurrentViewStateOrNew().detailBodyPartsExshauted ?: false

    fun getDetailExerciseTypesExhausted() : Boolean = getCurrentViewStateOrNew().detailExerciseTypeExshauted?: false

    fun setDetailWorkoutTypesExhausted(state : Boolean){
        val update = getCurrentViewStateOrNew()
        update.detailWorkoutTypesExshauted = state
        setViewState(update)
    }

    fun setDetailBodyPartsExhausted(state : Boolean){
        val update = getCurrentViewStateOrNew()
        update.detailBodyPartsExshauted = state
        setViewState(update)
    }

    fun setDetailExerciseTypesExhausted(state : Boolean){
        val update = getCurrentViewStateOrNew()
        update.detailExerciseTypeExshauted = state
        setViewState(update)
    }

    fun resetDetailExhausted(){
        val update = getCurrentViewStateOrNew()
        update.detailExerciseTypeExshauted = false
        update.detailBodyPartsExshauted = false
        update.detailWorkoutTypesExshauted = false
        setViewState(update)
    }

    fun setDetailWorkoutTypes(workoutTypes : ArrayList<WorkoutType>?){
        val update = getCurrentViewStateOrNew()
        update.detailWorkoutTypes = workoutTypes
        setViewState(update)
    }

    fun setDetailBodyPart(bodyparts : ArrayList<BodyPart>?){
        val update = getCurrentViewStateOrNew()
        update.detailBodyParts = bodyparts
        setViewState(update)
    }

    fun setDetailExerciseTypes(exerciseTypes : ArrayList<ExerciseType>?){
        val update = getCurrentViewStateOrNew()
        update.detailExerciseType = exerciseTypes
        setViewState(update)
    }

    fun clearDetailWorkoutTypes(){
        val update = getCurrentViewStateOrNew()
        update.detailWorkoutTypes = ArrayList()
        setViewState(update)
    }

    fun clearDetailBodyPart(){
        val update = getCurrentViewStateOrNew()
        update.detailBodyParts = ArrayList()
        setViewState(update)
    }

    fun clearDetailExerciseTypes(){
        val update = getCurrentViewStateOrNew()
        update.detailExerciseType = ArrayList()
        setViewState(update)
    }


    /********************************************************************
    INTERACTIONS Set STATE
     *********************************************************************/

    val repInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.repState

    val weightInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.weightState

    val timeInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.timeState

    val restInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.restState

    fun setInteractionRepState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setRepState(state)
    }

    fun setInteractionWeightState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setWeightState(state)
    }

    fun setInteractionTimeState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setTimeState(state)
    }

    fun setInteractionRestState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setRestState(state)
    }

    fun checkSetEditState() = exerciseSetInteractionManager.checkEditState()

    fun exitSetEditState() = exerciseSetInteractionManager.exitEditState()

    fun isEditingRep() = exerciseSetInteractionManager.isEditingRep()

    fun isEditingWeight() = exerciseSetInteractionManager.isEditingWeight()

    fun isEditingRest() = exerciseSetInteractionManager.isEditingRest()

    fun isEditingTime() = exerciseSetInteractionManager.isEditingTime()

}

