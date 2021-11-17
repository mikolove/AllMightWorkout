package com.mikolove.allmightworkout.framework.presentation.main.workout_list

/*
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts
import com.mikolove.allmightworkout.business.interactors.main.workout.UpdateWorkout
import com.mikolove.allmightworkout.business.interactors.main.workout.WorkoutInteractors
import com.mikolove.allmightworkout.framework.datasource.cache.database.*
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.common.ListInteractionManager
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState
import com.mikolove.allmightworkout.framework.presentation.main.workout_detail.WorkoutInteractionManager
import com.mikolove.allmightworkout.framework.presentation.main.workout_detail.WorkoutInteractionState
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


const val WORKOUT_ERRROR_RETRIEVING_ID_WORKOUT = "Error retrieving idWorkout from bundle."
const val WORKOUT_ID_WORKOUT_BUNDLE_KEY = "idWorkout"
const val WORKOUT_NAME_CANNOT_BE_EMPTY = "Workout name cannot be empty."
const val INSERT_WORKOUT_ERROR_NO_NAME = "You must set a name to create a workout."

@HiltViewModel
class WorkoutViewModel
@Inject
constructor(
    private val workoutInteractors: WorkoutInteractors,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences,
    private val workoutFactory: WorkoutFactory,
    private val dateUtil: DateUtil
) : BaseViewModel<WorkoutViewState>(){

    override fun initNewViewState(): WorkoutViewState {
        return WorkoutViewState()
    }

    */
/********************************************************************
        TOOLBAR SELECTION VARS
    *********************************************************************//*


    val workoutListInteractionManager = ListInteractionManager<Workout>()

    val workoutToolbarState: LiveData<ListToolbarState>
        get() = workoutListInteractionManager.toolbarState

    private val workoutInteractionManager: WorkoutInteractionManager = WorkoutInteractionManager()

    val workoutNameInteractionState: LiveData<WorkoutInteractionState>
        get() = workoutInteractionManager.workoutNameState

    val workoutIsActiveInteractionState: LiveData<WorkoutInteractionState>
        get() = workoutInteractionManager.workoutIsActiveState
    
    val exerciseListInteractionManager = ListInteractionManager<Exercise>()


    */
/********************************************************************
    INIT BLOC
     *********************************************************************//*


    init {
        setWorkoutListFilter(
            sharedPreferences.getString(
                PreferenceKeys.WORKOUT_LIST_FILTER,
                WORKOUT_FILTER_DATE_CREATED
            )
        )

        setWorkoutOrder(
            sharedPreferences.getString(
                PreferenceKeys.WORKOUT_LIST_ORDER,
                WORKOUT_ORDER_DESC
            )
        )
    }

    override fun handleNewData(data: WorkoutViewState) {
        data.let { viewState ->

            viewState.workoutSelected?.let { workout ->
                setWorkoutSelected(workout)
            }
            viewState.workoutToInsert?.let { insertedWorkout ->
                printLogD("WorkoutViewModel","Inserted workout ${insertedWorkout}")
                setInsertedWorkout(insertedWorkout)
            }
            viewState.listWorkouts?.let { listWorkouts ->
                setListWorkouts(listWorkouts)
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

    */
