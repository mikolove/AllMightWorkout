package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

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
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise_list.ExerciseListEvents
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


    /********************************************************************
    MENU INTERACTIONS
     *********************************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercise_detail, menu)

        /*      val menuUpdate = menu.findItem(R.id.toolbar_exercise_detail_update)
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
              }*/

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.toolbar_exercise_detail_add -> {
                viewModel.onTriggerEvent(ExerciseDetailEvents.InsertExercise)
                true
            }
            R.id.toolbar_exercise_detail_update -> {
                updateExercise()
                true
            }
            R.id.toolbar_exercise_detail_delete -> {
                //deleteExercise()
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
                        printLogD("ExerciseDetailFragment","Body parts ${state.bodyParts}")
                        submitList(state.bodyParts)
                        enableBodyParts(true)
                    }
                }
            }else{
                enableBodyParts(false)
            }

            if(state.exerciseTypes.isNotEmpty()){
                exerciseTypeAdapter?.apply {
                    if(!getItems().containsAll(state.exerciseTypes)){
                        printLogD("ExerciseDetailFragment","ExerciseTypes ${state.exerciseTypes}")
                        submitList(state.exerciseTypes)
                    }
                }

            }
            state.exercise?.sets?.let { sets ->
                exerciseSetAdapter?.apply {
                    submitList(list = sets.sortedBy { it.order })
                }
            }

            printLogD("ExerciseDetailFragment","Exercise state ${state.exercise?.name} ${state.exercise?.bodyPart} ${state.exercise?.exerciseType} ${state.exercise?.isActive}")


        })
        /*      viewModel.viewState.observe(viewLifecycleOwner,{ viewState ->

                  if(viewState != null){

                      viewState.detailWorkoutTypes?.let {
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
                              exerciseTypeAdapter?.clear()
                              exerciseTypeAdapter?.addAll(it)
                              viewModel.setDetailExerciseTypesExhausted(true)
                          }
                      }

                      viewState.exerciseSelected?.let {

                          //Update name and is Active
                          setExerciseName(it.name)
                          setExerciseIsActive(it.isActive)

                          //Update LiveData for list
                          viewModel.setExerciseTypeState(it.exerciseType)

                          //Update sets
                          val sortedList = it.sets.sortedBy { it.order }
                          exerciseSetAdapter?.submitList(sortedList)
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

                          ADD_EXERCISE_SETS_SUCCESS -> {
                              uiController.onResponseReceived(
                                  response = stateMessage.response,
                                  stateMessageCallback = object: StateMessageCallback {
                                      override fun removeMessageFromStack() {
                                          viewModel.clearStateMessage()
                                      }
                                  }
                              )
                              loadCachedExerciseSets()
                          }

                          ADD_EXERCISE_SETS_ERRORS -> {
                              uiController.onResponseReceived(
                                  response = stateMessage.response,
                                  stateMessageCallback = object: StateMessageCallback {
                                      override fun removeMessageFromStack() {
                                          viewModel.clearStateMessage()
                                      }
                                  }
                              )
                              loadCachedExerciseSets()
                          }

                          UPDATE_EXERCISE_SETS_SUCCESS -> {
                              uiController.onResponseReceived(
                                  response = stateMessage.response,
                                  stateMessageCallback = object: StateMessageCallback {
                                      override fun removeMessageFromStack() {
                                          viewModel.clearStateMessage()
                                      }
                                  }
                              )
                              loadCachedExerciseSets()
                          }

                          UPDATE_EXERCISE_SETS_ERRORS -> {
                              uiController.onResponseReceived(
                                  response = stateMessage.response,
                                  stateMessageCallback = object: StateMessageCallback {
                                      override fun removeMessageFromStack() {
                                          viewModel.clearStateMessage()
                                      }
                                  }
                              )
                              loadCachedExerciseSets()
                          }

                          DELETE_EXERCISE_SETS_SUCCESS -> {

                              uiController.onResponseReceived(
                                  response = stateMessage.response,
                                  stateMessageCallback = object: StateMessageCallback {
                                      override fun removeMessageFromStack() {
                                          viewModel.clearStateMessage()
                                      }
                                  }
                              )
                              loadCachedExerciseSets()
                          }

                          DELETE_EXERCISE_SETS_ERRORS -> {
                              uiController.onResponseReceived(
                                  response = stateMessage.response,
                                  stateMessageCallback = object: StateMessageCallback {
                                      override fun removeMessageFromStack() {
                                          viewModel.clearStateMessage()
                                      }
                                  }
                              )
                              loadCachedExerciseSets()
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
              })*/

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

    /*
        setup
     */

    private fun loadInitialValues(){
        viewModel.state.value?.exercise?.let {

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

    private fun loadCachedExerciseSets(){
        /* val idExercise = viewModel.getExerciseSelected()?.idExercise ?: ""
         viewModel.loadCachedExerciseSets(idExercise)*/
    }

    private fun loadDetailWorkoutTypes(list : ArrayList<WorkoutType>?){
        /*     viewModel.setDetailWorkoutTypes(list)*/
    }

    private fun loadDetailBodyParts(list : ArrayList<BodyPart>?){
/*        viewModel.setDetailBodyPart(list)
        viewModel.setDetailBodyPartsExhausted(false)*/
    }
    private fun loadDetailExerciseTypes(list : ArrayList<ExerciseType>?){
/*        viewModel.setDetailExerciseTypes(list)*/
    }

    private fun enableBodyParts(isEnable : Boolean){
        binding?.exerciseDetailBodypart?.isEnabled = isEnable
    }

    private fun setupButtonAction(){

/*        if(!viewModel.isExistExercise()){
            binding?.excerciseDetailButtonCreate?.setOnClickListener {
                insertExercise()
            }
        }*/

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

    private fun insertExercise(){
        /*  if(viewModel.checkExerciseEditState()){
              quitEditState()
          }
          viewModel.insertExercise()*/
    }

    private fun updateExercise(){
        /*  if(viewModel.isExistExercise()){
              quitEditState()
          }

          //Update exercise
          viewModel.updateExercise()

          //Update sets
          viewModel.updateExerciseSets()*/

    }

    private fun insertSets(){
        /*   if(viewModel.isExistExercise()){
               viewModel.insertSets()
           }*/
    }

    private fun addSet(){
        /*viewModel.addSet()
        if(viewModel.isExistExercise()){
            setUpdatePending()
        }*/
    }

    private fun removeSet(item : ExerciseSet){
        /*  viewModel.removeSet(item)
          setUpdatePending()*/
    }

    override fun updateOrder(item: ExerciseSet, position: Int) {
    }

    private fun setUpdatePending(){
        /* if(viewModel.isExistExercise() && !viewModel.getIsUpdatePending()){
             viewModel.setIsUpdatePending(true)
         }*/
    }
    private fun setupAdapters(){

        //WorkoutTypeAdapter
        workoutTypeAdapter = WorkoutTypeAdapter(requireContext(), R.layout.item_workout_type, mutableListOf())
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setAdapter(workoutTypeAdapter)
        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _  ->
            workoutTypeAdapter?.getItem(position)?.idWorkoutType?.let {
                viewModel.onTriggerEvent(ExerciseDetailEvents.GetBodyParts(it))
                viewModel.onTriggerEvent(ExerciseDetailEvents.UpdateExerciseBodyPart(null))
                setExerciseBodyPart("")
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


    /*
        Form click
     */

    private fun onClickExerciseName(){
        if(!viewModel.isEditingName()){
            viewModel.setInteractionNameState(ExerciseInteractionState.EditState())
            updateIsActiveInViewModel()
            updateBodyPartInViewModel()
            updateExerciseTypeInViewModel()
        }
    }

    private fun onClickIsActive(isChecked : Boolean){
        //if(!viewModel.isEditingIsActive()){
        updateNameInViewModel()
        updateBodyPartInViewModel()
        updateExerciseTypeInViewModel()
        setExerciseIsActive(isChecked)
        viewModel.setInteractionIsActiveState(ExerciseInteractionState.EditState())
        updateIsActiveInViewModel()
        //}
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

    private fun setExerciseName(name : String){
        binding?.exerciseDetailTextName?.editText?.setText(name)
    }

    private fun getExerciseIsActive() : Boolean {
        return binding?.exerciseDetailSwitchIsactive?.isChecked ?: false
    }

    private fun setExerciseIsActive(isActive : Boolean) {
        binding?.exerciseDetailSwitchIsactive?.isChecked = isActive
    }
    private fun setExerciseBodyPart(name :String){
        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).setText(name.capitalize(),false)
    }

    private fun getWorkoutTypeSelected() : WorkoutType? {
        return viewModel.getExerciseWorkoutType()
    }

    private fun getExerciseBodyPart() : BodyPart? {
        return viewModel.state.value?.exercise?.bodyPart
    }

    private fun clearExerciseBodyPart(){
        //(binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView).text = null
    }

    private fun getExerciseType() : ExerciseType {
        return viewModel.state.value?.exercise?.exerciseType ?: ExerciseType.REP_EXERCISE
    }

    /*
        Array Adapter position used to update item on state change

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
    } */

    /*
        Exercise Set Interactions
    */

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

    private fun launchDialog( message : GenericMessageInfo.Builder){
        viewModel.onTriggerEvent(ExerciseDetailEvents.LaunchDialog(message))
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
        /*if (viewModel.checkExerciseEditState()) {
            quitEditState()
        }else{

            viewModel.setIsUpdatePending(false)
            viewModel.setExerciseSelected(null)
            viewModel.setExerciseSetSelected(null)
            viewModel.clearExerciseTypeState()

//            viewModel.setDetailWorkoutTypes(null)
//            viewModel.setDetailBodyPart(null)
//            viewModel.setDetailExerciseTypes(null)
//            viewModel.resetDetailExhausted()

            findNavController().popBackStack()
        }*/
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