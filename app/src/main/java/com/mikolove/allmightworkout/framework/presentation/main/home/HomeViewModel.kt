package com.mikolove.allmightworkout.framework.presentation.main.home

import android.content.SharedPreferences
import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.home.*
import com.mikolove.allmightworkout.business.interactors.main.home.RemoveMultipleExercises.Companion.DELETE_EXERCISES_YOU_MUST_SELECT
import com.mikolove.allmightworkout.business.interactors.main.home.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_YOU_MUST_SELECT
import com.mikolove.allmightworkout.framework.datasource.cache.database.*
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_FILTER_NAME
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys.Companion.EXERCISE_LIST_FILTER
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys.Companion.EXERCISE_LIST_ORDER
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys.Companion.WORKOUT_LIST_FILTER
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys.Companion.WORKOUT_LIST_ORDER
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent.CreateStateMessageEvent
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import com.mikolove.allmightworkout.framework.presentation.main.home.state.ListInteractionManager
import com.mikolove.allmightworkout.framework.presentation.main.home.state.ListToolbarState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val homeListInteractors: HomeListInteractors,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<HomeViewState>(){

    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }

    /********************************************************************
        TOOLBAR SELECTION VARS
     *********************************************************************/

    val workoutListInteractionManager = ListInteractionManager<Workout>()

    val workoutToolbarState: LiveData<ListToolbarState>
        get() = workoutListInteractionManager.toolbarState


    val exerciseListInteractionManager = ListInteractionManager<Exercise>()

    val exerciseToolbarState: LiveData<ListToolbarState>
        get() = exerciseListInteractionManager.toolbarState


    /********************************************************************
        INIT BLOC
    *********************************************************************/

    init {
        setWorkoutListFilter(
            sharedPreferences.getString(
                WORKOUT_LIST_FILTER,
                WORKOUT_FILTER_DATE_CREATED
            )
        )

        setExerciseListFilter(
            sharedPreferences.getString(
                EXERCISE_LIST_FILTER,
                EXERCISE_FILTER_DATE_CREATED
            )
        )

        /*setWorkoutTrainingListOrder(
            sharedPreferences.getString(
                WORKOUT_TRAINING_LIST_FILTER,
                WORKOUT_FILTER_NAME
            )
        )*/
    }

    /********************************************************************
        HANDLE DATA
    *********************************************************************/

    override fun handleNewData(data: HomeViewState) {

        data.let { viewState ->

            viewState.listWorkouts?.let { listWorkouts ->
                setListWorkouts(listWorkouts)
            }
            /*viewState.listTrainingWorkouts?.let { listTrainingWorkouts ->
                setListTrainingWorkouts(listTrainingWorkouts)
            }*/
            viewState.listExercises?.let { listExercises ->
                setListExercises(listExercises)
            }
            viewState.listWorkoutTypes?.let { listWorkoutTypes ->
                setListWorkoutTypes(listWorkoutTypes)
            }
            viewState.listBodyParts?.let { listBodyParts ->
                setListBodyParts(listBodyParts)
            }
            viewState.workoutSelected?.let { workoutSelected ->
                setWorkoutSelected(workoutSelected)
            }
            viewState.totalWorkouts?.let { totalWorkouts ->
                setTotalWorkouts(totalWorkouts)
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

    /********************************************************************
        SET STATE EVENT - CALLING USECASES
    *********************************************************************/

    override fun setStateEvent(stateEvent: StateEvent) {

        val job : Flow<DataState<HomeViewState>?> = when(stateEvent){

            is GetBodyPartEvent-> {
               homeListInteractors.getBodyParts.getBodyParts(
                   query = "",
                   filterAndOrder = "",
                   page = 1,
                   stateEvent = stateEvent
               )
            }
            is GetExercisesEvent -> {
                homeListInteractors.getExercises.getExercises(
                    query = getSearchQueryExercises(),
                    filterAndOrder = getOrderExercises() + getFilterExercises(),
                    page = getPageExercises(),
                    stateEvent = stateEvent
                )
            }
            is GetTotalBodyPartsEvent -> {
                homeListInteractors.getTotalBodyParts.getTotalBodyParts(stateEvent)
            }
            is GetTotalBodyPartsByWorkoutTypeEvent -> {
                homeListInteractors.getTotalBodyPartsByWorkoutType.getTotalBodyPartsByWorkoutType(
                    idWorkoutType = stateEvent.idWorkoutType,
                    stateEvent = stateEvent
                )
            }
            is GetTotalExercisesEvent -> {
                homeListInteractors.getTotalExercises.getTotalExercises(stateEvent)
            }
            is GetTotalWorkoutsEvent -> {
                homeListInteractors.getTotalWorkouts.getTotalWorkouts(stateEvent)
            }
            is GetWorkoutByIdEvent -> {
                homeListInteractors.getWorkoutById.getWorkoutById(
                    idWorkout = stateEvent.idWorkout,
                    stateEvent = stateEvent
                )
            }
            is GetWorkoutsEvent -> {
                homeListInteractors.getWorkouts.getWorkouts(
                    query = getSearchQueryWorkouts(),
                    filterAndOrder = getOrderWorkouts() + getFilterWorkouts(),
                    page = getPageWorkouts(),
                    stateEvent = stateEvent
                )
            }
            is GetWorkoutTypesEvent -> {
                homeListInteractors.getWorkoutTypes.getWorkoutTypes(
                    query = "",
                    filterAndOrder = "",
                    1,
                    stateEvent = stateEvent
                )
            }
            is RemoveMultipleExercisesEvent -> {
                homeListInteractors.removeMultipleExercises.removeMultipleExercises(
                    exercises = stateEvent.exercises,
                    stateEvent = stateEvent
                )
            }
            is RemoveMultipleWorkoutsEvent -> {
                homeListInteractors.removeMultipleWorkouts.removeMultipleWorkouts(
                    workouts = stateEvent.workouts,
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

    fun firstLoad(){
        //Clean them and load them all
        reloadWorkoutTypes()
        reloadBodyParts()
        reloadWorkouts()
        reloadExercises()
    }

    fun reloadWorkouts(){
        setQueryWorkouts("")
        clearListWorkouts()
        loadWorkouts()
    }

    fun reloadExercises(){
        setQueryExercises("")
        clearListExercises()
        loadExercises()
    }

    fun reloadBodyParts(){
        clearListBodyParts()
        loadBodyParts()
    }

    fun reloadWorkoutTypes(){
        clearListWorkoutTypes()
        loadWorkoutTypes()
    }

    fun loadTotalWorkouts(){
        printLogD("HomeViewModel","Load total workouts")
        setStateEvent(GetTotalWorkoutsEvent())
    }

    fun loadTotalExercises(){
        printLogD("HomeViewModel","Load total exercises")
        setStateEvent(GetTotalExercisesEvent())
    }

    fun loadTotalWorkoutTypes(idWorkoutType : String){
        printLogD("HomeViewModel","Load total bodyPart by WorkoutType")
        setStateEvent(GetTotalBodyPartsByWorkoutTypeEvent(idWorkoutType))
    }

    fun loadTotalBodyParts(){
        printLogD("HomeViewModel","Load total bodyParts")
        setStateEvent(GetTotalBodyPartsEvent())
    }

    fun loadWorkoutTypes(){
        printLogD("HomeViewModel","Load workout types")
        setStateEvent(GetWorkoutTypesEvent())
    }

    fun loadBodyParts(){
        printLogD("HomeViewModel","Load body parts")
        setStateEvent(GetBodyPartEvent())
    }

    fun loadWorkouts(){
        printLogD("HomeViewModel","Load workouts")
        setStateEvent(GetWorkoutsEvent())
    }

    fun loadExercises(){
        printLogD("HomeViewModel","Load Exercises")
        setStateEvent(GetExercisesEvent())
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

    fun clearListWorkouts(){
        val update = getCurrentViewStateOrNew()
        update.listWorkouts = ArrayList()
        setViewState(update)
    }

    fun clearListExercises(){
        val update = getCurrentViewStateOrNew()
        update.listExercises = ArrayList()
        setViewState(update)
    }

    fun saveFilterWorkoutsOptions(filter: String, order: String){
        editor.putString(WORKOUT_LIST_FILTER, filter)
        editor.apply()

        editor.putString(WORKOUT_LIST_ORDER, order)
        editor.apply()
    }


    fun saveFilterExercisesOptions(filter: String, order: String){
        editor.putString(EXERCISE_LIST_FILTER, filter)
        editor.apply()

        editor.putString(EXERCISE_LIST_ORDER, order)
        editor.apply()
    }

    /********************************************************************
        GETTERS - QUERY
    *********************************************************************/

    fun getFilterWorkouts(): String {
        return getCurrentViewStateOrNew().workout_list_filter
            ?: WORKOUT_FILTER_NAME
    }

    fun getOrderWorkouts(): String {
        return getCurrentViewStateOrNew().workout_list_order
            ?: WORKOUT_ORDER_BY_ASC_NAME
    }

    fun getSearchQueryWorkouts(): String {
        return getCurrentViewStateOrNew().searchQueryWorkouts
            ?: return ""
    }

    private fun getPageWorkouts(): Int{
        return getCurrentViewStateOrNew().pageWorkouts
            ?: return 1
    }

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

/*    fun getFilterTrainingWorkouts(): String {
        return getCurrentViewStateOrNew().load_workout_list_filter
            ?: WORKOUT_FILTER_NAME
    }

    fun getOrderTrainingWorkouts(): String {
        return getCurrentViewStateOrNew().load_workout_list_order
            ?: WORKOUT_ORDER_BY_ASC_NAME
    }

    fun getSearchQueryTrainingWorkouts(): String {
        return getCurrentViewStateOrNew().searchQueryTrainingWorkouts
            ?: return ""
    }

    private fun getPageTrainingWorkouts(): Int{
        return getCurrentViewStateOrNew().pageTrainingWorkouts
            ?: return 1
    }*/


    /********************************************************************
        GETTERS - VIEWSTATE AND OTHER
    *********************************************************************/

    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun getLayoutManagerState(): Parcelable? {
        return getCurrentViewStateOrNew().layoutManagerState
    }

    fun getWorkouts() = getCurrentViewStateOrNew().listWorkouts

    fun getExercises() = getCurrentViewStateOrNew().listExercises

    fun getTotalWorkouts()  = getCurrentViewStateOrNew().totalWorkouts

    fun getTotalExercises() = getCurrentViewStateOrNew().totalExercises

    fun getTotalBodyParts() = getCurrentViewStateOrNew().totalBodyParts

    fun getTotalBodyPartByWorkoutType() = getCurrentViewStateOrNew().totalBodyPartsByWorkoutType

    fun getWorkoutTypeSelected() = getCurrentViewStateOrNew().workoutTypeSelected

    fun getWorkoutSelected() = getCurrentViewStateOrNew().workoutSelected

    /********************************************************************
        SETTERS - VIEWSTATE AND OTHER
    *********************************************************************/

    fun setWorkoutListFilter(filter : String?) {
        filter?.let {
            val update = getCurrentViewStateOrNew()
            update.workout_list_filter = filter
            setViewState(update)
        }
    }

    fun setExerciseListFilter(filter : String?){
        filter?.let {
            val update = getCurrentViewStateOrNew()
            update.exercise_list_filter = filter
            setViewState(update)
        }
    }

    private fun setListWorkouts(workouts: ArrayList<Workout>){
        val update = getCurrentViewStateOrNew()
        update.listWorkouts = workouts
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

    private fun setTotalWorkouts(total : Int){
        val update = getCurrentViewStateOrNew()
        update.totalWorkouts = total
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

    private fun setWorkoutSelected(workout : Workout){
        val update = getCurrentViewStateOrNew()
        update.workoutSelected = workout
        setViewState(update)
    }

    private fun setQueryWorkouts(string : String?){
        val update = getCurrentViewStateOrNew()
        update.searchQueryWorkouts = string
        setViewState(update)
    }

    private fun setQueryExercises(string : String?){
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
        TOOLBARS GETTERS AND SETTERS - WORKOUTS
    *********************************************************************/


    fun getSelectedWorkouts() = workoutListInteractionManager.getSelectedItems()

    fun setWorkoutToolbarState(state: ListToolbarState)
            = workoutListInteractionManager.setToolbarState(state)

    fun isWorkoutMultiSelectionStateActive()
            = workoutListInteractionManager.isMultiSelectionStateActive()

    fun addOrRemoveWorkoutFromSelectedList(workout: Workout)
            = workoutListInteractionManager.addOrRemoveItemFromSelectedList(workout)

    fun isNoteSelected(workout: Workout): Boolean
            = workoutListInteractionManager.isItemSelected(workout)

    fun clearSelectedNotes() = workoutListInteractionManager.clearSelectedItems()

    private fun removeSelectedWorkoutsFromList(){
        val update = getCurrentViewStateOrNew()
        update.listWorkouts?.removeAll(getSelectedWorkouts())
        setViewState(update)
        clearSelectedNotes()
    }

    fun deleteWorkouts(){
        if(getSelectedWorkouts().size > 0){
            setStateEvent(RemoveMultipleWorkoutsEvent(getSelectedWorkouts()))
            removeSelectedWorkoutsFromList()
        }
        else{
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = DELETE_WORKOUTS_YOU_MUST_SELECT,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
        }
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
        clearSelectedNotes()
    }

    fun deleteExercises(){
        if(getSelectedWorkouts().size > 0){
            setStateEvent(RemoveMultipleExercisesEvent(getSelectedExercises()))
            removeSelectedExercisesFromList()
        }
        else{
            setStateEvent(
                CreateStateMessageEvent(
                    stateMessage = StateMessage(
                        response = Response(
                            message = DELETE_EXERCISES_YOU_MUST_SELECT,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
        }
    }











}