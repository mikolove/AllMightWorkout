package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.exercise.InsertExercise.Companion.INSERT_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveExercise
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveExercise.Companion.DELETE_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.exercise.UpdateExercise.Companion.UPDATE_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveWorkout
import com.mikolove.allmightworkout.databinding.FragmentExerciseDetailBinding
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.TopSpacingItemDecoration
import com.mikolove.allmightworkout.framework.presentation.common.hideKeyboard
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseInteractionState.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutStateEvent
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseDetailFragment():
    BaseFragment(R.layout.fragment_exercise_detail),
    ExerciseSetListAdapter.Interaction,
    FabController{

    val viewModel : ExerciseViewModel by activityViewModels()

    @Inject
    lateinit var dateUtil : DateUtil

    private var binding : FragmentExerciseDetailBinding? = null
    private var exerciseSetAdapter : ExerciseSetListAdapter? = null
    private var workoutTypeAdapter : ArrayAdapter<WorkoutType>? = null
    private var bodyPartAdapter : ArrayAdapter<BodyPart>? = null
    private var exerciseTypeAdapter : ArrayAdapter<ExerciseType>? = null

    /*
        AutoComplete textview with ArrayAdapter is shit
     */
    private var workoutTypePosition : Int? = null
    private var bodyPartPosition : Int? = null
    private var exerciseTypePosition : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseDetailBinding.bind(view)

        viewModel.resetDetailExhausted()
        setupAdapters()
        setupButtonAction()
        setupOnBackPressDispatcher()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()

        loadDetailWorkoutTypes(viewModel.getWorkoutTypes())
        loadDetailExerciseTypes(ExerciseType.values().toCollection(ArrayList()))
        loadCachedExercise()
        init()
    }

    override fun onDestroyView() {

        exerciseSetAdapter = null
        workoutTypeAdapter = null
        bodyPartAdapter = null
        exerciseTypeAdapter = null
        binding = null

        super.onDestroyView()
    }


    /********************************************************************
    MENU INTERACTIONS
     *********************************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercise_detail, menu)

        val menuUpdate = menu.findItem(R.id.toolbar_exercise_detail_update)
        val menuDelete = menu.findItem(R.id.toolbar_exercise_detail_delete)
        val menuCreate = menu.findItem(R.id.toolbar_exercise_detail_add)

        if(!viewModel.isExistExercise()){
            setMenuVisibility(menuUpdate,false)
            setMenuVisibility(menuDelete,false)
            setMenuVisibility(menuCreate,true)
        }else{
            setMenuVisibility(menuUpdate,viewModel.getIsUpdatePending())
            setMenuVisibility(menuDelete,true)
            setMenuVisibility(menuCreate,false)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.toolbar_exercise_detail_add -> {
                insertExercise()
                true
            }
            R.id.toolbar_exercise_detail_update -> {
                updateExercise()
                true
            }
            R.id.toolbar_exercise_detail_delete -> {
                deleteExercise()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setMenuVisibility(menuItem: MenuItem, isVisible : Boolean){
        menuItem.setVisible(isVisible)
    }


    /*
        Observers
     */

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner,{ viewState ->

            if(viewState != null){

                viewState.detailWorkoutTypes?.let {
                    printLogD("ExerciseDetailFragment","Load workoutTypes ${viewModel.getDetailWorkoutTypesExhausted()}")
                    if(!viewModel.getDetailWorkoutTypesExhausted()){
                        workoutTypeAdapter?.clear()
                        workoutTypeAdapter?.addAll(it)
                        viewModel.setDetailWorkoutTypesExhausted(true)
                    }
                }

                viewState.detailBodyParts?.let {
                    if(!viewModel.getDetailBodyPartExhausted()){
                        bodyPartAdapter?.clear()
                        bodyPartAdapter?.addAll(it)
                        enableBodyParts(true)
                        viewModel.setDetailBodyPartsExhausted(true)
                    }
                }

                viewState.detailExerciseType?.let {
                    if(!viewModel.getDetailExerciseTypesExhausted()){
                        printLogD("ExerciseDetailFragment","Load exerciseTypes ${viewModel.getDetailExerciseTypesExhausted()}")
                        exerciseTypeAdapter?.clear()
                        exerciseTypeAdapter?.addAll(it)
                        viewModel.setDetailExerciseTypesExhausted(true)
                    }
                }

                viewState.exerciseSelected?.let {
                    printLogD("ExerciseDetailFragment","${it.name} / ${it.bodyPart} / ${it.exerciseType} / ${it.isActive}")

                    //Update name and is Active
                    setExerciseName(it.name)
                    setExerciseIsActive(it.isActive)

                    //Update LiveData for list
                    viewModel.setExerciseTypeState(it.exerciseType)

                    //Update sets
                    exerciseSetAdapter?.submitList(it.sets)
                }

            }

            viewState.isUpdatePending?.let { isUpdatePending ->
                activity?.invalidateOptionsMenu()
            }

        })


        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when(response.message){

                    INSERT_EXERCISE_SUCCESS -> {

                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object : StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            })

                        insertSets()
                        activity?.invalidateOptionsMenu()
                    }

                    UPDATE_EXERCISE_SUCCESS ->{
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object : StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            })
                        viewModel.setIsUpdatePending(false)
                        activity?.invalidateOptionsMenu()
                    }

                    DELETE_EXERCISE_SUCCESS -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                        findNavController().popBackStack()
                    }

                    else -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object : StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            })
                    }
                }
            }
        })

        viewModel.exerciseNameInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is DefaultState ->{
                    view?.hideKeyboard()
                    binding?.exerciseDetailTextName?.clearFocus()
                }
                is EditState ->{
                    setUpdatePending()
                }
            }
        })

        viewModel.exerciseWorkoutTypeInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is DefaultState ->{
                    binding?.exerciseDetailWorkouttype?.clearFocus()
                }
                is EditState ->{
                    setUpdatePending()
                }
            }
        })

        viewModel.exerciseBodyPartInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is DefaultState ->{
                    binding?.exerciseDetailBodypart?.clearFocus()
                }
                is EditState ->{
                    setUpdatePending()
                }
            }
        })

        viewModel.exerciseTypeInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){
                is DefaultState ->{
                    binding?.exerciseDetailExercisetype?.clearFocus()
                }
                is EditState ->{
                    setUpdatePending()
                }
            }
        })

    }

    /*
        setup
     */

    private fun init(){

        //Init from new exercise
        if(!viewModel.isExistExercise()){
            val exercise = viewModel.createExercise()
            val sets : ArrayList<ExerciseSet> = ArrayList()
            repeat(1){
                sets.add(viewModel.createExerciseSet())
            }
            exercise.sets = sets
            viewModel.setExerciseSelected(exercise)

        //Init from existing exercise
        }else{

            val workoutType = viewModel.getExerciseSelectedWorkoutType()
            val bodyPart = viewModel.getExerciseSelected()?.bodyPart
            val exerciseType = viewModel.getExerciseSelected()?.exerciseType

            loadDetailBodyParts(ArrayList(workoutType?.bodyParts))

            (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView)?.setText(workoutType?.name?.capitalize())
            (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView)?.setText(bodyPart?.name?.capitalize())
            (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView)?.setText(exerciseType.toString().capitalize())
        }
    }

    private fun loadCachedExercise(){
        val idExercise = viewModel.getExerciseSelected()?.idExercise ?: ""
        viewModel.loadCachedExerciseSets(idExercise)
    }

    private fun loadDetailWorkoutTypes(list : ArrayList<WorkoutType>?){
        viewModel.setDetailWorkoutTypes(list)
    }

    private fun loadDetailBodyParts(list : ArrayList<BodyPart>?){
        viewModel.setDetailBodyPart(list)
        viewModel.setDetailBodyPartsExhausted(false)
    }
    private fun loadDetailExerciseTypes(list : ArrayList<ExerciseType>?){
        viewModel.setDetailExerciseTypes(list)
    }

    private fun enableBodyParts(isEnable : Boolean){
        binding?.exerciseDetailBodypart?.isEnabled = isEnable
    }

    private fun setupButtonAction(){

        if(!viewModel.isExistExercise()){
            binding?.excerciseDetailButtonCreate?.setOnClickListener {
                insertExercise()
            }
        }
        binding?.exerciseDetailButtonAdd?.setOnClickListener {
            addSet()
        }

        binding?.exerciseDetailTextName?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickExerciseName()
                }
            }
        })

        binding?.exerciseDetailSwitchIsactive?.setOnClickListener {
            onClickIsActive()
        }

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

    private fun insertExercise(){
        if(viewModel.checkExerciseEditState()){
            quitEditState()
        }
        viewModel.insertExercise()
    }

    private fun updateExercise(){
        if(viewModel.isExistExercise()){
            quitEditState()
        }
        viewModel.updateExercise()
    }

    private fun insertSets(){
        if(viewModel.isExistExercise()){
            viewModel.insertSets()
        }
    }

    private fun addSet(){
        viewModel.addSet()
        if(viewModel.isExistExercise()){
            viewModel.setIsUpdatePending(true)
        }
    }

    private fun removeSet(item : ExerciseSet){
        viewModel.removeSet(item)
        setUpdatePending()
    }

    private fun setUpdatePending(){
        if(viewModel.isExistExercise()){
            viewModel.setIsUpdatePending(true)
        }
    }
    private fun setupAdapters(){

        printLogD("ExerciseDetailFragment","Setup Adapters")

        //WorkoutTypeAdapter
        val types = arrayListOf<WorkoutType>()
        workoutTypeAdapter = MaterialArrayAdapter(requireContext(),R.layout.item_workout_type,types)
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setAdapter(workoutTypeAdapter)
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            setWorkoutTypePosition(position)
            clearExerciseBodyPart()
            setBodyPartPosition(null)
            viewModel.updateExerciseBodyPart(null)
            getWorkoutTypeSelected()?.let {
                loadDetailBodyParts(ArrayList(it.bodyParts))
            }
        }
        
        //WorkoutTypeAdapter
        val bodyParts = arrayListOf<BodyPart>()
        bodyPartAdapter = MaterialArrayAdapter(requireContext(),R.layout.item_body_part,bodyParts)
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setAdapter(bodyPartAdapter)
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            setBodyPartPosition(position)
            updateBodyPartInViewModel()
        }

        //WorkoutTypeAdapter
        val exerciseTypes = arrayListOf<ExerciseType>()
        exerciseTypeAdapter = MaterialArrayAdapter(requireContext(),R.layout.item_exercise_type,exerciseTypes)
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setAdapter(exerciseTypeAdapter)
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            setExerciseTypePosition(position)
            updateExerciseTypeInViewModel()
        }

        binding?.exerciseDetailRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            exerciseSetAdapter = ExerciseSetListAdapter(
                this@ExerciseDetailFragment,
                viewLifecycleOwner,
                viewModel.exerciseTypeState
            )

            adapter = exerciseSetAdapter
        }
    }


    /*
        Form click
     */

    private fun onClickExerciseName(){
        if(!viewModel.isEditingName()){
            updateIsActiveInViewModel()
            updateBodyPartInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionNameState(EditState())
        }
    }

    private fun onClickIsActive(){
        if(!viewModel.isEditingIsActive()){
            updateNameInViewModel()
            updateBodyPartInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionIsActiveState(EditState())

            setExerciseIsActive(!getExerciseIsActive())
        }
    }

    private fun onClickWorkoutType(){
        if(!viewModel.isEditingWorkoutType()){
            updateNameInViewModel()
            updateIsActiveInViewModel()
            updateBodyPartInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionWorkoutTypeState(EditState())
        }
    }

    private fun onClickBodyPart(){
        if(!viewModel.isEditingBodyPart()){
            updateNameInViewModel()
            updateIsActiveInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionBodyPartState(EditState())
        }
    }

    private fun onClickExerciseType(){
        if(!viewModel.isEditingExerciseType()){
            updateNameInViewModel()
            updateIsActiveInViewModel()
            updateBodyPartInViewModel()
            viewModel.setInteractionExerciseTypeState(EditState())
        }
    }

    private fun updateNameInViewModel(){
        if(viewModel.isEditingName()){
            viewModel.updateExerciseName(getExerciseName())
        }
    }

    private fun updateIsActiveInViewModel(){
        if(viewModel.isEditingIsActive()){
            viewModel.updateExerciseIsActive(getExerciseIsActive())
        }
    }

    private fun updateBodyPartInViewModel(){
        if(viewModel.isEditingBodyPart()){
            printLogD("ExerciseDetailFragment","Bodypart ${getExerciseBodyPart()}")
            viewModel.updateExerciseBodyPart(getExerciseBodyPart())
        }
    }

    private fun updateExerciseTypeInViewModel(){
        if(viewModel.isEditingExerciseType()){
            viewModel.updateExerciseExerciseType(getExerciseType())
        }
    }

    private fun getExerciseName() : String {
        return binding?.exerciseDetailTextName?.editText?.text.toString()
    }

    private fun setExerciseName(name : String){
        binding?.exerciseDetailTextName?.editText?.setText(name)
    }

    private fun getExerciseIsActive() : Boolean {
        return binding?.exerciseDetailSwitchIsactive?.isChecked ?: false
    }

    private fun setExerciseIsActive(isActive : Boolean) {
        binding?.exerciseDetailSwitchIsactive?.isChecked = isActive
    }

    private fun getWorkoutTypeSelected() : WorkoutType? {
        return getWorkoutTypePosition()?.let {
            workoutTypeAdapter?.getItem(it)
        }
    }

    private fun getExerciseBodyPart() : BodyPart? {
        return getBodyPartPosition()?.let {
            bodyPartAdapter?.getItem(it)
        }
    }

    private fun clearExerciseBodyPart(){
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).text = null
    }

    private fun getExerciseType() : ExerciseType {
        return getExerciseTypePosition()?.let {
            exerciseTypeAdapter?.getItem(it)
        } ?: ExerciseType.REP_EXERCISE
    }

    /*
        Array Adapter position used to update item on state change
     */
    private fun getWorkoutTypePosition() : Int? = workoutTypePosition

    private fun setWorkoutTypePosition(position : Int?){
        workoutTypePosition = position
    }

    private fun getBodyPartPosition() : Int? = bodyPartPosition

    private fun setBodyPartPosition(position: Int?){
        bodyPartPosition = position
    }

    private fun getExerciseTypePosition() : Int? = exerciseTypePosition

    private fun setExerciseTypePosition(position: Int?) {
        exerciseTypePosition = position
    }

    /*
        Exercise Set Interactions
     */

    override fun onEditClick(item: ExerciseSet) {
        navigateToSet(item)
    }

    override fun onDeleteClick(item: ExerciseSet) {

        removeSet(item)

    }

    override fun setupFAB() {

    }

    override fun fabOnClick() {

    }


    private fun deleteExercise() {
        viewModel.setStateEvent(
            ExerciseStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = RemoveExercise.DELETE_EXERCISE_ARE_YOU_SURE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    viewModel.deleteExercise()
                                }

                                override fun cancel() {}
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )
    }

    /********************************************************************
        Navigate to set detail
     *********************************************************************/
    private fun navigateToSet(item: ExerciseSet){
        //Set destination set
        viewModel.setExerciseSetSelected(item)
        findNavController().navigate(R.id.action_exercise_detail_fragment_to_exercise_set_detail_fragment)
    }

    /********************************************************************
        BACK BUTTON PRESS
     *********************************************************************/

    private fun quitEditState(){
        updateNameInViewModel()
        updateIsActiveInViewModel()
        updateBodyPartInViewModel()
        updateExerciseTypeInViewModel()
        viewModel.exitExerciseEditState()
    }

    private fun onBackPressed() {
        printLogD("ExerciseDetailFragment","On Back Pressed")
        if (viewModel.checkExerciseEditState()) {
            quitEditState()
        }else{

            viewModel.setExerciseSelected(null)
            viewModel.setDetailWorkoutTypes(null)
            viewModel.setDetailBodyPart(null)
            viewModel.setDetailExerciseTypes(null)
            viewModel.resetDetailExhausted()

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

}