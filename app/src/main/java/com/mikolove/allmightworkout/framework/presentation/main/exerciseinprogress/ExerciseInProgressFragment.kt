package com.mikolove.allmightworkout.framework.presentation.main.exerciseinprogress

import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.FragmentExerciseInProgressBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.WIP_ARE_YOU_SURE_STOP_EXERCISE
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseInProgressFragment(): BaseFragment(R.layout.fragment_exercise_in_progress){

    val viewModel : ExerciseInProgressViewModel by viewModels()
    var binding : FragmentExerciseInProgressBinding? = null

    private var chronometer: Chronometer? = null
    private var countDownTimer : TextView? = null
    private var timer : CountDownTimer? = null

    @Inject
    lateinit var dateUtil: DateUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        closeChronometer()
        chronometer = null
        timer = null
        countDownTimer = null
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseInProgressBinding.bind(view)

        chronometer = binding?.eipChronometer
        countDownTimer = binding?.eipCountdowntimer

        setupOnBackPressDispatcher()
        subscribeObservers()

        binding?.eipButtonStartStop?.setOnClickListener {

            viewModel.state.value?.actualSet?.let { set ->
                if(viewModel.chronometerManager.isStartButtonActive()){

                    if(viewModel.chronometerManager.isIdleState()){
                        startSet(set)
                    }
                    else if(viewModel.chronometerManager.isRunningState()){
                        stopSet(set)
                    }
                    else if (viewModel.chronometerManager.isRestState()){
                        stopRest()
                    }
                }
            }
        }

        binding?.epiButtonReset?.setOnClickListener{
            viewModel.state.value?.actualSet?.let { set ->
                if(viewModel.chronometerManager.isRunningState()){
                    resetSet(set)
                }
            }
        }

        binding?.eipButtonEnd?.setOnClickListener {
            quitExercise()
        }


    }


    private fun quitExercise(){
        if(viewModel.chronometerManager.isEndButtonActive()){
            viewModel.state.value?.actualSet?.let { actualSet ->
                if( actualSet.order == 1 && actualSet.startedAt == null){
                    navigateBack()
                }else{
                    areYouSureToQuitExercise()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveAndNavigateback(){
        //saveExercise()
        setChronometerState(ChronometerState.CloseState())
        navigateBack()
    }

    private fun navigateBack(){
        viewModel.state.value?.exercise?.let { exercise ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                EXERCISE_UPDATED,
                exercise
            )
        }
        findNavController().popBackStack()
    }

    private fun subscribeObservers(){

        viewModel.state.observe(viewLifecycleOwner, { state ->

            state.isLoading?.let { uiController.displayProgressBar(it) }

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        viewModel.onTriggerEvent(ExerciseInProgressEvents.OnRemoveHeadFromQueue)
                    }
                })

            state.exercise?.let { exercise ->

                state.actualSet?.let { actualSet ->

                    updateUi(
                        order = actualSet.order,
                        totalSets = exercise.sets.size,
                        time = actualSet.time,
                        restTime = actualSet.restTime,
                        exerciseType = exercise.exerciseType,
                        reps = actualSet.reps,
                        weight = actualSet.weight
                    )
                }

            }


        })

        viewModel.chronometerState.observe(viewLifecycleOwner,  { chronometerState ->

            switchClock(chronometerState)

            printLogD("ExerciseInProgressFragment","chronometer state ${chronometerState}")

            when(chronometerState){

                is ChronometerState.StopState -> {
                    viewModel.state.value?.actualSet?.let { set ->
                        viewModel.state.value?.exercise?.let { exercise ->
                            if(exercise.sets.size >= set.order){
                                startRestChronometer(set.restTime)
                            }
                        }
                    }
                }
                is ChronometerState.SaveState -> {
                    viewModel.state.value?.actualSet?.let { set ->
                        viewModel.state.value?.exercise?.let { exercise ->
                            if(set.startedAt != null && set.endedAt != null){
                                printLogD("ExerciseInProgressFragment","actual set order ${set.order} - exercise size ${exercise.sets.size}")
                                //viewModel.saveSet(set)
                                //val nextSetExist = viewModel.loadNextSet(set)
                                viewModel.onTriggerEvent(ExerciseInProgressEvents.UpdateExerciseSet(set))
                                if(set.order == exercise.sets.size){
                                    saveAndNavigateback()
                                }else{
                                    printLogD("ExerciseInProgressFragment","LoadNextSet")
                                    viewModel.onTriggerEvent(ExerciseInProgressEvents.LoadNextSet)
                                    setChronometerState(ChronometerState.IdleState())
                                }

                            }
                        }
                    }
                }

                is ChronometerState.CloseState -> {
                    closeChronometer()
                }
                is ChronometerState.IdleState -> TODO()
                is ChronometerState.RestTimeState -> TODO()
                is ChronometerState.RunningState -> TODO()
            }
        })

        viewModel.chronometerManager.startButtonState.observe(viewLifecycleOwner, { state ->
            when(state){
                is ChronometerButtonState.ActiveButtonState -> {
                    binding?.eipButtonStartStop?.isEnabled = true

                }
                is ChronometerButtonState.PassiveButtonState ->{
                    binding?.eipButtonStartStop?.isEnabled = false
                }
                is ChronometerButtonState.RestButtonState -> TODO()
                is ChronometerButtonState.StartButtonState -> TODO()
                is ChronometerButtonState.StopButtonState -> TODO()
            }
        })

        viewModel.chronometerManager.resetButtonState.observe(viewLifecycleOwner, { state ->
            when(state){
                is ChronometerButtonState.ActiveButtonState -> {
                    binding?.epiButtonReset?.isEnabled = true

                }
                is ChronometerButtonState.PassiveButtonState ->{
                    binding?.epiButtonReset?.isEnabled = false
                }
                is ChronometerButtonState.RestButtonState -> TODO()
                is ChronometerButtonState.StartButtonState -> TODO()
                is ChronometerButtonState.StopButtonState -> TODO()
            }
        })

        viewModel.chronometerManager.endButtonState.observe(viewLifecycleOwner, { state ->
            when(state){
                is ChronometerButtonState.ActiveButtonState -> {
                    binding?.eipButtonEnd?.isEnabled = true

                }
                is ChronometerButtonState.PassiveButtonState ->{
                    binding?.eipButtonEnd?.isEnabled = false
                }
                is ChronometerButtonState.RestButtonState -> TODO()
                is ChronometerButtonState.StartButtonState -> TODO()
                is ChronometerButtonState.StopButtonState -> TODO()
            }
        })

    }

    private fun switchClock(state : ChronometerState){
        when(state){

            is ChronometerState.IdleState -> {
                binding?.eipButtonStartStop?.background = ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_play_circle_24,null)
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.RestTimeState -> {
                binding?.eipButtonStartStop?.background = ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_stop_circle_24,null)
                binding?.eipCountdowntimer?.visible()
                binding?.eipChronometer?.invisible()
            }
            is ChronometerState.StopState -> {
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.RunningState -> {
                binding?.eipButtonStartStop?.background = ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_stop_circle_24,null)
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.SaveState -> {
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.CloseState -> TODO()
        }
    }

    private fun updateUi(
        order : Int,
        totalSets : Int,
        time : Int,
        restTime : Int,
        exerciseType : ExerciseType,
        reps : Int,
        weight : Int
    ){

        binding?.eipTitleSets?.text =  "Set : ${order}/${totalSets}"

        binding?.eipTextRest?.text = "${restTime} sec"

        binding?.eipTextRep?.text = when(exerciseType){

            ExerciseType.TIME_EXERCISE -> { "${time} sec" }

            ExerciseType.REP_EXERCISE -> { "${reps} rep" }
        }

        binding?.eipTextWeight?.text = "${weight} kg"

    }

    private fun launchDialog(message : GenericMessageInfo.Builder){
        viewModel.onTriggerEvent(ExerciseInProgressEvents.LaunchDialog(message))
    }

    private fun areYouSureToQuitExercise(){

        val message = GenericMessageInfo.Builder()
            .id("ExerciseInProgressFragment.AreYouSureToQuit")
            .title(WIP_ARE_YOU_SURE_STOP_EXERCISE)
            .description("")
            .messageType(MessageType.Info)
            .uiComponentType(UIComponentType.Dialog)
            .positive(
                PositiveAction(
                    positiveBtnTxt = "OK",
                    onPositiveAction = {
                        saveAndNavigateback()
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

/*        viewModel.setStateEvent(
            WorkoutInProgressStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = WIP_ARE_YOU_SURE_STOP_EXERCISE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    saveAndNavigateback()
                                }

                                override fun cancel() {
                                }
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )*/
    }

/*    private fun navigateBackToastError(){
        viewModel.setStateEvent(
            WorkoutInProgressStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = WIP_ERROR_LOADING_EXERCISE,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Info()
                    )
                )
            )
        )
        navigateBack()
    }*/


/*
    private fun saveExercise(){
        //viewModel.saveExercise()
        //viewModel.onTriggerEvent(ExerciseInProgressEvents.Upda)
    }
*/

    /*
        Set management
     */

    private fun startSet(set : ExerciseSet){

        val updatedSet = set.copy(startedAt = dateUtil.getCurrentTimestamp())
        viewModel.onTriggerEvent(ExerciseInProgressEvents.UpdateActualSet(updatedSet))
        startChronometer()
    }

    private fun resetSet(set : ExerciseSet){
        val updatedSet = set.copy(startedAt = null)
        viewModel.onTriggerEvent(ExerciseInProgressEvents.UpdateActualSet(updatedSet))
        resetChronometer()
    }

    private fun stopSet(set : ExerciseSet){
        val updatedSet = set.copy(endedAt = dateUtil.getCurrentTimestamp())
        viewModel.onTriggerEvent(ExerciseInProgressEvents.UpdateActualSet(updatedSet))
        stopChronometer()
    }

    private fun stopRest(){
        setChronometerState(ChronometerState.SaveState())
        chronometer?.base = SystemClock.elapsedRealtime()
        timer?.cancel()
    }

    /*
        Chronometer
     */

    private fun setChronometerState(state : ChronometerState){
        viewModel.chronometerManager.setChronometerState(state)
    }

    private fun startChronometer(){
        setChronometerState(ChronometerState.RunningState())
        chronometer?.base = SystemClock.elapsedRealtime()
        chronometer?.onChronometerTickListener = null
        chronometer?.start()
    }

    private fun startRestChronometer(restTime : Int){
        val restTimeLong = restTime.toLong()
        timer = object : CountDownTimer(restTimeLong*1000 , 1000){

            override fun onTick(millisUntilFinished: Long) {
                countDownTimer?.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                stopRestChronometer()
            }
        }

        timer?.start()
        setChronometerState(ChronometerState.RestTimeState())
    }

    private fun stopRestChronometer(){
        chronometer?.base = SystemClock.elapsedRealtime()
        setChronometerState(ChronometerState.SaveState())
    }

    private fun resetChronometer(){
        chronometer?.stop()
        chronometer?.base = SystemClock.elapsedRealtime()
        setChronometerState(ChronometerState.IdleState())
    }

    private fun stopChronometer(){
        chronometer?.stop()
        setChronometerState(ChronometerState.StopState())
    }

    private fun closeChronometer(){
        chronometer?.stop()
        timer?.cancel()
        setChronometerState(ChronometerState.IdleState())
    }

    /********************************************************************
    BACK BUTTON PRESS
     *********************************************************************/

    private fun onBackPressed() {
        quitExercise()
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