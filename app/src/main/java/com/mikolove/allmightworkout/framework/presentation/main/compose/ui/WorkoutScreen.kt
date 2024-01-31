package com.mikolove.allmightworkout.framework.presentation.main.compose.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun WorkoutScreen(navController: NavController){
    WorkoutScreenContent()
}

@Composable
fun WorkoutScreenContent(){
    Text(text = "Workout screen")
}