package com.mikolove.allmightworkout.framework.presentation.main.compose.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>
@Composable
fun ExerciseScreen(){
    ExerciseScreenContent()
}

@Composable
fun ExerciseScreenContent(){
    Text(text = "Exercise screen")
}