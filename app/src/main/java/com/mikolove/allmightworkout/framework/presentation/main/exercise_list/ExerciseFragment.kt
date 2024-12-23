/*
package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.interactors.common.GetExercises
import com.mikolove.core.interactors.exercise.RemoveMultipleExercises
import com.mikolove.core.interactors.workout.RemoveMultipleWorkouts
import com.mikolove.allmightworkout.databinding.FragmentExerciseBinding
import com.mikolove.allmightworkout.framework.datasource.cache.database.*
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState.*
import com.mikolove.workout.presentation.WorkoutListEvents

import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val EXERCISE_VIEW_STATE_BUNDLE_KEY = "com.mikolove.allmightworkout.framework.presentation.main.exercise.state"

@AndroidEntryPoint
class ExerciseFragment(): BaseFragment(R.layout.fragment_exercise),
    ExerciseListAdapter.Interaction,
    FabController {

    val viewModel : ExerciseListViewModel by viewModels()

    @Inject
    lateinit var dateUtil: DateUtil

    private var actionMode : ActionMode? = null
    private var actionModeCallBack : ActionMode.Callback? = null
    private var listAdapter: ExerciseListAdapter? = null
    private var binding : FragmentExerciseBinding? = null


    */
/*******************************************************************
    LIFECYCLE MANANGEMENT
     ********************************************************************//*


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseBinding.bind(view)

        setupUI()
        setupFAB()
        setupRecyclerView()
        setupSwipeRefresh()
        subscribeObservers()

        // If an update occurred from UpdateBlogFragment, refresh the BlogPost
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            SHOULD_REFRESH)?.observe(viewLifecycleOwner) { shouldRefresh ->
            shouldRefresh?.run {
                viewModel.onTriggerEvent(ExerciseListEvents.Refresh)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(SHOULD_REFRESH)
                //findNavController().currentBackStackEntry?.savedStateHandle?.set(SHOULD_REFRESH, null)
            }
        }

        */
/*postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }*//*


    }

    override fun onResume() {
        super.onResume()
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


    */
/*******************************************************************
    FRAGEMENT SAVEINSTANCE
     ********************************************************************//*


    override fun onSaveInstanceState(outState: Bundle) {
        printLogD("ExerciseFragment","OnSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    */
/*******************************************************************
    SUBSCRIBE OBSERVERS
     ********************************************************************//*


    private fun subscribeObservers(){

        viewModel.exerciseToolbarState.observe(viewLifecycleOwner, Observer { toolbarState ->

            when (toolbarState) {

                is MultiSelectionState -> {
                    enableMultiSelectToolbarState()
                }

                is SelectionState -> {
                    disableMultiSelectToolbarState()

                }
            }
        })

        viewModel.state.observe(viewLifecycleOwner, { state ->

            state.isLoading?.let { uiController.displayProgressBar(it) }

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        viewModel.onTriggerEvent(ExerciseListEvents.OnRemoveHeadFromQueue)
                    }
                })

            listAdapter?.apply {
                submitList(list = state.listExercises)
                if(itemCount > 0){
                    showList()
                }else{
                    hideList()
                }
            }
        })

*/
/*        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

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

        })*//*

    }


    */
/*******************************************************************
    SETUP
     ********************************************************************//*


    private fun showList(){
        if(binding?.fragmentExerciseSwiperefreshlayout?.isVisible == false) {
            binding?.fragmentExerciseSwiperefreshlayout?.visible()
            binding?.fragmentExerciseNoExercise?.invisible()
        }
    }

    private fun hideList(){
        if(binding?.fragmentExerciseSwiperefreshlayout?.isVisible == true) {
            binding?.fragmentExerciseSwiperefreshlayout?.invisible()
            binding?.fragmentExerciseNoExercise?.visible()
        }
    }

    private fun setupUI(){
        view?.hideKeyboard()
    }

    override fun setupFAB(){
        uiController.loadFabController(this@ExerciseFragment)
    }

    private fun setupSwipeRefresh(){
        binding?.fragmentExerciseSwiperefreshlayout?.setOnRefreshListener {
            viewModel.onTriggerEvent(ExerciseListEvents.NewSearch)
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
                    if (
                        lastPosition == listAdapter?.itemCount?.minus(1) &&
                        viewModel.state.value?.isLoading == false &&
                        viewModel.state.value?.isQueryExhausted == false
                    ) {
                        printLogD("WorkoutFragment","Next Page")
                        viewModel.onTriggerEvent(ExerciseListEvents.NextPage)
                    }
                }
            })

            listAdapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = listAdapter
        }
    }

    */
/*******************************************************************
        NAVIGATION
     ********************************************************************//*



    private fun selectionNavigateToManageExercise(containerView : View){

       //viewModel.setIsExistExercise(true)
       */
