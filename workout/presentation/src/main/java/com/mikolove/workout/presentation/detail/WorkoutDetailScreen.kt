package com.mikolove.workout.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikolove.core.presentation.designsystem.AmwTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutDetailScreenRoot(
    viewModel: WorkoutDetailViewModel = koinViewModel()
) {

    WorkoutDetailScreen(
        state = viewModel.state,
        onAction = viewModel::onAction

    )

}

@Composable
private fun WorkoutDetailScreen(
    state: WorkoutDetailState,
    onAction: (WorkoutDetailAction) -> Unit
) {

}

@Preview
@Composable
private fun WorkoutDetailScreenPreview() {
    AmwTheme {

        WorkoutDetailScreen(
            state = WorkoutDetailState(),
            onAction = {}
        )

    }

}