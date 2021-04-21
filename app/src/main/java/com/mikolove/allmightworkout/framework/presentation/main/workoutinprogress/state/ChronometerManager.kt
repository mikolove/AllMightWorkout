package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state

import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.util.printLogD
import kotlin.concurrent.timer

class ChronometerManager() {

    private var chronometer: Chronometer? = null
    private var countDownTimer : TextView? = null
    private var timer : CountDownTimer? = null


    private val _chronometerState : MutableLiveData<ChronometerState>
        = MutableLiveData(ChronometerState.IdleState())
    private val chronometerState : LiveData<ChronometerState>
        get() = _chronometerState

    fun setChronometer(chronometer: Chronometer){
        this.chronometer = chronometer
    }

    fun setCountDownTimer(countDownTimer: TextView){
        this.countDownTimer = countDownTimer
    }

    fun start(){
        setChronometerState(ChronometerState.RunningState())
        chronometer?.base = SystemClock.elapsedRealtime()
        chronometer?.onChronometerTickListener = null
        chronometer?.start()
    }

    fun startRest(restTime : Int){

        val restTimeLong = restTime.toLong()
        timer = object : CountDownTimer(restTimeLong*1000 , 1000){

            override fun onTick(millisUntilFinished: Long) {
                countDownTimer?.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                stopRest()
            }
        }

        timer?.start()
        setChronometerState(ChronometerState.RestTimeState())
    }

    fun cancelRest(){
        timer?.cancel()
        stopRest()
    }

    fun stopRest(){
        setChronometerState(ChronometerState.SaveState())
        chronometer?.base = SystemClock.elapsedRealtime()
    }

    fun stop(){
        setChronometerState(ChronometerState.StopState())
        chronometer?.stop()
    }

    fun setChronometerState( state : ChronometerState){
        _chronometerState.value = state
    }

    fun close(){
        timer?.cancel()
        chronometer?.stop()
        setChronometerState(ChronometerState.IdleState())
        countDownTimer = null
        chronometer = null
        timer = null
    }

    fun getChronometerSate() = chronometerState

    fun isIdleState()  = chronometerState.value.toString().equals(ChronometerState.IdleState().toString())

    fun isRunningState()  = chronometerState.value.toString().equals(ChronometerState.RunningState().toString())

    fun isStopState()  = chronometerState.value.toString().equals(ChronometerState.StopState().toString())

    fun isRestState()  = chronometerState.value.toString().equals(ChronometerState.RestTimeState().toString())
}