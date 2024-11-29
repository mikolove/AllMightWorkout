@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class,
)

package com.mikolove.exercise.presentation.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.MenuMoreIcon
import com.mikolove.core.presentation.designsystem.SearchIcon
import com.mikolove.core.presentation.designsystem.components.AmwChip
import com.mikolove.core.presentation.designsystem.components.AmwFloatingButton
import com.mikolove.core.presentation.designsystem.components.AmwScaffold
import com.mikolove.core.presentation.designsystem.components.AmwTopAppBar
import com.mikolove.exercise.presentation.R
import com.mikolove.exercise.presentation.model.ExerciseCategoryUi
import com.mikolove.exercise.presentation.model.ExerciseUi
import com.mikolove.exercise.presentation.overview.component.ExerciseCardItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExerciseScreenRoot(
    viewModel: ExerciseViewModel = koinViewModel()
) {

    ExerciseScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@Composable
private fun ExerciseScreen(
    state: ExerciseState,
    onAction: (ExerciseAction) -> Unit
) {

    AmwScaffold(
        topAppBar = {
            AmwTopAppBar(
                titleRes = stringResource(id = R.string.exercise_title),
                navigationIcon = SearchIcon,
                navigationIconContentDescription = stringResource(id = R.string.search_icon_content_description),
                actionIcon = MenuMoreIcon,
                actionIconContentDescription = stringResource(id = R.string.action_icon_content_description),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                ),
                onActionClick = { },
                onNavigationClick = { },
            )
        },
        floatingActionButton = {
            AmwFloatingButton(
                onClick = { },
                enabled = true,
                title = stringResource(R.string.add_exercise)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.exercise_filter_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )

                HorizontalDivider(thickness = 1.dp)
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AmwChip(
                    title = stringResource(R.string.chip_filter_all),
                    selected = state.workoutTypes.any { !it.enabled }
                )
                state.workoutTypes.forEach { workoutTypeUi ->
                    AmwChip(
                        title = workoutTypeUi.name,
                        selected = workoutTypeUi.enabled
                    )
                }
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(state.exerciseCategories.flatMap { it.exercises }) { exerciseUi ->
                        ExerciseCardItem(
                            exerciseUi = exerciseUi,
                            onCardItemClick = { }
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

    }
}

@Preview
@Composable
private fun ExerciseScreenPreview() {
    AmwTheme {
        ExerciseScreen(
            state = ExerciseState(
                exerciseCategories = listOf(
                    ExerciseCategoryUi(
                        idWorkoutType = "1",
                        name = "Category 1",
                        exercises = listOf(
                            ExerciseUi(
                                idExercise = "1",
                                name = "Exercise 1",
                                sets = emptyList(),
                                bodyPart = emptyList(),
                                exerciseType = "Exercise Type",
                                isActive = true,
                                createdAt = "01/01/2024 10:00",
                                updatedAt = "01/01/2024 10:00"
                            ),
                            ExerciseUi(
                                idExercise = "2",
                                name = "Exercise 2",
                                sets = emptyList(),
                                bodyPart = emptyList(),
                                exerciseType = "Exercise Type",
                                isActive = true,
                                createdAt = "01/01/2024 10:00",
                                updatedAt = "01/01/2024 10:00"
                            )
                        )
                    ),
                    ExerciseCategoryUi(
                        idWorkoutType = "2",
                        name = "Category 2",
                        exercises = listOf(
                            ExerciseUi(
                                idExercise = "1",
                                name = "Exercise 1",
                                sets = emptyList(),
                                bodyPart = emptyList(),
                                exerciseType = "Exercise Type",
                                isActive = true,
                                createdAt = "01/01/2024 10:00",
                                updatedAt = "01/01/2024 10:00"
                            ),
                            ExerciseUi(
                                idExercise = "3",
                                name = "Exercise 3",
                                sets = emptyList(),
                                bodyPart = emptyList(),
                                exerciseType = "Exercise Type",
                                isActive = true,
                                createdAt = "01/01/2024 10:00",
                                updatedAt = "01/01/2024 10:00"
                            )
                        )
                    )
                )
            ),
            onAction = {}
        )
    }

}