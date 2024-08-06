package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail
/*

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.FragmentExerciseDetailBinding
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.UIController
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise_list.ExerciseListEvents
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseDetailFragment():
    BaseFragment(R.layout.fragment_exercise_detail),
    ExerciseSetListAdapter.Interaction{

    val viewModel : ExerciseDetailViewModel by viewModels()

    @Inject
    lateinit var dateUtil : DateUtil

    private var binding : FragmentExerciseDetailBinding? = null
    private var exerciseSetAdapter : ExerciseSetListAdapter? = null

    private var workoutTypeAdapter : WorkoutTypeAdapter? = null
    private var bodyPartAdapter : BodyPartAdapter? = null
    private var exerciseTypeAdapter : ExerciseTypeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseDetailBinding.bind(view)

        //Loading workout from savestatehandle in viewmodel
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ExerciseSet>(
            EXERCISE_SET_UPDATED
        )?.observe(viewLifecycleOwner) { exerciseSet ->
            exerciseSet?.run {
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<ExerciseSet>(EXERCISE_SET_UPDATED)
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateSet(exerciseSet))
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            SHOULD_REFRESH)?.observe(viewLifecycleOwner) { shouldRefresh ->
            shouldRefresh?.run {
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(SHOULD_REFRESH)
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateLoadInitialValues(true))
            }
        }

        setupAdapters()
        setupButtonAction()
        setupOnBackPressDispatcher()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {

        exerciseSetAdapter = null
        workoutTypeAdapter = null
        bodyPartAdapter = null
        exerciseTypeAdapter = null
        binding = null

        super.onDestroyView()
    }


    */
/********************************************************************
    MENU INTERACTIONS
     *********************************************************************//*


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    */
/********************************************************************
    Observer
     *********************************************************************//*


    private fun subscribeObservers(){

        viewModel.state.observe(viewLifecycleOwner, { state ->

            state.isLoading?.let { uiController.displayProgressBar(it) }

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        viewModel.onTriggerEvent(ExerciseDetailEvents.OnRemoveHeadFromQueue)
                    }
                })

            if(state.loadInitialValues){
                loadInitialValues()
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateLoadInitialValues(false))
            }

            if(state.workoutTypes.isNotEmpty()){
                workoutTypeAdapter?.apply {
                    if(getItems().isEmpty()){
                        submitList(state.workoutTypes)
                    }
                }
            }

            if(state.bodyParts.isNotEmpty()){
                bodyPartAdapter?.apply {
                    if(!getItems().containsAll(state.bodyParts)){
                        submitList(state.bodyParts)
                        binding?.exerciseDetailBodypart?.isEnabled = true
                    }
                }
            }else{
                binding?.exerciseDetailBodypart?.isEnabled = false
            }

            if(state.exerciseTypes.isNotEmpty()){
                exerciseTypeAdapter?.apply {
                    if(!getItems().containsAll(state.exerciseTypes)){
                        submitList(state.exerciseTypes)
                    }
                }

            }
            state.exercise?.sets?.let { sets ->
                exerciseSetAdapter?.apply {
                    submitList(list = sets.sortedBy { it.order })
                }
            }

            //change this with better ui
            binding?.exerciseDetailButtonValidate?.isEnabled = state.isUpdatePending && viewModel.isExerciseValid()
            binding?.exerciseDetailButtonValidate?.text = if(state.isInCache) "Update" else "Add"
        })

        viewModel.exerciseNameInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is ExerciseInteractionState.DefaultState ->{
                    view?.hideKeyboard()
                    binding?.exerciseDetailTextName?.clearFocus()
                }
                is ExerciseInteractionState.EditState ->{
                    viewModel.onTriggerEvent(ExerciseDetailEvents.OnUpdateIsPending(true))
                }
            }
        })

        viewModel.exerciseIsActiveInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is ExerciseInteractionState.DefaultState ->{ }
                is ExerciseInteractionState.EditState ->{
                    viewModel.onTriggerEvent(ExerciseDetailEvents.OnUpdateIsPending(true))
                }
            }
        })

        viewModel.exerciseWorkoutTypeInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is ExerciseInteractionState.DefaultState ->{
                    binding?.exerciseDetailWorkouttype?.clearFocus()
                }
                is ExerciseInteractionState.EditState ->{
                    viewModel.onTriggerEvent(ExerciseDetailEvents.OnUpdateIsPending(true))
                }
            }
        })

        viewModel.exerciseBodyPartInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is ExerciseInteractionState.DefaultState ->{
                    binding?.exerciseDetailBodypart?.clearFocus()
                }
                is ExerciseInteractionState.EditState ->{
                    viewModel.onTriggerEvent(ExerciseDetailEvents.OnUpdateIsPending(true))
                }
            }
        })

        viewModel.exerciseTypeInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is ExerciseInteractionState.DefaultState ->{
                    binding?.exerciseDetailExercisetype?.clearFocus()
                }
                is ExerciseInteractionState.EditState ->{
                    viewModel.onTriggerEvent(ExerciseDetailEvents.OnUpdateIsPending(true))
                }
            }
        })

    }

    */
