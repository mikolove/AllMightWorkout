@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class,
)

package com.mikolove.workout.presentation.overview

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.MenuMoreIcon
import com.mikolove.core.presentation.designsystem.SearchIcon
import com.mikolove.core.presentation.designsystem.components.AmwActionButton
import com.mikolove.core.presentation.designsystem.components.AmwAddChip
import com.mikolove.core.presentation.designsystem.components.AmwChip
import com.mikolove.core.presentation.designsystem.components.AmwFloatingButton
import com.mikolove.core.presentation.designsystem.components.AmwScaffold
import com.mikolove.core.presentation.designsystem.components.AmwTextDialog
import com.mikolove.core.presentation.designsystem.components.AmwTopAppBar
import com.mikolove.core.presentation.ui.ObserveAsEvents
import com.mikolove.core.presentation.ui.component.WorkoutCardItem
import com.mikolove.workout.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutScreenRoot(
    onSearchClick : () -> Unit,
    onAddGroupClick : () -> Unit,
    onWorkoutClick : (String) -> Unit,
    viewModel: WorkoutViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    ){

    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is WorkoutEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            is WorkoutEvent.OnCreateWorkout -> {
                //Go Details
                //onWorkoutClick(action.workoutId)
            }
        }
    }

    WorkoutScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
                is WorkoutAction.OnWorkoutClick ->  onWorkoutClick(action.workoutId)
                is WorkoutAction.OnSearchClick -> onSearchClick()
                is WorkoutAction.OnAddGroupClick -> onAddGroupClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )
}

@Composable
fun WorkoutScreen(
    state : WorkoutState,
    onAction : (WorkoutAction) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
) {

    var showDialog by remember { mutableStateOf(false) }

    AmwScaffold(
        topAppBar = {
            AmwTopAppBar(
                titleRes = stringResource(id = R.string.workout_title),
                navigationIcon = SearchIcon,
                navigationIconContentDescription = stringResource(id = R.string.search_icon_content_description),
                actionIcon = MenuMoreIcon,
                actionIconContentDescription = stringResource(id = R.string.action_icon_content_description),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                onActionClick = {},
                onNavigationClick = { onAction(WorkoutAction.OnSearchClick) },
            )
        },
        floatingActionButton = {
            AmwFloatingButton(
                onClick = {
                    showDialog = true
                    //onAction(WorkoutAction.OnUpsertWorkoutClick(""))
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = stringResource(R.string.workout_filter_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )

                    Spacer(modifier = Modifier.weight(1f))

                }

                HorizontalDivider(thickness = 1.dp)

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AmwChip(
                        title = stringResource(R.string.chip_filter_all),
                        selected = state.workoutTypes.all { !it.selected },
                        onClick = {
                            onAction(WorkoutAction.OnWorkoutTypeChipClick(null))
                        }
                    )
                    state.workoutTypes.forEach { workoutTypeUi ->
                        AmwChip(
                            title = workoutTypeUi.name,
                            selected = workoutTypeUi.selected,
                            onClick = {
                                onAction(WorkoutAction.OnWorkoutTypeChipClick(workoutTypeUi.idWorkoutType))
                            }
                        )
                    }
                }

            }

            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = stringResource(R.string.workout_filter_groups_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    AmwAddChip(
                        title = stringResource(R.string.chip_add_group),
                        selected = false,
                        onClick = {
                            onAction(WorkoutAction.OnAddGroupClick)
                        }
                    )
                }

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
                            onAction(WorkoutAction.OnGroupChipClick(null))
                        }
                    )
                    state.groups.forEach { groupUi ->
                        AmwChip(
                            title = groupUi.name,
                            selected = groupUi.selected,
                            onClick = {
                                onAction(WorkoutAction.OnGroupChipClick(groupUi.idGroup))
                            }
                        )
                    }

                }



            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(state.workouts) { workoutUi ->
                        WorkoutCardItem(
                            workoutUi = workoutUi,
                            containerAnimationKey = "w-container",
                            titleAnimationKey = "w-title",
                            onCardItemClick = {
                                onAction(WorkoutAction.OnWorkoutClick(workoutId = workoutUi.idWorkout))
                            },
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedContentScope
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if(showDialog){
                AmwTextDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    onConfirmRequest = { name ->
                        onAction(WorkoutAction.OnCreateWorkoutClick(name))
                        showDialog = false
                    },
                    title = stringResource(R.string.create_workout),
                    hint = stringResource(R.string.create_workout_hint),
                )

            }
        }
    }
}

@Preview
@Composable
fun WorkoutScreenPreview(){
    AmwTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                WorkoutScreen(
                    state = WorkoutState(),
                    onAction = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedVisibility,
                )
            }
        }

    }
}