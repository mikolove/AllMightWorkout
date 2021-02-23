package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_ARE_YOU_SURE
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_ERRORS
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentWorkoutBinding
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_FILTER_DATE_CREATED
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_FILTER_NAME
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_ORDER_ASC
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_ORDER_DESC
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState.*
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val WORKOUT_VIEW_STATE_BUNDLE_KEY = "com.mikolove.allmightworkout.framework.presentation.main.workout.state"

@AndroidEntryPoint
class WorkoutFragment
    : BaseFragment(R.layout.fragment_workout),
    WorkoutListAdapter.Interaction,
    FabController {

    @Inject
    lateinit var dateUtil: DateUtil

    val viewModel : WorkoutViewModel by activityViewModels()
    private var actionMode : ActionMode? = null
    private var actionModeCallBack : ActionMode.Callback? = null
    private var listAdapter: WorkoutListAdapter? = null
    private var binding : FragmentWorkoutBinding? = null


    /********************************************************************
        LIFECYCLE MANANGEMENT
     *********************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        printLogD("WorkoutFragment","OnCreate")
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        printLogD("WorkoutFragment","OnViewCreated")
        setHasOptionsMenu(true)

        binding = FragmentWorkoutBinding.bind(view)

        setupUI()
        setupRecyclerView()
        setupSwipeRefresh()
        subscribeObservers()

        //Restore ViewState where it was not sure
        //restoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        printLogD("WorkoutFragment","OnResume")

        setupFAB()
        setupBottomNav()

        viewModel.loadTotalWorkouts()
        viewModel.clearListWorkouts()
        viewModel.refreshWorkoutSearchQuery()
    }

    override fun onPause() {
        super.onPause()
        printLogD("WorkoutFragment", "OnPause")
    }

    override fun onDestroyView() {
        printLogD("WorkoutFragment", "OnDestroyView")
        disableMultiSelectToolbarState()
        binding?.fragmentWorkoutRecyclerview?.adapter = null
        listAdapter = null
        binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        printLogD("WorkoutFragment", "OnDestroy")
    }


    /********************************************************************
        FRAGEMENT SAVEINSTANCE
     *********************************************************************/

    override fun onSaveInstanceState(outState: Bundle) {
        printLogD("WorkoutFragment","OnSaveInstanceState")
        val viewState = viewModel.viewState.value

        viewState?.listWorkouts =  ArrayList()
        viewState?.listBodyParts = ArrayList()
        viewState?.listWorkoutTypes = ArrayList()
        viewState?.listExercisesFromWorkoutId = ArrayList()
        viewState?.workoutSelected = null
        viewState?.workoutToInsert = null

        outState.putParcelable(
            WORKOUT_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        printLogD("WorkoutFragment","restoreInstanceState")
        savedInstanceState?.let { inState ->
            (inState[WORKOUT_VIEW_STATE_BUNDLE_KEY] as WorkoutViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    /********************************************************************
        SUBSCRIBE OBSERVERS
     *********************************************************************/

    private fun subscribeObservers(){

        viewModel.workoutToolbarState.observe(viewLifecycleOwner, Observer { toolbarState ->

            when (toolbarState) {

                is MultiSelectionState -> {
                    enableMultiSelectToolbarState()
                }

                is SelectionState -> {
                    disableMultiSelectToolbarState()

                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {

                viewState.listWorkouts?.let { workoutList ->

                    if(workoutList.size > 0){
                        if (viewModel.isWorkoutsPaginationExhausted() && !viewModel.isWorkoutsQueryExhausted()) {
                            viewModel.setWorkoutQueryExhausted(true)
                        }
                        listAdapter?.submitList(workoutList)
                    }
                }

                viewState.workoutToInsert?.let { insertedWorkout ->
                    viewModel.getWorkoutById(insertedWorkout.idWorkout)
                }

                viewState.workoutSelected?.let { _ ->
                    if(viewModel.getWorkoutToInsert() != null) {
                        insertionNavigateToManageWorkout()
                    }
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { isLoading ->
            printActiveJobs()
            uiController.displayProgressBar(isLoading)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when (response.message) {

                    DELETE_WORKOUTS_SUCCESS -> {

                        showToastDeleteWorkouts()
                        disableActionMode()
                    }

                    DELETE_WORKOUTS_ERRORS -> {
                        showToastDeleteWorkoutsError()
                        disableActionMode()
                    }

                    else -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                    }
                }
            }

        })
    }


    /********************************************************************
        SETUP
    *********************************************************************/

    private fun setupUI(){
        view?.hideKeyboard()
    }

    override fun setupFAB(){
        uiController.loadFabController(this)
        uiController.mainFabVisibility()
    }

    private fun setupBottomNav(){
        uiController.displayBottomNavigation(View.VISIBLE)
    }

    private fun setupSwipeRefresh(){
        binding?.fragmentWorkoutSwiperefreshlayout?.setOnRefreshListener {
            startNewSearch()
            binding?.fragmentWorkoutSwiperefreshlayout?.isRefreshing = false
        }
    }

    private fun setupRecyclerView(){
        binding?.fragmentWorkoutRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            listAdapter = WorkoutListAdapter(
                this@WorkoutFragment,
                viewLifecycleOwner,
                viewModel.workoutListInteractionManager.selectedItems,
                dateUtil
            )


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == listAdapter?.itemCount?.minus(1)) {
                        viewModel.nextPageWorkouts()
                    }
                }
            })

            listAdapter?.stateRestorationPolicy = PREVENT_WHEN_EMPTY
            adapter = listAdapter
        }
    }

    /********************************************************************
        NAVIGATION
     *********************************************************************/

    private fun insertionNavigateToManageWorkout(){
        findNavController().navigate(
            R.id.action_workout_fragment_to_workout_detail_fragment,
            null
        )
        viewModel.setInsertedWorkout(null)
    }

    private fun selectionNavigateToManageWorkout(containerView : View){

        val itemDetailTransitionName = getString(R.string.test_workout_item_detail_transition_name)
        val extras = FragmentNavigatorExtras(containerView to itemDetailTransitionName)
        findNavController().navigate(
            R.id.action_workout_fragment_to_workout_detail_fragment,
            null,
            null,
            extras
        )
    }

    /********************************************************************
        UI DIALOG
     *********************************************************************/

    fun showFilterDialog(){

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilterWorkouts()
            val order = viewModel.getOrderWorkouts()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    WORKOUT_FILTER_DATE_CREATED -> check(R.id.filter_date)
                    WORKOUT_FILTER_NAME -> check(R.id.filter_title)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    WORKOUT_ORDER_ASC -> check(R.id.filter_asc)
                    WORKOUT_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_title -> WORKOUT_FILTER_NAME
                        R.id.filter_date -> WORKOUT_FILTER_DATE_CREATED
                        else -> WORKOUT_FILTER_DATE_CREATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterWorkoutsOptions(newFilter, newOrder)
                    setWorkoutListFilter(newFilter)
                    setWorkoutOrder(newOrder)
                }

                startNewSearch()

                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    /********************************************************************
        UI FAB
     *********************************************************************/

    override fun fabOnClick() {
        addWorkout()
    }

    /********************************************************************
        ACTION MODE
     *********************************************************************/

    private fun createActionModeCallBack() : ActionMode.Callback = object : ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            activity?.menuInflater?.inflate(R.menu.menu_workout_actionmode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.toolbar_workout_delete -> {
                    deleteWorkouts()
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            disableActionMode()
        }
    }

    private fun enableMultiSelectToolbarState(){
        if(actionMode == null) {
            actionModeCallBack = createActionModeCallBack()
            actionMode = activity?.startActionMode(actionModeCallBack)
        }
    }

    private fun disableMultiSelectToolbarState(){
        if(actionMode != null){
            actionMode?.finish()
            actionModeCallBack = null
            actionMode = null
        }
    }

    private fun disableActionMode(){
        viewModel.setWorkoutToolbarState(SelectionState())
        viewModel.clearSelectedWorkouts()
    }

    /********************************************************************
        MENU
     *********************************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workout, menu)

        printLogD("WorkoutFragment", "LOADED MENU")

        //Deal with searchView
        val searchItem = menu.findItem(R.id.menu_workout_search)
        val searchView = searchItem?.actionView as SearchView

        //Reload standard search when finish
        searchItem.setOnActionExpandListener(object :MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.setQueryWorkouts("")
                viewModel.setIsSearchActive(false)
                startNewSearch()
                return true
            }
        })

        //May Add autocomplete
        val searchAutoComplete: SearchView.SearchAutoComplete? = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                val searchQuery = v.text.toString()
                viewModel.setQueryWorkouts(searchQuery)
                startNewSearch()
                viewModel.setIsSearchActive(true)
            }
            true
        }

        //Expand if search isActive on menu reload
        if(viewModel.isSearchActive()){
            searchItem.expandActionView()
            searchView.setQuery(viewModel.getSearchQueryWorkouts(),false)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_workout_filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /********************************************************************
        VIEWMODEL ACTIONS
     *********************************************************************/


    private fun startNewSearch(){
        printLogD("WorkoutFragment","Start New search")
        viewModel.clearListWorkouts()
        viewModel.workoutsStartNewSeach()
    }

    private fun addWorkout(){
        uiController.displayInputCaptureDialog(
            getString(R.string.fragment_choose_workout_add_name),
            object : DialogInputCaptureCallback {
                override fun onTextCaptured(text: String) {
                    if (!text.isNullOrBlank()) {
                        viewModel.setStateEvent(
                            InsertWorkoutEvent(name = text)
                        )
                    } else {
                        onErrorNoNameSpecified()
                    }
                }
            }
        )
    }

    private fun onErrorNoNameSpecified(){
        viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = INSERT_WORKOUT_ERROR_NO_NAME,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        )
    }
    private fun deleteWorkouts(){
        viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = DELETE_WORKOUTS_ARE_YOU_SURE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    viewModel.deleteWorkouts()
                                }

                                override fun cancel() {
                                    // do nothing
                                }
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )
    }

    /********************************************************************
        OVERRIDE UI CONTROLLER TOAST SNACKBAR
     *********************************************************************/

    private fun showToastDeleteWorkouts(){

        uiController.onResponseReceived(
            response = Response(
                message = DELETE_WORKOUTS_SUCCESS,
                uiComponentType = UIComponentType.SnackBar(
                    undoCallback = null,
                    onDismissCallback = null
                ),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object: StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    private fun showToastDeleteWorkoutsError(){

        uiController.onResponseReceived(
            response = Response(
                message = DELETE_WORKOUTS_ERRORS,
                uiComponentType = UIComponentType.SnackBar(
                    undoCallback = null,
                    onDismissCallback = null
                ),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object: StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }


    /********************************************************************
        WORKOUT LIST ADAPTER INTERACTIONS
     *********************************************************************/

    override fun onItemSelected(position: Int, item: Workout, containerView: View) {
        if(isMultiSelectionModeEnabled()){
            viewModel.addOrRemoveWorkoutFromSelectedList(item)
        }else{
            viewModel.setWorkoutSelected(item)
            selectionNavigateToManageWorkout(containerView)
        }
    }

    override fun isMultiSelectionModeEnabled(): Boolean = viewModel.isWorkoutMultiSelectionStateActive()

    override fun activateMultiSelectionMode() = viewModel.setWorkoutToolbarState(MultiSelectionState())

    override fun isWorkoutSelected(workout: Workout): Boolean {
        return viewModel.isWorkoutSelected(workout)
    }

    /********************************************************************
        DEBUG
     *********************************************************************/

    private fun printActiveJobs(){

        for((index, job) in viewModel.getActiveJobs().withIndex()){
            printLogD("WorkoutFragment", "${index}: ${job}")
        }
    }

}