/********************************************************************
        SET STATE EVENT - CALLING USECASES
    *********************************************************************//*


    override fun setStateEvent(stateEvent: StateEvent) {

        printLogD("WorkoutViewModel","ENTER SET STATE EVENT")

        val job : Flow<DataState<WorkoutViewState>?> = when(stateEvent) {

            is InsertWorkoutEvent -> {
                printLogD("WorkoutViewModel","INSERT IT")
                workoutInteractors.insertWorkout.insertWorkout(
                    name = stateEvent.name,
                    stateEvent = stateEvent
                )
            }

            is GetWorkoutByIdEvent -> {
                workoutInteractors.getWorkoutById.execute(
                    idWorkout = stateEvent.idWorkout,
                    stateEvent = stateEvent
                )
            }

            is GetExercisesEvent -> {
                workoutInteractors.getExercises.getExercises(
                    query = getSearchQueryExercises(),
                    filterAndOrder = getOrderExercises() + getFilterExercises(),
                    page = getPageExercises(),
                    stateEvent = stateEvent
                )
            }

            is GetWorkoutsEvent -> {
                workoutInteractors.getWorkouts.execute(
                   query = getSearchQueryWorkouts(),
                   filterAndOrder = getOrderWorkouts() + getFilterWorkouts(),
                   page = getPageWorkouts(),
                   stateEvent = stateEvent
               )
            }

            is UpdateWorkoutEvent -> {
                if(!isWorkoutNameNull()){
                    workoutInteractors.updateWorkout.updateWorkout(
                        workout = getWorkoutSelected()!!,
                        stateEvent = stateEvent
                    )
                }else{
                    emitStateMessageEvent(
                        stateMessage = StateMessage(
                            response = Response(
                                message = UpdateWorkout.UPDATE_WORKOUT_FAILED,
                                uiComponentType = UIComponentType.Dialog(),
                                messageType = MessageType.Error())
                        ),
                        stateEvent= stateEvent
                    )
                }
            }

            is RemoveWorkoutEvent -> {
                workoutInteractors.removeWorkout.removeWorkout(
                    workout = getWorkoutSelected()!!,
                    stateEvent = stateEvent
                )
            }

            is AddExerciseToWorkoutEvent -> {
                workoutInteractors.addExerciseToWorkout.addExerciseToWorkout(
                    idExercise = stateEvent.exerciseId,
                    idWorkout = stateEvent.workoutId,
                    stateEvent = stateEvent
                )
            }

            is RemoveExerciseFromWorkoutEvent -> {
                workoutInteractors.removeExerciseFromWorkout.removeExerciseFromWorkout(
                    idExercise = stateEvent.exerciseId,
                    idWorkout = stateEvent.workoutId,
                    stateEvent = stateEvent
                )
            }

            is RemoveMultipleWorkoutsEvent -> {
                workoutInteractors.removeMultipleWorkouts.removeMultipleWorkouts(
                    workouts = stateEvent.workouts,
                    stateEvent = stateEvent
                )
            }

            is GetWorkoutTypesEvent -> {
                workoutInteractors.getWorkoutTypes.getWorkoutTypes(
                    query = "",
                    filterAndOrder = "",
                    1,
                    stateEvent = stateEvent
                )
            }

            is GetBodyPartEvent -> {
                workoutInteractors.getBodyParts.getBodyParts(
                    query = "",
                    filterAndOrder = "",
                    page = 1,
                    stateEvent = stateEvent
                )
            }

            is GetTotalWorkoutsEvent -> {
                workoutInteractors.getTotalWorkouts.execute(stateEvent)
            }

            is GetTotalExercisesEvent -> {
                workoutInteractors.getTotalExercises.getTotalExercises(stateEvent)
            }

            is GetTotalBodyPartsEvent -> {
                workoutInteractors.getTotalBodyParts.getTotalBodyParts(stateEvent)
            }

            is GetTotalBodyPartsByWorkoutTypeEvent -> {
                workoutInteractors.getTotalBodyPartsByWorkoutType.getTotalBodyPartsByWorkoutType(
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

    */
/********************************************************************
    TRIGGER STATE EVENTS - FUNCTIONS
     *********************************************************************//*


    fun getWorkoutById(idWorkout : String){
        setStateEvent(GetWorkoutByIdEvent(idWorkout = idWorkout))
    }


    fun createWorkout(name : String) : Workout = workoutFactory.createWorkout(
        idWorkout = null,
        name = name,
        exercises = null,
        isActive = true,
        created_at = null
    )

    fun updateWorkout(){
        updateWorkoutUpdatedAt()
        setStateEvent(UpdateWorkoutEvent())
    }

    fun isWorkoutNameNull() : Boolean{
        val name = getWorkoutSelected()?.name
        if (name.isNullOrBlank()) {
            setStateEvent(
                CreateStateMessageEvent(
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

    */
/********************************************************************
        LIST WORKOUTS MANAGING
     *********************************************************************//*


    //Launch actual query reseting Pagination
    fun workoutsStartNewSearch(){
        setWorkoutQueryExhausted(false)
        resetPageWorkouts()
        loadWorkouts()
    }

    //Launch actual query keeping Pagination
    fun refreshWorkoutSearchQuery(){
        setWorkoutQueryExhausted(false)
        loadWorkouts()
    }

    fun loadWorkouts(){
        setStateEvent(GetWorkoutsEvent())
    }


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

    */
