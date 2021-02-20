package com.mikolove.allmightworkout.framework.presentation.main.home

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.home.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_ARE_YOU_SURE
import com.mikolove.allmightworkout.business.interactors.main.home.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_ERRORS
import com.mikolove.allmightworkout.business.interactors.main.home.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentChooseWorkoutBinding
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_FILTER_DATE_CREATED
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_FILTER_NAME
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_ORDER_ASC
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_ORDER_DESC
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import com.mikolove.allmightworkout.framework.presentation.main.home.state.ListToolbarState.*
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.MANAGE_WORKOUT_ID_WORKOUT_BUNDLE_KEY
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ChooseWorkoutFragment
: BaseFragment(R.layout.fragment_choose_workout),
    WorkoutListAdapter.Interaction,
    ItemTouchHelperAdapter,
    FabController {

    @Inject
    lateinit var dateUtil: DateUtil
    val viewModel : HomeViewModel by activityViewModels()
    private var actionMode : ActionMode? = null
    private var actionModeCallBack : ActionMode.Callback? = null
    private var listAdapter: WorkoutListAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null
    private var binding : FragmentChooseWorkoutBinding? = null

    override fun fabOnClick() {
        addWorkout()
    }

    override fun setupFAB(){
        uiController.loadFabController(this)
        uiController.mainFabVisibility()

    }


    override fun onDestroyView() {
        printLogD("ChooseWorkoutFragment", "OnDestroyView")
        //disableActionMode()
        binding?.fragmentChooseWorkoutRecyclerview?.adapter = null
        printLogD("ChooseWokoutFragment","Action mode is ${actionMode}")
        binding = null
        listAdapter = null
        itemTouchHelper = null
        super.onDestroyView()
    }


    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTotalWorkouts()
        viewModel.clearListWorkouts()
        viewModel.refreshWorkoutSearchQuery()
        setupBottomNav()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //clear the list. Don't want to save a large list to bundle.
        viewState?.listWorkouts =  ArrayList()

        outState.putParcelable(
            HOME_LIST_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        printLogD("ChooseWorkoutFragment","OnViewCreated")
        setHasOptionsMenu(true)

        binding = FragmentChooseWorkoutBinding.bind(view)

        setupUI()
        setupRecyclerView()
        setupSwipeRefresh()
        setupFAB()
        setupBottomNav()
        subscribeObservers()

        restoreInstanceState(savedInstanceState)
    }

    private fun setupBottomNav(){
        uiController?.displayBottomNavigation(View.VISIBLE)
    }

    private fun navigateToManageWorkout( idWorkout: String){

        val bundle = bundleOf(MANAGE_WORKOUT_ID_WORKOUT_BUNDLE_KEY to idWorkout)
        findNavController().navigate(
            R.id.action_chooseWorkoutFragment_to_manageWorkoutFragment,
            bundle
        )
        viewModel.setInsertedWorkout(null)
    }

    private fun navigateToManageWorkout(containerView : View, idWorkout: String){

        val itemDetailTransitionName = getString(R.string.test_workout_item_detail_transition_name)
        val extras = FragmentNavigatorExtras(containerView to itemDetailTransitionName)

        val bundle = bundleOf(MANAGE_WORKOUT_ID_WORKOUT_BUNDLE_KEY to idWorkout)
        findNavController().navigate(
            R.id.action_chooseWorkoutFragment_to_manageWorkoutFragment,
            bundle,
            null,
            extras
        )
        viewModel.setInsertedWorkout(null)
    }

    private fun subscribeObservers(){

        viewModel.workoutToolbarState.observe(viewLifecycleOwner, Observer { toolbarState ->

            when (toolbarState) {

                is MultiSelectionState -> {
                    enableMultiSelectToolbarState()
                    //disableSearchViewToolbarState()

                }

                is SearchViewState -> {
                    //enableSearchViewToolbarState()
                    disableMultiSelectToolbarState()
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {

                viewState.listWorkouts?.let { workoutList ->

                    if (viewModel.isWorkoutsPaginationExhausted() && !viewModel.isWorkoutsQueryExhausted()) {
                        viewModel.setWorkoutQueryExhausted(true)
                    }
                    printLogD("ChooseWorkoutFragment","List size ${workoutList.size}")
                    listAdapter?.submitList(workoutList)
                    listAdapter?.notifyDataSetChanged()
                }

                viewState.insertedWorkout?.let { insertedWorkout ->
                    navigateToManageWorkout(insertedWorkout.idWorkout)
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer {
            printActiveJobs()
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when (response.message) {

                     DELETE_WORKOUTS_SUCCESS -> {

                        showSnackbarDeleteWorkouts()
                        viewModel.setWorkoutToolbarState(SearchViewState())

                    }

                    DELETE_WORKOUTS_ERRORS -> {

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


    //Allow the app to restart at list position
    private fun saveLayoutManagerState(){
        binding?.fragmentChooseWorkoutRecyclerview?.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setWorkoutsLayoutManagerState(lmState)
        }
    }

    private fun showSnackbarDeleteWorkouts(){
        uiController.onResponseReceived(
            response = Response(
                message = DELETE_WORKOUTS_SUCCESS,
                uiComponentType = UIComponentType.SnackBar(
                    undoCallback = null,
                    onDismissCallback = null
                ),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object : StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    private fun setupMenu(){

        setHasOptionsMenu(true)
        activity?.let {
            //(it as AppCompatActivity).startSupportActionMode()
        }
    }



    private fun initializeActionMode() : ActionMode.Callback = object : ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            activity?.menuInflater?.inflate(R.menu.toolbar_workout_menu_multiselection, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.toolbar_workout_delete -> {
                    printLogD("ChooseWorkoutFragment", "DELETE IS PRESSED")
                    deleteWorkouts()
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            printLogD("ChooseWorkoutFragment", "Close is pressed")
            disableActionMode()
        }
    }


    private fun disableActionMode(){
        viewModel.setWorkoutToolbarState(SearchViewState())
        viewModel.clearSelectedWorkouts()
        //actionMode?.finish()
        //actionMode = null
    }

    private fun setupUI(){
        view?.hideKeyboard()
    }

    private fun setupRecyclerView(){
        binding?.fragmentChooseWorkoutRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            itemTouchHelper = ItemTouchHelper(
                ItemTouchHelperCallback<Workout>(
                    this@ChooseWorkoutFragment,
                    viewModel.workoutListInteractionManager
                )
            )
            //itemTouchHelper?.attachToRecyclerView(this)

            listAdapter = WorkoutListAdapter(
                this@ChooseWorkoutFragment,
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

            adapter = listAdapter
        }
    }

    private fun setupSwipeRefresh(){
        binding?.fragmentChooseWorkoutSwiperefreshlayout?.setOnRefreshListener {
            startNewSearch()
            binding?.fragmentChooseWorkoutSwiperefreshlayout?.isRefreshing = false
        }
    }

    private fun startNewSearch(){
        viewModel.reloadWorkouts()
    }


    /*
        Toolbar Setup and Actions
     */


/*    private fun setupMultiSelectionToolbar(parentView: View){
        parentView
            .findViewById<ImageView>(R.id.action_exit_multiselect_state)
            .setOnClickListener {
                viewModel.setWorkoutToolbarState(SearchViewState())
            }

        parentView
            .findViewById<ImageView>(R.id.action_delete_items)
            .setOnClickListener {
                deleteWorkouts()
            }
    }

    private fun enableMultiSelectToolbarState(){
        view?.let { v ->
            val view = View.inflate(
                v.context,
                R.layout.layout_multiselection_toolbar,
                null
            )
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            binding?.fragmentChooseWorkoutToolbarContentContainer?.addView(view)
            setupMultiSelectionToolbar(view)
        }
    }

    private fun disableMultiSelectToolbarState(){
        view?.let {
            val view = binding?.fragmentChooseWorkoutToolbarContentContainer?.findViewById<Toolbar>(R.id.multiselect_toolbar)
            binding?.fragmentChooseWorkoutToolbarContentContainer?.removeView(view)
            viewModel.clearSelectedWorkouts()
        }
    }


    private fun setupSearchView(){

        val searchViewToolbar: Toolbar? = binding?.fragmentChooseWorkoutToolbarContentContainer?.findViewById<Toolbar>(R.id.searchview_toolbar)

        searchViewToolbar?.let { toolbar ->

            val searchView = toolbar.findViewById<SearchView>(R.id.search_view)

            val searchPlate: SearchView.SearchAutoComplete?
                    = searchView.findViewById(androidx.appcompat.R.id.search_src_text)

            searchPlate?.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                    || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                    val searchQuery = v.text.toString()
                    viewModel.setQueryWorkouts(searchQuery)
                    startNewSearch()
                }
                true
            }
        }
    }

    private fun enableSearchViewToolbarState(){
        view?.let { v ->
            val view = View.inflate(
                v.context,
                R.layout.layout_searchview_toolbar,
                null
            )
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            binding?.fragmentChooseWorkoutToolbarContentContainer?.addView(view)
            setupSearchView()
            setupFilterButton()
        }
    }

    private fun disableSearchViewToolbarState(){
        view?.let {
            val view = binding?.fragmentChooseWorkoutToolbarContentContainer?.findViewById<Toolbar>(R.id.searchview_toolbar)
            binding?.fragmentChooseWorkoutToolbarContentContainer?.removeView(view)
        }
    }


    private fun setupFilterButton(){
        val searchViewToolbar: Toolbar? =  binding?.fragmentChooseWorkoutToolbarContentContainer?.findViewById<Toolbar>(R.id.searchview_toolbar)
        searchViewToolbar?.findViewById<ImageView>(R.id.action_filter)?.setOnClickListener {
            showFilterDialog()
        }
    }*/


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_workout_menu_search, menu)

        //Deal with searchView
        val searchItem = menu?.findItem(R.id.toolbar_workout_search)
        val searchView = searchItem?.actionView as SearchView

        //May Add autocomplete
        val searchAutoComplete: SearchView.SearchAutoComplete? = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                val searchQuery = v.text.toString()
                viewModel.setQueryWorkouts(searchQuery)
                startNewSearch()
                printLogD("ChooseWorkoutFragment", "Started search on ${searchQuery}")
            }
            true
        }
        searchView.setOnCloseListener {
            printLogD("ChooseWorkoutFragment", "Close search view")
            viewModel.clearSelectedWorkouts()
            false
        }

    }

    private fun enableMultiSelectToolbarState(){
        printLogD("ChooseWorkoutFragment", "Start action Mode")
        if(actionMode == null) {
            printLogD("ChooseWorkoutFragment", "Action mode  null")
            actionModeCallBack = initializeActionMode()
            actionMode = activity?.startActionMode(actionModeCallBack)
            uiController?.displayBottomNavigation(View.INVISIBLE)
        }
    }

    private fun disableMultiSelectToolbarState(){
        printLogD("ChooseWorkoutFragment", "Stop action Mode")
        if(actionMode != null){
            printLogD("ChooseWorkoutFragment", "Action Mode is not null")
            actionMode?.finish()
            actionModeCallBack = null
            actionMode = null
            uiController?.displayBottomNavigation(View.VISIBLE)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_workout_filter -> {
                // navigate to settings screen
                printLogD("ChooseWorkoutFragment", "Show filter dialog")
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


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


    private fun addWorkout(){
        uiController.displayInputCaptureDialog(
            getString(R.string.fragment_choose_workout_add_name),
            object : DialogInputCaptureCallback {
                override fun onTextCaptured(text: String) {
                    if (!text.isNullOrEmpty() && text.isNotBlank()) {
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

    // for debugging
    private fun printActiveJobs(){

        for((index, job) in viewModel.getActiveJobs().withIndex()){
            printLogD("ChooseWorkoutFragment", "${index}: ${job}")
        }
    }


    /*
        List Adapter Interactions
     */
    override fun onItemSelected(position: Int, item: Workout) {
        if(isMultiSelectionModeEnabled()){
            viewModel.addOrRemoveWorkoutFromSelectedList(item)
        }else{
            navigateToManageWorkout(item.idWorkout)
        }

    }

    override fun onItemSelected(position: Int, item: Workout, containerView: View) {
        if(isMultiSelectionModeEnabled()){
            viewModel.addOrRemoveWorkoutFromSelectedList(item)
        }else{
            navigateToManageWorkout(containerView,item.idWorkout)
        }
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            binding?.fragmentChooseWorkoutRecyclerview?.layoutManager?.onRestoreInstanceState(
                lmState
            )
        }
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.let { inState ->
            (inState[HOME_LIST_STATE_BUNDLE_KEY] as HomeViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun isMultiSelectionModeEnabled(): Boolean = viewModel.isWorkoutMultiSelectionStateActive()

    override fun activateMultiSelectionMode() = viewModel.setWorkoutToolbarState(MultiSelectionState())

    override fun isWorkoutSelected(workout: Workout): Boolean {
        return viewModel.isWorkoutSelected(workout)
    }

    /*
        Item Touch Helper
     */
    override fun onItemSwiped(position: Int) {
        //TODO("Not yet implemented")
    }

}