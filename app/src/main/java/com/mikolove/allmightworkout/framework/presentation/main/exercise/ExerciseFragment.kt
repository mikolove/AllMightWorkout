package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.transition.MaterialElevationScale
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.exercise.GetExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises
import com.mikolove.allmightworkout.databinding.FragmentExerciseBinding
import com.mikolove.allmightworkout.framework.datasource.cache.database.*
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val EXERCISE_VIEW_STATE_BUNDLE_KEY = "com.mikolove.allmightworkout.framework.presentation.main.exercise.state"

@AndroidEntryPoint
class ExerciseFragment(): BaseFragment(R.layout.fragment_exercise),
    ExerciseListAdapter.Interaction,
    FabController {

    val viewModel : ExerciseViewModel by activityViewModels()

    @Inject
    lateinit var dateUtil: DateUtil

    private var actionMode : ActionMode? = null
    private var actionModeCallBack : ActionMode.Callback? = null
    private var listAdapter: ExerciseListAdapter? = null
    private var binding : FragmentExerciseBinding? = null


    /********************************************************************
    LIFECYCLE MANANGEMENT
     *********************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseBinding.bind(view)

        setupUI()
        setupRecyclerView()
        setupSwipeRefresh()
        subscribeObservers()

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

    }

    override fun onResume() {
        super.onResume()
        setupFAB()
        viewModel.loadTotalExercises()
        //viewModel.clearListExercises()
        viewModel.loadWorkoutTypes()
        viewModel.refreshExerciseSearchQuery()
    }

    override fun onDestroyView() {
        disableMultiSelectToolbarState()
        binding?.fragmentExerciseRecyclerview?.adapter = null
        listAdapter = null
        binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        printLogD("ExerciseFragment", "OnDestroy")
    }


    /********************************************************************
    FRAGEMENT SAVEINSTANCE
     *********************************************************************/

    override fun onSaveInstanceState(outState: Bundle) {
        printLogD("ExerciseFragment","OnSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        printLogD("ExerciseFragment","restoreInstanceState")
        savedInstanceState?.let { inState ->
            (inState[EXERCISE_VIEW_STATE_BUNDLE_KEY] as ExerciseViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    /********************************************************************
    SUBSCRIBE OBSERVERS
     *********************************************************************/

    private fun subscribeObservers(){

        viewModel.exerciseToolbarState.observe(viewLifecycleOwner, Observer { toolbarState ->

            when (toolbarState) {

                is ListToolbarState.MultiSelectionState -> {
                    enableMultiSelectToolbarState()
                }

                is ListToolbarState.SelectionState -> {
                    disableMultiSelectToolbarState()

                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {

                viewState.listExercises?.let { exerciseList ->

                    if(exerciseList.size > 0){
                        if (viewModel.isExercisesPaginationExhausted() && !viewModel.isExercisesQueryExhausted()) {
                            viewModel.setExerciseQueryExhausted(true)
                        }
                        listAdapter?.submitList(exerciseList)
                        showList()
                    }else{
                        hideList()
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

                    RemoveMultipleExercises.DELETE_EXERCISES_SUCCESS -> {

                        showToastDeleteExercises()
                        disableActionMode()
                    }

                    RemoveMultipleExercises.DELETE_EXERCISES_ERRORS -> {
                        showToastDeleteExercisesError()
                        disableActionMode()
                    }

                    GetExercises.GET_EXERCISES_NO_MATCHING_RESULTS -> {
                            showToastNoMatchingExercises()
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

    private fun showList(){
        binding?.fragmentExerciseSwiperefreshlayout?.fadeIn()
        binding?.fragmentExerciseNoExercise?.fadeOut()
    }

    private fun hideList(){
        binding?.fragmentExerciseSwiperefreshlayout?.fadeOut()
        binding?.fragmentExerciseNoExercise?.fadeIn()
    }

    private fun setupUI(){
        view?.hideKeyboard()
    }

    override fun setupFAB(){
        uiController.loadFabController(this)
        uiController.mainFabVisibility()
    }

    private fun setupSwipeRefresh(){
        binding?.fragmentExerciseSwiperefreshlayout?.setOnRefreshListener {
            startNewSearch()
            binding?.fragmentExerciseSwiperefreshlayout?.isRefreshing = false
        }
    }

    private fun setupRecyclerView(){
        binding?.fragmentExerciseRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            listAdapter = ExerciseListAdapter(
                this@ExerciseFragment,
                viewLifecycleOwner,
                viewModel.exerciseListInteractionManager.selectedItems,
                dateUtil
            )


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == listAdapter?.itemCount?.minus(1)) {
                        viewModel.nextPageExercises()
                    }
                }
            })

            listAdapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = listAdapter
        }
    }

    /********************************************************************
        NAVIGATION
     *********************************************************************/

    private fun insertionNavigateToManageExercise(){
        findNavController().navigate(
            R.id.action_exercise_fragment_to_exercise_detail_fragment,
            null
        )
        viewModel.setIsExistExercise(false)
    }

    private fun selectionNavigateToManageExercise(containerView : View){

        viewModel.setIsExistExercise(true)
        val itemDetailTransitionName = getString(R.string.test_exercise_item_detail_transition_name)
        val extras = FragmentNavigatorExtras(containerView to itemDetailTransitionName)
        findNavController().navigate(
            R.id.action_exercise_fragment_to_exercise_detail_fragment,
            null,
            null,
            extras
        )

        exitTransition = MaterialElevationScale(false).apply {
            duration = 300.toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300.toLong()
        }
    }

    /********************************************************************
    UI DIALOG
     *********************************************************************/

    fun showFilterDialog(){

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.dialog_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilterExercises()
            val order = viewModel.getOrderExercises()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    EXERCISE_FILTER_DATE_CREATED -> check(R.id.filter_date)
                    EXERCISE_FILTER_NAME -> check(R.id.filter_title)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    EXERCISE_ORDER_ASC -> check(R.id.filter_asc)
                    EXERCISE_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_title -> EXERCISE_FILTER_NAME
                        R.id.filter_date -> EXERCISE_FILTER_DATE_CREATED
                        else -> EXERCISE_FILTER_DATE_CREATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterExercisesOptions(newFilter, newOrder)
                    setExerciseListFilter(newFilter)
                    setExerciseOrder(newOrder)
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
        //Generate exercise to create
        val exercise = viewModel.createExercise()
        viewModel.setExerciseSelected(exercise)
        insertionNavigateToManageExercise()
    }

    /********************************************************************
    ACTION MODE
     *********************************************************************/

    private fun createActionModeCallBack() : ActionMode.Callback = object : ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            activity?.menuInflater?.inflate(R.menu.menu_exercise_actionmode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.toolbar_exercise_delete -> {
                    deleteExercises()
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
        viewModel.setExerciseToolbarState(ListToolbarState.SelectionState())
        viewModel.clearSelectedExercises()
    }

    /********************************************************************
    MENU
     *********************************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercise, menu)

        //Deal with searchView
        val searchItem = menu.findItem(R.id.menu_exercise_search)
        val searchView = searchItem?.actionView as SearchView

        //Reload standard search when finish
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.setQueryExercises("")
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
                viewModel.setQueryExercises(searchQuery)
                startNewSearch()
                viewModel.setIsSearchActive(true)
            }
            true
        }

        //Expand if search isActive on menu reload
        if(viewModel.isSearchActive()){
            searchItem.expandActionView()
            searchView.setQuery(viewModel.getSearchQueryExercises(),false)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_exercise_filter -> {
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
        viewModel.clearListExercises()
        viewModel.exercisesStartNewSearch()
    }

    private fun onErrorNoNameSpecified(){
        viewModel.setStateEvent(
            ExerciseStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = INSERT_EXERCISE_ERROR_NO_NAME,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        )
    }
    private fun deleteExercises(){
        viewModel.setStateEvent(
            ExerciseStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = RemoveMultipleExercises.DELETE_EXERCISES_ARE_YOU_SURE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    viewModel.deleteExercises()
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

    private fun showToastDeleteExercises(){

        uiController.onResponseReceived(
            response = Response(
                message = RemoveMultipleExercises.DELETE_EXERCISES_SUCCESS,
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

    private fun showToastDeleteExercisesError(){

        uiController.onResponseReceived(
            response = Response(
                message = RemoveMultipleExercises.DELETE_EXERCISES_ERRORS,
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

    private fun showToastNoMatchingExercises(){

        uiController.onResponseReceived(
            response = Response(
                message = GetExercises.GET_EXERCISES_NO_MATCHING_RESULTS,
                uiComponentType = UIComponentType.None(),
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

    override fun onItemSelected(position: Int, item: Exercise, containerView: View) {
        if(isMultiSelectionModeEnabled()){
            viewModel.addOrRemoveExerciseFromSelectedList(item)
        }else{
            viewModel.setExerciseSelected(item)
            selectionNavigateToManageExercise(containerView)
        }
    }

    override fun isMultiSelectionModeEnabled(): Boolean = viewModel.isExerciseMultiSelectionStateActive()

    override fun activateMultiSelectionMode() = viewModel.setExerciseToolbarState(ListToolbarState.MultiSelectionState())

    override fun isExerciseSelected(exercise: Exercise): Boolean {
        return viewModel.isExerciseSelected(exercise)
    }

    /********************************************************************
    DEBUG
     *********************************************************************/

    private fun printActiveJobs(){

        for((index, job) in viewModel.getActiveJobs().withIndex()){
            printLogD("ExerciseFragment", "${index}: ${job}")
        }
    }

}