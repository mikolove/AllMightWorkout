package com.mikolove.allmightworkout.framework.presentation.main.workout_list

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.mikolove.allmightworkout.databinding.FragmentWorkoutBinding
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_FILTER_DATE_CREATED
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_FILTER_NAME
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_ORDER_ASC
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_ORDER_DESC
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents.*
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val WORKOUT_VIEW_STATE_BUNDLE_KEY = "com.mikolove.allmightworkout.framework.presentation.main.workout.state"

@AndroidEntryPoint
class WorkoutFragment
    : BaseFragment(R.layout.fragment_workout),
    WorkoutListAdapter.Interaction,
    FabController {

    val viewModel : WorkoutListViewModel by viewModels()

    @Inject
    lateinit var dateUtil: DateUtil

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        printLogD("WorkoutFragment","OnViewCreated")
        setHasOptionsMenu(true)

        binding = FragmentWorkoutBinding.bind(view)

        setupUI()
        setupFAB()
        setupRecyclerView()
        setupSwipeRefresh()
        subscribeObservers()

        // If an update occurred from UpdateBlogFragment, refresh the BlogPost
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            SHOULD_REFRESH)?.observe(viewLifecycleOwner) { shouldRefresh ->
            shouldRefresh?.run {
                viewModel.onTriggerEvent(Refresh)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(SHOULD_REFRESH)
                //findNavController().currentBackStackEntry?.savedStateHandle?.set(SHOULD_REFRESH, null)
            }
        }


