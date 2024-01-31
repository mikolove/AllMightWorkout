package com.mikolove.allmightworkout.framework.presentation.main.compose.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ExerciseScreen(){
    ExerciseScreenContent()
}

@Composable
fun ExerciseScreenContent(){
    Text(text = "Exercise screen")
}