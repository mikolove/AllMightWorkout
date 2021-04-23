package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import android.widget.TextView
import androidx.annotation.RequiresApi
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
        chronometer?.stop()
        timer?.cancel()
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
        subscribeObservers()

        binding?.eipButtonStartStop?.setOnClickListener {

            if(viewModel.chronometerManager.isStartButtonActive()){
                viewModel.getActualSet()?.let { set ->

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

        binding?.eipButtonEnd?.setOnClickListener {

            if(viewModel.chronometerManager.isEndButtonActive()){
                val actualSet = viewModel.getActualSet()
                if( actualSet?.order == 1 && actualSet.startedAt == null){
                    navigateBack()
                }else{
                    areYouSureToQuitExercise()
                }
            }
        }


    }

    private fun saveAndNavigateback(){
        saveExercise()
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
        }

        chronometer = binding?.eipChronometer
        countDownTimer = binding?.eipCountdowntimer

    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            viewState.actualSet?.let { set ->
                printLogD("ExerciseInProgressFragment"," started at : ${set.startedAt} ended at : ${set.endedAt}")
                updateUi(set)
            }
        })

        viewModel.chronometerState.observe(viewLifecycleOwner, Observer { chronometerState ->

            printLogD("ExerciseInProgressFragment","STATE ${chronometerState}")
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
                binding?.eipButtonStartStop?.text = "Start"
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.RestTimeState -> {
                binding?.eipButtonStartStop?.text = "Stop Rest"
                binding?.eipCountdowntimer?.visible()
                binding?.eipChronometer?.invisible()
            }
            is ChronometerState.StopState -> {
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.RunningState -> {
                binding?.eipButtonStartStop?.text = "Stop"
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

        binding?.eipTextInfoRestTime?.text = "Rest time : ${set.restTime}"

        viewModel.getExercise()?.let {
            binding?.eipTextInfoSet?.text = when(it.exerciseType){

                ExerciseType.TIME_EXERCISE -> {
                    "Time : ${set.time} Weight : ${set.weight}"
                }

                ExerciseType.REP_EXERCISE -> {
                    "Rep : ${set.reps} Weight : ${set.weight}"
                }
            }
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
                                    //Save exercise state
                                    saveAndNavigateback()
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

    private fun stopSet(set : ExerciseSet){
        val updatedSet = set.copy(endedAt = dateUtil.getCurrentTimestamp())
        viewModel.setActualSet(updatedSet)
        stopChronometer()
    }

    private fun stopRest(){
        setChronometerState(ChronometerState.SaveState())
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

    private fun stopChronometer(){
        chronometer?.stop()
        setChronometerState(ChronometerState.StopState())
    }
}