/*        MaterialDialog(requireActivity())
            .show{
                title(text = "Test")
                message(text = "Test")
                positiveButton(text = "Ok"){
                    dismiss()
                }
                negativeButton(text = "Cancel"){
                    dismiss()
                }
                onDismiss {
                    dismiss()
                }
                cancelable(false)
            }*/
        /*  postponeEnterTransition()
          view.doOnPreDraw { startPostponedEnterTransition() }
  */
    }

    override fun onResume() {
        super.onResume()
        printLogD("WorkoutFragment","OnResume")
    }

    override fun onPause() {
        super.onPause()
        printLogD("WorkoutFragment","OnPause")

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
/*        val viewState = viewModel.viewState.value

        viewState?.listWorkouts =  ArrayList()
        viewState?.listBodyParts = ArrayList()
        viewState?.listWorkoutTypes = ArrayList()
        viewState?.listExercisesFromWorkoutId = ArrayList()
        viewState?.workoutSelected = null
        viewState?.workoutToInsert = null

        outState.putParcelable(
            WORKOUT_VIEW_STATE_BUNDLE_KEY,
            viewState
        )*/
        super.onSaveInstanceState(outState)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        printLogD("WorkoutFragment","restoreInstanceState")
        /*savedInstanceState?.let { inState ->
            (inState[WORKOUT_VIEW_STATE_BUNDLE_KEY] as WorkoutViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }*/
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


        viewModel.state.observe(viewLifecycleOwner, { state ->

            state.isLoading?.let { uiController.displayProgressBar(it) }

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        viewModel.onTriggerEvent(OnRemoveHeadFromQueue)
                    }
                })

            listAdapter?.apply {
                submitList(list = state.listWorkouts)
                if(itemCount > 0){
                    showList()
                }else{
                    hideList()
                }
            }
        })

        /* viewModel.state.observe(viewLifecycleOwner, Observer { state ->

            if (state != null) {

                state.listWorkouts.let { workoutList ->

                    if(workoutList.size > 0){
                        if (viewModel.isWorkoutsPaginationExhausted() && !viewModel.isWorkoutsQueryExhausted()) {
                            viewModel.setWorkoutQueryExhausted(true)
                        }
                        listAdapter?.submitList(workoutList)
                        showList()
                    }else{
                        hideList()
                    }
                }

               viewState.workoutToInsert?.let { insertedWorkout ->
                    printLogD("WorkoutFragment","INSERTED WORKOUT OBSERVED IN FRAGMENT")
                    viewModel.getWorkoutById(insertedWorkout.idWorkout)
                }*/

        /*viewState.workoutSelected?.let { _ ->
            if(viewModel.getWorkoutToInsert() != null) {
                insertionNavigateToManageWorkout()
            }
        }
    }
})*/

        /*viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { isLoading ->
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

                    GET_WORKOUTS_NO_MATCHING_RESULTS -> {
                        showToastNoMatchingWorkouts()
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

        })*/
    }


    /********************************************************************
    SETUP
     *********************************************************************/

    private fun showList(){
        if(binding?.fragmentWorkoutSwiperefreshlayout?.isVisible == false) {
            binding?.fragmentWorkoutSwiperefreshlayout?.visible()
            binding?.fragmentWorkoutNoWorkout?.invisible()
        }
    }

    private fun hideList(){
        if(binding?.fragmentWorkoutSwiperefreshlayout?.isVisible == true){
            binding?.fragmentWorkoutSwiperefreshlayout?.invisible()
            binding?.fragmentWorkoutNoWorkout?.visible()
        }
    }

    private fun setupUI(){
        view?.hideKeyboard()
    }

    override fun setupFAB(){
        uiController.loadFabController(this@WorkoutFragment)
        uiController.mainFabVisibility()
    }

    private fun setupSwipeRefresh(){
        binding?.fragmentWorkoutSwiperefreshlayout?.setOnRefreshListener {
            viewModel.onTriggerEvent(NewSearch)
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
                    printLogD("WorkoutFragment","Is onscroll exhausted : ${viewModel.state.value?.isQueryExhausted}")
                    if (
                        lastPosition == listAdapter?.itemCount?.minus(1) &&
                        viewModel.state.value?.isLoading == false &&
                        viewModel.state.value?.isQueryExhausted == false
                    ) {
                        printLogD("WorkoutFragment","Next Page")
                        viewModel.onTriggerEvent(NextPage)
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

/*    private fun insertionNavigateToManageWorkout(){
        findNavController().navigate(
            R.id.action_workout_fragment_to_workout_detail_fragment,
            null
        )
        viewModel.setInsertedWorkout(null)
    }*/

    private fun selectionNavigateToManageWorkout(containerView : View?){

        findNavController().navigate(
            R.id.action_workout_fragment_to_workout_detail_fragment,
            null
        )
/*        val itemDetailTransitionName = getString(R.string.test_workout_item_detail_transition_name)
        val extras = FragmentNavigatorExtras(containerView to itemDetailTransitionName)
        findNavController().navigate(
            R.id.action_workout_fragment_to_workout_detail_fragment,
            null,
            null,
            extras
        )

        exitTransition = MaterialElevationScale(false).apply {
            duration = 300.toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300.toLong()
        }*/
    }

    /********************************************************************
    UI DIALOG
     *********************************************************************/


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
                        onTriggerEvent(UpdateFilter(getFilterFromValue(newFilter)))
                        onTriggerEvent(UpdateOrder(getOrderFromValue(newOrder)))
                        onTriggerEvent(NewSearch)
                    }

                    //viewModel.onTriggerEvent(NewSearch)
                    dialog.dismiss()
                }

                view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }

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

        //Deal with searchView
        val searchItem = menu.findItem(R.id.menu_workout_search)
        val searchView = searchItem?.actionView as SearchView

        //Reload standard search when finish
        searchItem.setOnActionExpandListener(object :MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.onTriggerEvent(UpdateQuery(""))
                viewModel.setIsSearchActive(false)
                viewModel.onTriggerEvent(NewSearch)
                return true
            }
        })

        //May Add autocomplete
        val searchAutoComplete: SearchView.SearchAutoComplete? = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                val searchQuery = v.text.toString()
                viewModel.onTriggerEvent(UpdateQuery(searchQuery))
                viewModel.onTriggerEvent(NewSearch)
                viewModel.setIsSearchActive(true)
            }
            true
        }

        //Expand if search isActive on menu reload
        if(viewModel.isSearchActive()){
            searchItem.expandActionView()
            searchView.setQuery(viewModel.getSearchQuery(),false)
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


    private fun addWorkout(){

        val message = GenericMessageInfo.Builder()
            .id("WorkoutListFragment.AddWorkoutDialog")
            .title(getString(R.string.fragment_choose_workout_add_name))
            .messageType(MessageType.Success)
            .uiComponentType(
                UIComponentType.InputCaptureDialog(
                    object : DialogInputCaptureCallback {
                        override fun onTextCaptured(text: String) {
                            if (text.isNotBlank()) {
                                viewModel.onTriggerEvent(InsertWorkout(name = text))
                            }
                        }
                    }
                ))

        launchDialog(message)
    }

    private fun deleteWorkouts(){

        printLogD("WorkoutListFragment","Launch remove delete workout")
        val message = GenericMessageInfo.Builder()
            .id("WorkoutFragment.LaunchDelete")
            .title(DELETE_WORKOUTS_ARE_YOU_SURE)
            .description("")
            .messageType(MessageType.Success)
            .uiComponentType(UIComponentType.Dialog)
            .positive(
                PositiveAction(
                    positiveBtnTxt = "OK",
                    onPositiveAction = {
                        viewModel.onTriggerEvent(RemoveSelectedWorkouts)
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
        viewModel.onTriggerEvent(LaunchDialog(message))
    }

    /********************************************************************
    OVERRIDE UI CONTROLLER TOAST SNACKBAR
     *********************************************************************/

/*    private fun showToastDeleteWorkouts(){

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

    private fun showToastNoMatchingWorkouts(){

        uiController.onResponseReceived(
            response = Response(
                message = GET_WORKOUTS_NO_MATCHING_RESULTS,
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object: StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }*/

    /********************************************************************
    WORKOUT LIST ADAPTER INTERACTIONS
     *********************************************************************/

    override fun onItemSelected(position: Int?, item: Workout, containerView: View?) {
        if(isMultiSelectionModeEnabled()){
            viewModel.addOrRemoveWorkoutFromSelectedList(item)
        }else{

            val bundle = bundleOf("idWorkout" to item.idWorkout)
            findNavController().navigate(R.id.action_workout_fragment_to_workout_detail_fragment, bundle)
            //selectionNavigateToManageWorkout(containerView)
        }
    }

    override fun isMultiSelectionModeEnabled(): Boolean = viewModel.isWorkoutMultiSelectionStateActive()

    override fun activateMultiSelectionMode() = viewModel.setWorkoutToolbarState(MultiSelectionState())

    override fun isWorkoutSelected(workout: Workout): Boolean {
        return viewModel.isWorkoutSelected(workout)
    }


}