/********************************************************************
        OTHERS LIST MANAGING
     *********************************************************************//*


    fun reloadWorkoutSelected(){
        getWorkoutSelected()?.let { workout ->
            setStateEvent(GetWorkoutByIdEvent(workout.idWorkout))
        }
    }

    fun reloadBodyParts(){
        clearListBodyParts()
        loadBodyParts()
    }

    fun reloadWorkoutTypes(){
        clearListWorkoutTypes()
        loadWorkoutTypes()
    }

    fun loadTotalWorkoutTypes(idWorkoutType : String){
        setStateEvent(GetTotalBodyPartsByWorkoutTypeEvent(idWorkoutType))
    }

    fun loadTotalBodyParts(){
        setStateEvent(GetTotalBodyPartsEvent())
    }

    fun loadTotalWorkouts(){
        setStateEvent(GetTotalWorkoutsEvent())
    }
    fun loadTotalExercises(){
        setStateEvent(GetTotalExercisesEvent())
    }

    fun loadWorkoutTypes(){
        setStateEvent(GetWorkoutTypesEvent())
    }

    fun loadBodyParts(){
        setStateEvent(GetBodyPartEvent())
    }

    fun addExerciseToWorkout(idExercise : String, idWorkout : String){
        setStateEvent(AddExerciseToWorkoutEvent(
            exerciseId = idExercise,
            workoutId = idWorkout
        ))
    }

    fun removeExerciseFromWorkout(idExercise : String, idWorkout : String){
        setStateEvent(RemoveExerciseFromWorkoutEvent(
            exerciseId = idExercise,
            workoutId = idWorkout
        ))
    }

    fun saveFilterWorkoutsOptions(filter: String, order: String){
        editor.putString(PreferenceKeys.WORKOUT_LIST_FILTER, filter)
        editor.apply()

        editor.putString(PreferenceKeys.WORKOUT_LIST_ORDER, order)
        editor.apply()
    }


    */
/********************************************************************
        GETTERS - QUERY
     *********************************************************************//*


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
        return EXERCISE_FILTER_NAME
    }

    fun getOrderExercises(): String {
        return EXERCISE_ORDER_BY_ASC_NAME
    }

    fun getSearchQueryExercises(): String {
        return getCurrentViewStateOrNew().searchQueryExercises
            ?: return ""
    }

    private fun getPageExercises(): Int{
        return getCurrentViewStateOrNew().pageExercises
            ?: return 1
    }



    */
/********************************************************************
    GETTERS - VIEWSTATE AND OTHER
     *********************************************************************//*


    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun getWorkouts() = getCurrentViewStateOrNew().listWorkouts

    fun getTotalWorkouts()  = getCurrentViewStateOrNew().totalWorkouts ?: 0

    fun getTotalExercises()  = getCurrentViewStateOrNew().totalExercises ?: 0

    fun getTotalBodyParts() = getCurrentViewStateOrNew().totalBodyParts

    fun getTotalBodyPartByWorkoutType() = getCurrentViewStateOrNew().totalBodyPartsByWorkoutType

    fun getWorkoutTypeSelected() = getCurrentViewStateOrNew().workoutTypeSelected

    fun getWorkoutToInsert() = getCurrentViewStateOrNew().workoutToInsert ?: null

    fun getWorkoutSelected() : Workout? = getCurrentViewStateOrNew().workoutSelected ?: null

    fun getIsUpdatePending(): Boolean = getCurrentViewStateOrNew().isUpdatePending?: false

    fun getWorkoutsListSize() = getCurrentViewStateOrNew().listWorkouts?.size?: 0

    fun isWorkoutsPaginationExhausted() = getWorkoutsListSize() >= getTotalWorkouts()

    fun getExercisesListSize() = getCurrentViewStateOrNew().listExercises?.size?: 0

    fun isExercisesPaginationExhausted() = getExercisesListSize() >= getTotalExercises()

    fun isSearchActive() = getCurrentViewStateOrNew().searchActive ?: false

    */
/********************************************************************
    SETTERS - VIEWSTATE AND OTHER
     *********************************************************************//*


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

    fun updateWorkoutUpdatedAt(){
        val update = getCurrentViewStateOrNew()
        val updatedWorkout = update.workoutSelected?.copy(
            updatedAt = dateUtil.getCurrentTimestamp()
        )
        update.workoutSelected = updatedWorkout
        setViewState(update)
    }

    fun updateWorkoutIsActive(isActive: Boolean){
        val update = getCurrentViewStateOrNew()
        val updatedWorkout = update.workoutSelected?.copy(
            isActive = isActive
        )
        update.workoutSelected = updatedWorkout
        setViewState(update)
    }

    fun updateWorkoutExercises(exercises: List<Exercise>){
        val update = getCurrentViewStateOrNew()
        val updatedWorkout = update.workoutSelected?.copy(
            exercises = exercises
        )
        update.workoutSelected = updatedWorkout
        setViewState(update)
    }

    fun updateWorkoutName(name: String?){
        if(name.isNullOrBlank()){
            setStateEvent(
                CreateStateMessageEvent(
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
            val updatedWorkout = update.workoutSelected?.copy(
                name = name
            )
            update.workoutSelected = updatedWorkout
            setViewState(update)
        }
    }

    fun setIsSearchActive(isActive : Boolean){
        val update = getCurrentViewStateOrNew()
        update.searchActive = isActive
        setViewState(update)
    }

    fun setIsUpdatePending(isPending: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isUpdatePending = isPending
        setViewState(update)
    }

    fun setWorkoutSelected(workout: Workout?){
        val update = getCurrentViewStateOrNew()
        update.workoutSelected = workout
        setViewState(update)
    }

    fun setWorkoutListFilter(filter : String?) {
        filter?.let {
            val update = getCurrentViewStateOrNew()
            update.workout_list_filter = filter
            setViewState(update)
        }
    }

    fun setInsertedWorkout(workout: Workout?){
        val update = getCurrentViewStateOrNew()
        update.workoutToInsert = workout
        setViewState(update)
    }

    fun setWorkoutOrder(order: String?){
        val update = getCurrentViewStateOrNew()
        update.workout_list_order = order
        setViewState(update)
    }

    private fun setListWorkouts(workouts: ArrayList<Workout>){
        val update = getCurrentViewStateOrNew()
        update.listWorkouts = workouts
        setViewState(update)
    }

    private fun setListExercises(exercises: ArrayList<Exercise>){
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

    private fun setTotalExercises(total: Int){
        val update = getCurrentViewStateOrNew()
        update.totalExercises = total
        setViewState(update)
    }

    private fun setTotalBodyParts(total : Int){
        val update = getCurrentViewStateOrNew()
        update.totalBodyParts = total
        setViewState(update)
    }

    private fun setTotalBodyPartsByWorkoutType(total : Int){
        val update = getCurrentViewStateOrNew()
        update.totalBodyPartsByWorkoutType = total
        setViewState(update)
    }

    fun setQueryWorkouts(string : String?){
        val update = getCurrentViewStateOrNew()
        update.searchQueryWorkouts = string
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

    */