/********************************************************************
    Setup view
     *********************************************************************//*

    private fun loadInitialValues(){
        viewModel.state.value?.let{ state ->
            state.exercise?.let {
                //Static
                binding?.exerciseDetailTextName?.editText?.setText(it.name)
                setExerciseIsActive(it.isActive)

                //AutoComplete
                val workoutType = viewModel.getExerciseWorkoutType()
                (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setText(workoutType?.name?.capitalize(),false)
                (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setText(it.bodyPart?.name?.capitalize(),false)
                (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setText(it.exerciseType.type.capitalize(),false)
            }
        }
    }

    private fun setupButtonAction(){

        binding?.exerciseDetailButtonValidate?.setOnClickListener {
            viewModel.state.value?.let { state ->
                if(state.isInCache){
                    viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExercise)
                }else{
                    viewModel.onTriggerEvent(ExerciseDetailEvents.InsertExercise)
                }
            }
        }

        binding?.exerciseDetailButtonAdd?.setOnClickListener {
            viewModel.onTriggerEvent(ExerciseDetailEvents.AddSet)
        }

        binding?.exerciseDetailTextName?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickExerciseName()
                }
            }
        })

        binding?.exerciseDetailTextName?.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrBlank()){
                    binding?.exerciseDetailTextName?.error = "Invalid name"
                }else{
                    binding?.exerciseDetailTextName?.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding?.exerciseDetailSwitchIsactive?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                onClickIsActive(isChecked)
            }
        })
        binding?.exerciseDetailWorkouttype?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickWorkoutType()
                }
            }
        })

        binding?.exerciseDetailBodypart?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickBodyPart()
                }
            }
        })

        binding?.exerciseDetailExercisetype?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickExerciseType()
                }
            }
        })
    }

    private fun setupAdapters(){

        //WorkoutTypeAdapter
        workoutTypeAdapter = WorkoutTypeAdapter(requireContext(), R.layout.item_workout_type, mutableListOf())
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setAdapter(workoutTypeAdapter)
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _  ->
            workoutTypeAdapter?.getItem(position)?.idWorkoutType?.let {
                viewModel.onTriggerEvent(ExerciseDetailEvents.GetBodyParts(it))
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseBodyPart(null))
                (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setText("",false)
            }
        }

        //WorkoutTypeAdapter
        bodyPartAdapter = BodyPartAdapter(requireContext(), R.layout.item_body_part, mutableListOf())
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setAdapter(bodyPartAdapter)
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            bodyPartAdapter?.getItem(position)?.let {
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseBodyPart(it))
            }
        }

        //WorkoutTypeAdapter
        exerciseTypeAdapter = ExerciseTypeAdapter(requireContext(),R.layout.item_exercise_type, mutableListOf())
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setAdapter(exerciseTypeAdapter)
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            exerciseTypeAdapter?.getItem(position)?.let {
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseExerciseType(it))
            }
        }

        binding?.exerciseDetailRecyclerview?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            exerciseSetAdapter = ExerciseSetListAdapter(
                this@ExerciseDetailFragment,
                viewLifecycleOwner,
                viewModel.state
            )

            adapter = exerciseSetAdapter
        }
    }

    */