/* val itemDetailTransitionName = getString(R.string.test_exercise_item_detail_transition_name)
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
        }*//*

    }

    */
/*******************************************************************
    UI DIALOG
     ********************************************************************//*


    fun showFilterDialog(){

        activity?.let {

            viewModel.state.value?.let { state ->

                val dialog = MaterialDialog(it)
                    .noAutoDismiss()
                    .customView(R.layout.dialog_filter)

                val view = dialog.getCustomView()

                val filter = state.list_filter.value
                val order = state.list_order.value

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
                        onTriggerEvent(ExerciseListEvents.UpdateFilter(getFilterFromValue(newFilter)))
                        onTriggerEvent(ExerciseListEvents.UpdateOrder(getOrderFromValue(newOrder)))
                        onTriggerEvent(ExerciseListEvents.NewSearch)
                    }

                    //viewModel.onTriggerEvent(ExerciseListEvents.NewSearch)
                    dialog.dismiss()
                }

                view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }

            }
        }

    */
/*******************************************************************
    UI FAB
     ********************************************************************//*


    override fun fabOnClick() {
        //Generate exercise to create
        findNavController().navigate(R.id.action_exercise_fragment_to_exercise_detail_fragment, null)
    }

    */
/*******************************************************************
    ACTION MODE
     ********************************************************************//*


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
        viewModel.setExerciseToolbarState(SelectionState())
        viewModel.clearSelectedExercises()
    }

    */
/*******************************************************************
    MENU
     ********************************************************************//*


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercise, menu)

        //Deal with searchView
        val searchItem = menu.findItem(R.id.menu_exercise_search)
        val searchView = searchItem?.actionView as SearchView

        //Reload standard search when finish
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.onTriggerEvent(ExerciseListEvents.UpdateQuery(""))
                viewModel.setIsSearchActive(false)
                viewModel.onTriggerEvent(ExerciseListEvents.NewSearch)
                return true
            }
        })

        //May Add autocomplete
        val searchAutoComplete: SearchView.SearchAutoComplete? = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                val searchQuery = v.text.toString()
                viewModel.onTriggerEvent(ExerciseListEvents.UpdateQuery(searchQuery))
                viewModel.onTriggerEvent(ExerciseListEvents.NewSearch)
                viewModel.setIsSearchActive(true)
            }
            true
        }

        //Expand if search isActive on menu reload
        //TODO : This .getSearchQuery could be out of the state logic
        if(viewModel.isSearchActive()){
            searchItem.expandActionView()
            searchView.setQuery(viewModel.getSearchQuery(),false)
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


    */
/******************************************************************
        VIEWMODEL ACTIONS
     ********************************************************************//*



    private fun deleteExercises(){

        printLogD("ExerciseListFragment","Launch remove delete workout")
        val message = GenericMessageInfo.Builder()
            .id("ExerciseListFragment.LaunchDelete")
            .title(RemoveMultipleExercises.DELETE_EXERCISES_ARE_YOU_SURE)
            .description("")
            .messageType(MessageType.Success)
            .uiComponentType(UIComponentType.Dialog)
            .positive(
                PositiveAction(
                    positiveBtnTxt = "OK",
                    onPositiveAction = {
                        viewModel.onTriggerEvent(ExerciseListEvents.RemoveSelectedExercises)
                    }
                )
            )
            .negative(
                NegativeAction(
                    negativeBtnTxt = "Delete",
                    onNegativeAction = {}
                )
            )

        launchDialog(message)
    }

    private fun launchDialog( message : GenericMessageInfo.Builder){
        viewModel.onTriggerEvent(ExerciseListEvents.LaunchDialog(message))
    }

    */
/*******************************************************************
    OVERRIDE UI CONTROLLER TOAST SNACKBAR
     ********************************************************************//*


*/
/*
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
*//*


   */
/*******************************************************************
    WORKOUT LIST ADAPTER INTERACTIONS
     ********************************************************************//*


    override fun onItemSelected(position: Int, item: Exercise, containerView: View) {
        if(isMultiSelectionModeEnabled()){
            viewModel.addOrRemoveExerciseFromSelectedList(item)
        }else{
            val bundle = bundleOf("idExercise" to item.idExercise)
            findNavController().navigate(R.id.action_exercise_fragment_to_exercise_detail_fragment, bundle)
        }
    }

    override fun isMultiSelectionModeEnabled(): Boolean = viewModel.isExerciseMultiSelectionStateActive()

    override fun activateMultiSelectionMode() = viewModel.setExerciseToolbarState(
        MultiSelectionState()
    )

    override fun isExerciseSelected(exercise: Exercise): Boolean {
        return viewModel.isExerciseSelected(exercise)
    }
}*/