/********************************************************************
        TOOLBARS GETTERS AND SETTERS - WORKOUTS
    *********************************************************************//*


    fun getSelectedExercises() = exerciseListInteractionManager.getSelectedItems()
    
    fun addOrRemoveExerciseFromSelectedList(exercise: Exercise)
            = exerciseListInteractionManager.addOrRemoveItemFromSelectedList(exercise)

    fun isExerciseSelected(exercise: Exercise): Boolean
            = exerciseListInteractionManager.isItemSelected(exercise)

    fun clearSelectedExercises() = exerciseListInteractionManager.clearSelectedItems()


    fun getSelectedWorkouts() = workoutListInteractionManager.getSelectedItems()

    fun setWorkoutToolbarState(state: ListToolbarState)
            = workoutListInteractionManager.setToolbarState(state)

    fun isWorkoutMultiSelectionStateActive()
            = workoutListInteractionManager.isMultiSelectionStateActive()

    fun addOrRemoveWorkoutFromSelectedList(workout: Workout)
            = workoutListInteractionManager.addOrRemoveItemFromSelectedList(workout)

    fun isWorkoutSelected(workout: Workout): Boolean
            = workoutListInteractionManager.isItemSelected(workout)

    fun clearSelectedWorkouts() = workoutListInteractionManager.clearSelectedItems()

    private fun removeSelectedWorkoutsFromList(){
        val update = getCurrentViewStateOrNew()
        update.listWorkouts?.removeAll(getSelectedWorkouts())
        setViewState(update)
        clearSelectedWorkouts()
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
                            message = RemoveMultipleWorkouts.DELETE_WORKOUTS_YOU_MUST_SELECT,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Info()
                        )
                    )
                )
            )
        }
    }

    */
/********************************************************************
        ListWorkouts Page Management
     *********************************************************************//*


    private fun resetPageWorkouts(){
        val update = getCurrentViewStateOrNew()
        update.pageWorkouts = 1
        setViewState(update)
    }

    private fun incrementPageWorkoutsNumber(){
        val update = getCurrentViewStateOrNew()
        val page = update.copy().pageWorkouts ?: 1
        update.pageWorkouts = page.plus(1)
        setViewState(update)
    }

    fun nextPageWorkouts(){
        if(!isWorkoutsQueryExhausted()){
            incrementPageWorkoutsNumber()
            setStateEvent(GetWorkoutsEvent())
        }
    }

    fun isWorkoutsQueryExhausted(): Boolean{
        return getCurrentViewStateOrNew().isWorkoutsQueryExhausted?: true
    }

    fun setWorkoutQueryExhausted(isExhausted : Boolean){
        val update = getCurrentViewStateOrNew()
        update.isWorkoutsQueryExhausted = isExhausted
        setViewState(update)
    }

    */
/********************************************************************
    ListExercise Page Management
     *********************************************************************//*


    private fun resetPageExercises(){
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
        return getCurrentViewStateOrNew().isExerciseQueryExhausted?: true
    }

    fun setExerciseQueryExhausted(isExhausted : Boolean){
        val update = getCurrentViewStateOrNew()
        update.isExerciseQueryExhausted = isExhausted
        setViewState(update)
    }


    */
/********************************************************************
    INTERACTIONS
     *********************************************************************//*


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



}*/
