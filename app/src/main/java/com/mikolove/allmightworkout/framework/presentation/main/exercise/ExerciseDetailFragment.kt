package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.card.MaterialCardView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.exercise.UpdateExercise.Companion.UPDATE_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentExerciseDetailBinding
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.TopSpacingItemDecoration
import com.mikolove.allmightworkout.framework.presentation.common.hideKeyboard
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseInteractionState.*
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
    private var workoutTypeSelectedPosition : Int? = null
    private var bodyPartPosition : Int? = null
    private var exerciseTypePosition : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onResume() {
        super.onResume()
        loadDetailWorkoutTypes(viewModel.getWorkoutTypes())
        loadDetailBodyParts(null)
        loadDetailExerciseTypes(ExerciseType.values().toCollection(ArrayList()))

        loadExercise()
        loadCachedExercise()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseDetailBinding.bind(view)

        setupAdapters()
        setupButtonAction()
        subscribeObservers()
    }

    override fun onDestroyView() {

        viewModel.setExerciseSelected(null)
        viewModel.setWorkoutTypeSelected(null)

        viewModel.setDetailWorkoutTypes(null)
        viewModel.setDetailBodyPart(null)
        viewModel.setDetailExerciseTypes(null)
        viewModel.resetDetailExhausted()

        exerciseSetAdapter = null
        workoutTypeAdapter = null
        bodyPartAdapter = null
        exerciseTypeAdapter = null
        binding = null

        super.onDestroyView()
    }

    /*
        Observers
     */

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner,{ viewState ->

            if(viewState != null){

                viewState.detailWorkoutTypes?.let {
                    if(!viewModel.getDetailWorkoutTypesExhausted()){
                        printLogD("ExerciseDetailFragment","load workoutTypes")
                        workoutTypeAdapter?.clear()
                        workoutTypeAdapter?.addAll(it)
                        viewModel.setDetailWorkoutTypesExhausted(true)
                    }
                }

                viewState.detailBodyParts?.let {
                    if(!viewModel.getDetailBodyPartExhausted()){
                        printLogD("ExerciseDetailFragment","load bodyParts ${it}")
                        bodyPartAdapter?.clear()
                        bodyPartAdapter?.addAll(it)
                        enableBodyParts(true)
                        viewModel.setDetailBodyPartsExhausted(true)
                    }
                }

                viewState.detailExerciseType?.let {
                    if(!viewModel.getDetailExerciseTypesExhausted()){
                        printLogD("ExerciseDetailFragment","load exerciseTypes")
                        exerciseTypeAdapter?.clear()
                        exerciseTypeAdapter?.addAll(it)
                        viewModel.setDetailExerciseTypesExhausted(true)
                    }
                }

                viewState.reloadBodyParts?.let { reload ->
             /*       if(reload){
                        getWorkoutTypeSelected()?.bodyParts?.let {
                            bodyPartPosition = null
                            bodyPartAdapter?.clear()
                            bodyPartAdapter?.addAll(it)
                            enableBodyParts(true)
                        }?:enableBodyParts(false)
                        viewModel.setReloadBodyParts(false)
                    }*/
                }

                viewState.workoutTypeSelected?.let { workoutType ->
                    printLogD("ExerciseDetailFragment","WorkoutTypeSelected ${workoutType}")
                    //setWorkoutTypeSelected(workoutType.name)
                }

                viewState.exerciseSelected?.let {
                    viewModel.setExerciseTypeState(it.exerciseType)
                    printLogD("ExerciseDetailFragment","${it}")
                    setExerciseName(it.name)
                    setExerciseIsActive(it.isActive)
                   /* setWorkoutTypeSelected("")
                    setExerciseBodyPart("")
                    setExerciseType("")*/
                    //setExerciseUI()
                    exerciseSetAdapter?.submitList(it.sets)
                }

                viewState.cachedExerciseSetsByIdExercise?.let {
                    printLogD("ExerciseDetailFragment","Cached exercise size ${it.size}")
                }
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when(response.message){

                     UPDATE_EXERCISE_SUCCESS ->{

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
                }
                is EditState ->{

                }
            }
        })
    }

    /*
        setup
     */

    private fun loadExercise(){
        if(!viewModel.isExistExercise()){
            val exercise = viewModel.createExercise()
            val sets : ArrayList<ExerciseSet> = ArrayList()
            repeat(1){
                sets.add(viewModel.createExerciseSet())
            }
            //viewModel.updateExerciseSets(sets)
            exercise.sets = sets
            viewModel.setExerciseSelected(exercise)

        }else{

            val workoutType = viewModel.getExerciseSelectedWorkoutType()
            val bodyPart = viewModel.getExerciseSelected()?.bodyPart
            val exerciseType = viewModel.getExerciseSelected()?.exerciseType

            loadDetailBodyParts(ArrayList(workoutType?.bodyParts))
            //viewModel.setWorkoutTypeSelected(workoutType)

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
                addExercise()
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

    private fun addExercise(){
        viewModel.addExercise()
    }

    private fun addSet(){
        viewModel.addSet()
    }

    private fun reloadBodyParts(){
        viewModel.updateExerciseBodyPart(null)
        viewModel.setReloadBodyParts(true)
    }
    private fun setupAdapters(){

        //WorkoutTypeAdapter
        val types = arrayListOf<WorkoutType>()
        workoutTypeAdapter = MaterialArrayAdapter(requireContext(),R.layout.item_workout_type,types)
        //viewModel.getWorkoutTypes()?.let { workoutTypeAdapter?.addAll(it) }
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setAdapter(workoutTypeAdapter)
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            setWorkoutTypeSelectedPosition(position)
            printLogD("ExerciseDetailFragment","Workout Type selected ${getWorkoutTypeSelectedPosition()} - ${getWorkoutTypeSelected()}")
            (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).text = null
            getWorkoutTypeSelected()?.let {
                printLogD("ExerciseDetailFragment","SEND BODY PARTS ${it.bodyParts}")
                loadDetailBodyParts(ArrayList(it.bodyParts))
            }
            /*getWorkoutTypeSelected()?.let {
                updateWorkoutTypeInViewModel()
                reloadBodyParts()
            }*/
        }
        
        //WorkoutTypeAdapter
        val bodyParts = arrayListOf<BodyPart>()
        bodyPartAdapter = MaterialArrayAdapter(requireContext(),R.layout.item_body_part,bodyParts)
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setAdapter(bodyPartAdapter)
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            bodyPartPosition = position
        }

        //WorkoutTypeAdapter
        val exerciseTypes = arrayListOf<ExerciseType>()
        //val exerciseTypes = ExerciseType.values().toCollection(ArrayList())
        exerciseTypeAdapter = MaterialArrayAdapter(requireContext(),R.layout.item_exercise_type,exerciseTypes)
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setAdapter(exerciseTypeAdapter)
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            exerciseTypePosition = position
        }

        binding?.exerciseDetailRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            exerciseSetAdapter = ExerciseSetListAdapter(
                this@ExerciseDetailFragment,
                viewLifecycleOwner,
                viewModel.exerciseTypeState,
                dateUtil
            )

            adapter = exerciseSetAdapter
        }
    }

    private fun showToastInsertedExercises(){

/*        uiController.onResponseReceived(
            response = Response(
                message = ,
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
        )*/
    }


    /*
        Form click
     */

    private fun onClickExerciseName(){
        if(!viewModel.isEditingName()){
            updateIsActiveInViewModel()
            updateWorkoutTypeInViewModel()
            updateBodyPartInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionNameState(EditState())
        }
    }

    private fun onClickIsActive(){
        if(!viewModel.isEditingIsActive()){
            updateNameInViewModel()
            updateWorkoutTypeInViewModel()
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
            updateWorkoutTypeInViewModel()
            updateExerciseTypeInViewModel()
            viewModel.setInteractionBodyPartState(EditState())
        }
    }

    private fun onClickExerciseType(){
        if(!viewModel.isEditingExerciseType()){
            updateNameInViewModel()
            updateIsActiveInViewModel()
            updateWorkoutTypeInViewModel()
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

    private fun updateWorkoutTypeInViewModel(){
        if(viewModel.isEditingWorkoutType()){
            viewModel.setWorkoutTypeSelected(getWorkoutTypeSelected())
        }
    }

    private fun updateBodyPartInViewModel(){
        if(viewModel.isEditingBodyPart()){
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
        return binding?.exerciseDetailSwitchIsactive?.let {
            it.isChecked
        }?: false
    }

    private fun setExerciseIsActive(isActive : Boolean) {
        binding?.exerciseDetailSwitchIsactive?.isChecked = isActive
    }

    private fun getWorkoutTypeSelected() : WorkoutType? {
        return getWorkoutTypeSelectedPosition()?.let {
            workoutTypeAdapter?.getItem(it)
        }
    }

    private fun getWorkoutTypeSelectedPosition() : Int? = workoutTypeSelectedPosition

    private fun setWorkoutTypeSelectedPosition(position : Int){
        workoutTypeSelectedPosition = position
    }

    private fun setWorkoutTypeSelected(name :String?) {
        getWorkoutTypeSelectedPosition()?.let {
            printLogD("ExerciseDetailFragment","wType setListSelection")
            (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setSelection(it)
        }
    }

    private fun getExerciseBodyPart() : BodyPart? {
        return getBodyPartPosition()?.let {
            bodyPartAdapter?.getItem(it)
        }
    }
    private fun getBodyPartPosition() : Int? = bodyPartPosition

    private fun setExerciseBodyPart(name : String?) {
        bodyPartPosition?.let {
            (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setSelection(it)
        }
    }

    private fun getExerciseType() : ExerciseType {
        return getExerciseTypePosition()?.let {
            exerciseTypeAdapter?.getItem(it)
        } ?: ExerciseType.REP_EXERCISE
    }

    private fun getExerciseTypePosition() : Int? = exerciseTypePosition

    private fun setExerciseType(name : String?) {
        exerciseTypePosition?.let {
            printLogD("ExerciseDetailFragment","eType setListSelection")

            (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setSelection(it)
        }
    }


    /*
        Exercise Set Interactions
     */
    override fun onEditClick(rootView : MaterialCardView,expandableView: View) {

        TransitionManager.beginDelayedTransition(rootView,AutoTransition())
        if(expandableView.visibility == View.GONE){
            expandableView.visibility = View.VISIBLE
        }else{
            expandableView.visibility = View.GONE
        }
    }

    override fun onDeleteClick(item: ExerciseSet) {

        viewModel.removeSet(item)
    }

    override fun isEditModeEnabled(): Boolean {
        return false
    }

    override fun activateEditMode() {

    }

    override fun setupFAB() {

    }

    override fun fabOnClick() {

    }
}