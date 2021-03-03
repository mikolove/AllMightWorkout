package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.google.android.material.card.MaterialCardView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.Response
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.exercise.InsertExercise.Companion.INSERT_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.UpdateExercise.Companion.UPDATE_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentExerciseDetailBinding
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.TopSpacingItemDecoration
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseInteractionState
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseInteractionState.*
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Body
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

                viewState.workoutTypeSelected?.let { workoutType ->

                    workoutType.bodyParts?.let { bodyParts ->
                        printLogD("ExerciseDetailFragment","Reloading bodyParts")
                        bodyPartAdapter?.clear()
                        bodyPartAdapter?.addAll(bodyParts)
                        enableBodyParts(true)
                    }?:enableBodyParts(false)
                }

                viewState.exerciseSelected?.let {
                    viewModel.setExerciseTypeState(it.exerciseType)
                    printLogD("ExerciseDetailFragment","${it.sets}")
                    setExerciseUI()
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
    }

    /*
        setup
     */


    private fun setExerciseUI(){

        val exercise = viewModel.getExerciseSelected()
        val workoutType = viewModel.getWorkoutTypeSelected()
        val bodyPart = viewModel.getExerciseSelected()?.bodyPart
        val exerciseType = viewModel.getExerciseSelected()?.exerciseType
        val sets = viewModel.getExerciseSelected()?.sets ?: ArrayList()

        binding?.exerciseDetailTextName?.editText?.setText(exercise?.name)

        workoutType?.let {
            (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setText(workoutType.name,false)
        }

        if(workoutType?.bodyParts?.contains(bodyPart) == false) {
            (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setText(null)
        }else {
            bodyPart?.let { bp ->
                (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setText(bp.name,false)
            }
        }

        exerciseType?.let { et ->
            (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setText(et.toString(),false)
        }
    }

    private fun loadExercise(){
        if(!viewModel.isExistExercise()){
            val exercise = viewModel.createExercise()
            val sets : ArrayList<ExerciseSet> = ArrayList()
            repeat(3){
                sets.add(viewModel.createExerciseSet())
            }
            //viewModel.updateExerciseSets(sets)
            exercise.sets = sets
            viewModel.setExerciseSelected(exercise)
        }
    }

    private fun loadCachedExercise(){
        val idExercise = viewModel.getExerciseSelected()?.idExercise ?: ""
        viewModel.loadCachedExerciseSets(idExercise)
    }

    private fun loadWorkoutType(){
        viewModel.loadWorkoutTypes()
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
                    printLogD("ExerciseDetailFragment","focus on")
                }else {
                    printLogD("ExerciseDetailFragment","focus off")
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

    private fun setupAdapters(){

        //WorkoutTypeAdapter
        val types = arrayListOf<WorkoutType>()
        workoutTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_workout_type,
            types)

        viewModel.getWorkoutTypes()?.let { workoutTypeAdapter?.addAll(it) }
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setAdapter(workoutTypeAdapter)
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            val workoutType = workoutTypeAdapter?.getItem(position)
            viewModel.updateExerciseBodyPart(null)
            viewModel.setWorkoutTypeSelected(workoutType)
        }
        
        //WorkoutTypeAdapter
        val bodyParts = arrayListOf<BodyPart>()
        bodyPartAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_body_part,
            bodyParts)

        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setAdapter(bodyPartAdapter)
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            val bodyPart = bodyPartAdapter?.getItem(position)
            viewModel.updateExerciseBodyPart(bodyPart)
        }

        //WorkoutTypeAdapter
        val exerciseTypes =  ExerciseType.values()
        exerciseTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_exercise_type,
            exerciseTypes)
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setAdapter(exerciseTypeAdapter)
        (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
            val exerciseType = exerciseTypeAdapter?.getItem(position)
            exerciseType?.let { viewModel.updateExerciseExerciseType(it) }
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

    private fun onClickExercisePart(){
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

    private fun getExerciseIsActive() : Boolean {
        return binding?.exerciseDetailSwitchIsactive?.let {
            it.isChecked
        }?: false
    }

    private fun getWorkoutTypeSelected() : WorkoutType? {
        val position = (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).listSelection
        return workoutTypeAdapter?.getItem(position)
    }

    private fun getExerciseBodyPart() : BodyPart? {
        val position = (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).listSelection
        return bodyPartAdapter?.getItem(position)
    }

    private fun getExerciseType() : ExerciseType {
        val position = (binding?.exerciseDetailExercisetype?.editText as AutoCompleteTextView).listSelection
        return exerciseTypeAdapter?.getItem(position) ?: ExerciseType.REP_EXERCISE
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