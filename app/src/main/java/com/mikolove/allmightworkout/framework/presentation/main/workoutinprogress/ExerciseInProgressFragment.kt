package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Chronometer
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
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerState
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseInProgressFragment(): BaseFragment(R.layout.fragment_exercise_in_progress){

    val viewModel : WorkoutInProgressViewModel by activityViewModels()
    var binding : FragmentExerciseInProgressBinding? = null
    var chronometer : Chronometer? = null

    @Inject
    lateinit var dateUtil: DateUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onDestroyView() {
        viewModel.chronometerManager.stop()
        viewModel.chronometerManager.close()
        viewModel.cleanValidatedSetList()
        chronometer = null
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseInProgressBinding.bind(view)

        chronometer = binding?.eipChronometer
        chronometer?.let {
            viewModel.chronometerManager.setChronometer(it)
        }
        binding?.eipCountdowntimer?.let {
            viewModel.chronometerManager.setCountDownTimer(it)
        }

        init()
        subscribeObservers()

        binding?.eipButtonStartStop?.setOnClickListener {

            viewModel.getActualSet()?.let { set ->


               if(viewModel.chronometerManager.isIdleState()){
                    printLogD("ExerciseInProgressFragment","Start")
                    val updatedSet = set.copy(
                        startedAt = dateUtil.getCurrentTimestamp()
                    )
                    viewModel.setActualSet(updatedSet)
                    viewModel.chronometerManager.start()

                } else if(viewModel.chronometerManager.isRunningState()){
                    printLogD("ExerciseInProgressFragment","Stop Chrono")
                    val updatedSet = set.copy(
                        endedAt = dateUtil.getCurrentTimestamp()
                    )
                    viewModel.setActualSet(updatedSet)
                    viewModel.chronometerManager.stop()
                }else if (viewModel.chronometerManager.isRestState()){
                    printLogD("ExerciseInProgressFragment","Stop timer")
                    viewModel.chronometerManager.cancelRest()
               }
            }
        }

    }


    private fun getNextSet(){

        val sets = viewModel.getSets()
        val actualSetOrder = viewModel.getActualSet()?.order

        val nextSet = sets.find { it.order == actualSetOrder?.plus(1) }

        nextSet?.let {
            viewModel.setActualSet(it)
        }

    }

    private fun init(){
        viewModel.getExercise()?.let { exercise ->

            printLogD("ExerciseInProgressFragment","${exercise}")
            val sets = exercise.sets
            viewModel.setExerciseSetList(sets)

            sets.minWithOrNull(compareBy { it.order })?.let { set ->
                viewModel.setActualSet(set)
            }

        }
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            viewState.actualSet?.let { set ->
                printLogD("ExerciseInProgressFragment"," started at : ${set.startedAt} ended at : ${set.endedAt}")
                printLogD("ExerciseInProgressFragment","validatedSet ${viewModel.getValidatedSetList().size}")
                updateUi(set)
            }
        })

        viewModel.chronometerState.observe(viewLifecycleOwner, Observer { chronometerState ->

            printLogD("ExerciseInProgressFragment","STATE ${chronometerState}")
            switchClock(chronometerState)

            when(chronometerState){
                is ChronometerState.IdleState -> {
                    binding?.eipButtonStartStop?.text = "Start"
                }
                is ChronometerState.RunningState -> {
                    binding?.eipButtonStartStop?.text = "Stop"
                }

                is ChronometerState.StopState -> {
                    viewModel.getActualSet()?.let { set ->
                        if(viewModel.geTotalSets() > set.order){
                            viewModel.chronometerManager.startRest(set.restTime)
                        }
                    }

                }
                is ChronometerState.RestTimeState -> {
                    binding?.eipButtonStartStop?.text = "Stop Rest"
                }
                is ChronometerState.SaveState -> {
                    viewModel.getActualSet()?.let { set ->
                        if(set.startedAt != null && set.endedAt != null){
                            //Save it
                            viewModel.addSetToValidated(set)
                            //Load next
                            val nextSetExist = viewModel.loadNextSet(set)

                            if(!nextSetExist){
                                printLogD("ExerciseInProgressFragment","it was next set")
                                saveExercise()
                                findNavController().popBackStack()
                                //Deactivate all chrono button and cancel button
                                //Activate end button
                            }else{
                                viewModel.chronometerManager.setChronometerState(ChronometerState.IdleState())
                            }
                        }
                    }
                }
            }
        })
    }

    private fun switchClock(state : ChronometerState){
        when(state){

            is ChronometerState.IdleState -> {
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.RestTimeState -> {
                binding?.eipCountdowntimer?.visible()
                binding?.eipChronometer?.invisible()
            }
            is ChronometerState.StopState -> {
                binding?.eipCountdowntimer?.invisible()
                binding?.eipChronometer?.visible()
            }
            is ChronometerState.RunningState -> {
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
                                    saveExercise()
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
}