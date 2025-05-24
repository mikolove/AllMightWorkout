package com.mikolove.workout.presentation.overview


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikolove.core.presentation.designsystem.AmwTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun WorkoutScreenRoot(
    /*onSearchClick : () -> Unit,
    onUpsertExerciseClick : (String) -> Unit,*/
    viewModel: WorkoutViewModel = koinViewModel()
){
    WorkoutScreen()
}

@Composable
fun WorkoutScreen(){
    Text(text = "Workout screen")
}

@Preview
@Composable
fun WorkoutScreenPreview(){
    AmwTheme {
        WorkoutScreen()
    }
}