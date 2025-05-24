@file:OptIn(ExperimentalMaterial3Api::class)

package com.mikolove.exercise.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.BackIcon
import com.mikolove.core.presentation.designsystem.components.AmwScaffold
import com.mikolove.core.presentation.designsystem.components.AmwSearchAppBar
import com.mikolove.exercise.presentation.R
import com.mikolove.core.presentation.ui.model.ExerciseUi
import com.mikolove.exercise.presentation.overview.component.ExerciseCardItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExerciseSearchScreenRoot(
    onBack: () -> Unit,
    onNavigateToExerciseDetail: (String) -> Unit,
    viewModel: ExerciseSearchViewModel = koinViewModel()
){
    ExerciseSearchScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
                is ExerciseSearchAction.onBackClick -> onBack()
                is ExerciseSearchAction.onDetailClick -> onNavigateToExerciseDetail(action.exerciseId)
            }
            viewModel.onAction(action)
        })
}

@Composable
fun ExerciseSearchScreen(
    state : ExerciseSearchState,
    onAction :(ExerciseSearchAction) -> Unit
){
    AmwScaffold(
        topAppBar = {
            AmwSearchAppBar(
                navigationIcon = BackIcon,
                navigationIconContentDescription = "",
                hint = stringResource(id = R.string.exercise_search_hint),
                searchQuery = state.searchQuerySelected,
                onBackClick = { onAction(ExerciseSearchAction.onBackClick) }
            )
        }
    ){ paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {

            if(state.exercises.isEmpty()){
                Text(
                    text = stringResource(R.string.no_exercise_matches),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize())
            }else{
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(state.exercises) { exerciseUi ->
                            ExerciseCardItem(
                                exerciseUi = exerciseUi,
                                onCardItemClick = {
                                    onAction(ExerciseSearchAction.onDetailClick(exerciseId = exerciseUi.idExercise))
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Preview
@Composable
private fun ExerciseSearchScreenPreview(){
    AmwTheme {
        ExerciseSearchScreen(
            state = ExerciseSearchState(
                exercises = listOf(
                    ExerciseUi(
                        idExercise = "adada",
                        name = "test",
                        bodyPart = listOf(),
                        exerciseType = "exerciseType",
                        isActive = true,
                        createdAt = "date1",
                        updatedAt = "date2"
                    )
                )),
            onAction = {}
        )
    }
}