package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state

import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerButtonState.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerState.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.ChronometerState.StopState
import com.mikolove.allmightworkout.util.printLogD
import kotlin.concurrent.timer

class ChronometerManager() {

    private val _chronometerState : MutableLiveData<ChronometerState>
            = MutableLiveData(IdleState())
    val chronometerState : LiveData<ChronometerState>
        get() = _chronometerState

    private val _startButtonState : MutableLiveData<ChronometerButtonState>
        = MutableLiveData(StartButtonState())
    val startButtonState : LiveData<ChronometerButtonState>
        get() = _startButtonState

    private val _resetButtonState : MutableLiveData<ChronometerButtonState>
            = MutableLiveData(PassiveButtonState())
    val resetButtonState : LiveData<ChronometerButtonState>
        get() = _resetButtonState

    private val _endButtonState : MutableLiveData<ChronometerButtonState>
            = MutableLiveData(ActiveButtonState())
    val endButtonState : LiveData<ChronometerButtonState>
        get() = _endButtonState


    fun setChronometerState(state : ChronometerState) {
        if(!chronometerState.toString().equals(state.toString())){
            _chronometerState.value = state
            when(state){

                is IdleState ->{
                    _startButtonState.value = StartButtonState()
                    _resetButtonState.value = PassiveButtonState()
                    _endButtonState.value = ActiveButtonState()
                }

                is RunningState ->{
                    _startButtonState.value = StopButtonState()
                    _resetButtonState.value = PassiveButtonState()
                    _endButtonState.value = PassiveButtonState()
                }

                is RestTimeState -> {
                    _startButtonState.value = RestButtonState()
                    _resetButtonState.value = PassiveButtonState()
                    _endButtonState.value = PassiveButtonState()
                }

                is StopState ->{

                }

                is SaveState ->{

                }
            }
        }
    }


    fun getChronometerSate() = chronometerState

    fun isIdleState()  = chronometerState.value.toString().equals(IdleState().toString())

    fun isRunningState()  = chronometerState.value.toString().equals(RunningState().toString())

    fun isStopState()  = chronometerState.value.toString().equals(StopState().toString())

    fun isRestState()  = chronometerState.value.toString().equals(RestTimeState().toString())

    fun isStartButtonActive() = !startButtonState.value.toString().equals(PassiveButtonState().toString())

    fun isResetButtonActive() = !resetButtonState.value.toString().equals(PassiveButtonState().toString())

    fun isEndButtonActive() = !endButtonState.value.toString().equals(PassiveButtonState().toString())
}