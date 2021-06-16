package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts
import com.mikolove.allmightworkout.databinding.FragmentExerciseInProgressBinding
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.invisible
import com.mikolove.allmightworkout.framework.presentation.common.visible
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerButtonState
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerState
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseInProgressFragment(): BaseFragment(R.layout.fragment_exercise_in_progress){

    val viewModel : WorkoutInProgressViewModel by activityViewModels()
    var binding : FragmentExerciseInProgressBinding? = null

    private var chronometer: Chronometer? = null
    private var countDownTimer : TextView? = null
    private var timer : CountDownTimer? = null

    @Inject
    lateinit var dateUtil: DateUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
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

        init()
        setupOnBackPressDispatcher()
        subscribeObservers()

        binding?.eipButtonStartStop?.setOnClickListener {

            viewModel.getActualSet()?.let { set ->
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
            viewModel.getActualSet()?.let { set ->
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
            val actualSet = viewModel.getActualSet()
            if( actualSet?.order == 1 && actualSet.startedAt == null){
                navigateBack()
            }else{
                areYouSureToQuitExercise()
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
        saveExercise()
        setChronometerState(ChronometerState.CloseState())
        navigateBack()
    }

    private fun navigateBack(){
        viewModel.setExercise(null)
        viewModel.setExerciseSetList(null)
        findNavController().popBackStack()
    }

    private fun init(){

        viewModel.getExercise()?.let { exercise ->
            val sets = exercise.sets
            viewModel.setExerciseSetList(sets)
            sets.minWithOrNull(compareBy { it.order })?.let { set ->
                viewModel.setActualSet(set)
            }

            chronometer = binding?.eipChronometer
            countDownTimer = binding?.eipCountdowntimer

        } ?: navigateBackToastError()

    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            viewState.actualSet?.let { set ->
                updateUi(set)
            }
        })

        viewModel.chronometerState.observe(viewLifecycleOwner, Observer { chronometerState ->

            switchClock(chronometerState)

            when(chronometerState){
                is ChronometerState.StopState -> {
                    viewModel.getActualSet()?.let { set ->
                        if(viewModel.geTotalSets() > set.order){
                            startRestChronometer(set.restTime)
                        }
                    }
                }
                is ChronometerState.SaveState -> {
                    viewModel.getActualSet()?.let { set ->
                        if(set.startedAt != null && set.endedAt != null){
                            viewModel.saveSet(set)
                            val nextSetExist = viewModel.loadNextSet(set)
                            if(!nextSetExist){
                                saveAndNavigateback()
                            }else{
                                setChronometerState(ChronometerState.IdleState())
                            }
                        }
                    }
                }

                is ChronometerState.CloseState -> {
                    closeChronometer()
                }
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
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when(response.message){

                    //If another we quit so we clear Message Stack
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
        }
    }

    private fun updateUi(set : ExerciseSet){

        binding?.eipTitleSets?.text =  "Set : ${set.order}/${viewModel.getSets().size}"

        binding?.eipTextRest?.text = "${set.restTime} sec"

        viewModel.getExercise()?.let {
            binding?.eipTextRep?.text = when(it.exerciseType){

                ExerciseType.TIME_EXERCISE -> {
                    "${set.time} sec"
                }

                ExerciseType.REP_EXERCISE -> {
                    "${set.reps} rep"
                }
            }

            binding?.eipTextWeight?.text = "${set.weight} kg"

        }

    }

    private fun areYouSureToQuitExercise(){
        viewModel.setStateEvent(
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
        )
    }

    private fun navigateBackToastError(){
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
    }


    private fun saveExercise(){
        viewModel.saveExercise()
    }

    /*
        Set management
     */

    private fun startSet(set : ExerciseSet){

        val updatedSet = set.copy(startedAt = dateUtil.getCurrentTimestamp())
        viewModel.setActualSet(updatedSet)
        startChronometer()
    }

    private fun resetSet(set : ExerciseSet){
        val updatedSet = set.copy(startedAt = null)
        viewModel.setActualSet(updatedSet)
        resetChronometer()
    }

    private fun stopSet(set : ExerciseSet){
        val updatedSet = set.copy(endedAt = dateUtil.getCurrentTimestamp())
        viewModel.setActualSet(updatedSet)
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