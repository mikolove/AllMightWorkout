@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
)

package com.mikolove.workout.presentation.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import com.mikolove.core.presentation.designsystem.components.AmwAddChip
import com.mikolove.core.presentation.designsystem.components.AmwChip
import com.mikolove.core.presentation.designsystem.components.AmwFloatingButton
import com.mikolove.core.presentation.designsystem.components.AmwScaffold
import com.mikolove.core.presentation.designsystem.components.AmwTopAppBar
import com.mikolove.workout.presentation.R
import com.mikolove.core.presentation.ui.component.WorkoutCardItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutScreenRoot(
    onSearchClick : () -> Unit,
    onAddGroupClick : () -> Unit,
    onUpsertWorkoutClick : (String) -> Unit,
    viewModel: WorkoutViewModel = koinViewModel()
){
    WorkoutScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
                is WorkoutAction.onUpsertWorkoutClick ->{
                    onUpsertWorkoutClick(action.workoutId)
                }
                is WorkoutAction.onSearchClick -> onSearchClick()
                is WorkoutAction.onAddGroupClick -> onAddGroupClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun WorkoutScreen(
    state : WorkoutState,
    onAction : (WorkoutAction) -> Unit
) {
    AmwScaffold(
        topAppBar = {
            AmwTopAppBar(
                titleRes = stringResource(id = R.string.workout_title),
                navigationIcon = SearchIcon,
                navigationIconContentDescription = stringResource(id = R.string.search_icon_content_description),
                actionIcon = MenuMoreIcon,
                actionIconContentDescription = stringResource(id = R.string.action_icon_content_description),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                ),
                onActionClick = {},
                onNavigationClick = { onAction(WorkoutAction.onSearchClick) },
            )
        },
        floatingActionButton = {
            AmwFloatingButton(
                onClick = {
                    onAction(WorkoutAction.onUpsertWorkoutClick(""))
                },
                enabled = true,
                title = stringResource(R.string.add_workout)
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
                    text = stringResource(R.string.workout_filter_title),
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
                    selected = state.workoutTypes.all { !it.selected },
                    onClick = {
                        onAction(WorkoutAction.onWorkoutTypeChipClick(null))
                    }
                )
                state.workoutTypes.forEach { workoutTypeUi ->
                    AmwChip(
                        title = workoutTypeUi.name,
                        selected = workoutTypeUi.selected,
                        onClick = {
                            onAction(WorkoutAction.onWorkoutTypeChipClick(workoutTypeUi.idWorkoutType))
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.workout_filter_groups_title),
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
                if(state.groups.isNotEmpty()){

                    AmwChip(
                        title = stringResource(R.string.chip_filter_all),
                        selected = state.groups.all { !it.selected },
                        onClick = {
                            onAction(WorkoutAction.onGroupChipClick(null))
                        }
                    )
                    state.groups.forEach { groupUi ->
                        AmwChip(
                            title = groupUi.name,
                            selected = groupUi.selected,
                            onClick = {
                                onAction(WorkoutAction.onGroupChipClick(groupUi.idGroup))
                            }
                        )
                    }

                }

                AmwAddChip(
                    title = stringResource(R.string.chip_add_group),
                    selected = false,
                    onClick = {
                        onAction(WorkoutAction.onAddGroupClick)
                    }
                )

            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(state.workouts) { workoutUi ->
                        WorkoutCardItem(
                            workoutUi = workoutUi,
                            onCardItemClick = {
                                onAction(WorkoutAction.onUpsertWorkoutClick(workoutId = workoutUi.idWorkout))
                            }
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
fun WorkoutScreenPreview(){
    AmwTheme {
        WorkoutScreen(
            state = WorkoutState(),
            onAction = {}
        )
    }
}