/********************************************************************
    Form edit
     *********************************************************************//*


    private fun onClickExerciseName(){
        if(!viewModel.isEditingName()){
            viewModel.setInteractionNameState(ExerciseInteractionState.EditState())
            updateIsActiveInViewModel()
            updateBodyPartInViewModel()
            updateExerciseTypeInViewModel()
        }
    }

    private fun onClickIsActive(isChecked : Boolean){
        updateNameInViewModel()
        updateBodyPartInViewModel()
        updateExerciseTypeInViewModel()
        setExerciseIsActive(isChecked)
        viewModel.setInteractionIsActiveState(ExerciseInteractionState.EditState())
        updateIsActiveInViewModel()
    }

    private fun onClickWorkoutType(){
        if(!viewModel.isEditingWorkoutType()){
            updateNameInViewModel()
            updateIsActiveInViewModel()
            updateBodyPartInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionWorkoutTypeState(ExerciseInteractionState.EditState())
        }
    }

    private fun onClickBodyPart(){
        if(!viewModel.isEditingBodyPart()){
            updateNameInViewModel()
            updateIsActiveInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionBodyPartState(ExerciseInteractionState.EditState())
        }
    }

    private fun onClickExerciseType(){
        if(!viewModel.isEditingExerciseType()){
            updateNameInViewModel()
            updateIsActiveInViewModel()
            updateBodyPartInViewModel()
            viewModel.setInteractionExerciseTypeState(ExerciseInteractionState.EditState())
        }
    }

    private fun updateNameInViewModel(){
        if(viewModel.isEditingName()){
            viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseName(getExerciseName()))
        }
    }

    private fun updateIsActiveInViewModel(){
        if(viewModel.isEditingIsActive()){
            viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseIsActive(getExerciseIsActive()))
        }
    }

    private fun updateBodyPartInViewModel(){
        if(viewModel.isEditingBodyPart()){
            getExerciseBodyPart()?.let {
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseBodyPart(it))
            }
        }
    }

    private fun updateExerciseTypeInViewModel(){
        if(viewModel.isEditingExerciseType()){
            viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseExerciseType(getExerciseType()))
        }
    }

    private fun getExerciseName() : String {
        return binding?.exerciseDetailTextName?.editText?.text.toString()
    }

    private fun getExerciseIsActive() : Boolean {
        return binding?.exerciseDetailSwitchIsactive?.isChecked ?: false
    }

    private fun setExerciseIsActive(isActive : Boolean) {
        binding?.exerciseDetailSwitchIsactive?.isChecked = isActive
    }

    private fun getExerciseBodyPart() : BodyPart? {
        return viewModel.state.value?.exercise?.bodyPart
    }

    private fun getExerciseType() : ExerciseType {
        return viewModel.state.value?.exercise?.exerciseType ?: ExerciseType.REP_EXERCISE
    }

    private fun quitEditState(){
        updateNameInViewModel()
        updateIsActiveInViewModel()
        updateBodyPartInViewModel()
        updateExerciseTypeInViewModel()
        viewModel.exitExerciseEditState()
    }

    */
/********************************************************************
        Adapter click
     *********************************************************************//*


    override fun onEditClick(item: ExerciseSet) {
        //Set destination set
        quitEditState()
        val bundle = bundleOf(
            "exerciseSet" to item,
            "exerciseType" to viewModel.state.value?.exercise?.exerciseType)
        findNavController().navigate(R.id.action_exercise_detail_fragment_to_exercise_set_detail_fragment,bundle)
    }

    override fun onDeleteClick(item: ExerciseSet) {
        viewModel.onTriggerEvent(ExerciseDetailEvents.RemoveSet(item))
    }

    override fun updateOrder(item: ExerciseSet, position: Int) {
    }


    */
/********************************************************************
    Dialog view
     *********************************************************************//*


    private fun exitDialog(){

        val message = GenericMessageInfo.Builder()
            .id("ExerciseSetDetailFragment.ExitDialog")
            .title(EXERCISE_DETAIL_EXIT_DIALOG_TITLE)
            .description(EXERCISE_DETAIL_EXIT_DIALOG_CONTENT)
            .messageType(MessageType.Info)
            .uiComponentType(UIComponentType.Dialog)
            .positive(
                PositiveAction(
                    positiveBtnTxt = "OK",
                    onPositiveAction = {
                        exit()
                    }
                )
            )
            .negative(
                NegativeAction(
                    negativeBtnTxt = "Cancel",
                    onNegativeAction = {}
                )
            )

        launchDialog(message)
    }

    private fun launchDialog( message : GenericMessageInfo.Builder){
        viewModel.onTriggerEvent(ExerciseDetailEvents.LaunchDialog(message))
    }

    */
/********************************************************************
    BACK BUTTON PRESS
     *********************************************************************//*


    private fun onBackPressed() {
        if (viewModel.checkExerciseEditState()) {
            quitEditState()
        }
        viewModel.state.value?.let { state ->
            if(state.isUpdatePending){
                exitDialog()
            }
            else {
                exit()
            }
        }
    }

    private fun exit(){
        viewModel.state.value?.let { state ->
            if(state.isUpdateDone){
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    SHOULD_REFRESH,
                    true
                )
            }
            findNavController().popBackStack()
        }
    }
    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}*/
