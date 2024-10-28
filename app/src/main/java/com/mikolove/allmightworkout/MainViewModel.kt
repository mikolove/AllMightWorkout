package com.mikolove.allmightworkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            //Check auth
            //Load workoutTypes
        }
    }

}