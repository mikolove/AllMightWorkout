@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mikolove.workout.presentation.detail

import android.R.attr.top
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.BackIcon
import com.mikolove.core.presentation.designsystem.MenuMoreIcon
import com.mikolove.core.presentation.designsystem.components.AmwAddChip
import com.mikolove.core.presentation.designsystem.components.AmwLargeTopAppBar
import com.mikolove.core.presentation.designsystem.components.AmwScaffold
import com.mikolove.core.presentation.ui.ObserveAsEvents
import com.mikolove.core.presentation.ui.component.ExerciseCardItem
import com.mikolove.core.presentation.ui.model.BodyPartUi
import com.mikolove.core.presentation.ui.model.ExerciseUi
import com.mikolove.core.presentation.ui.model.GroupUi
import com.mikolove.core.presentation.ui.model.WorkoutUi
import com.mikolove.workout.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutDetailScreenRoot(
    viewModel: WorkoutDetailViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is WorkoutDetailEvent.Error -> {
            }
            WorkoutDetailEvent.Test -> {
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    WorkoutDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WorkoutDetailScreen(
    state: WorkoutDetailState,
    onAction: (WorkoutDetailAction) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope){

        AmwScaffold(
            topAppBar = {
                AmwLargeTopAppBar(
                    titleRes = state.workoutUi.name,
                    navigationIcon = BackIcon,
                    navigationIconContentDescription = stringResource(id = R.string.back_icon_content_description),
                    actionIcon = MenuMoreIcon,
                    actionIconContentDescription = stringResource(id = R.string.action_icon_content_description),
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    onActionClick = {},
                    onNavigationClick = {},
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = sharedTransitionScope.rememberSharedContentState(
                                key = "w-title-${state.workoutUi.idWorkout}"),
                            animatedVisibilityScope = animatedContentScope,
                        )
                ) },
            floatingActionButton = {

            },
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = sharedTransitionScope.rememberSharedContentState(
                            key = "w-bounds-${state.workoutUi.idWorkout}"),
                        animatedVisibilityScope = animatedContentScope,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                    )
                    .padding(paddingValues = paddingValues)
                    .fillMaxSize()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    // Groups

                    Text(
                        text = stringResource(R.string.workout_group_name),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    HorizontalDivider()

                    FlowRow( modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        for(group in state.workoutUi.groups){
                            AmwAddChip(
                                title = group.name.replaceFirstChar { it.uppercase() },
                                selected = false,
                                onClick = {}
                            )
                        }

                        AmwAddChip(
                            title = stringResource(R.string.chip_add_group),
                            selected = false,
                            onClick = {}
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //Exercises
                    Text(
                        text = stringResource(R.string.wd_exercise_title),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    HorizontalDivider()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.workoutUi.exercises){ exerciseUi ->
                            ExerciseCardItem(
                                exerciseUi = exerciseUi,
                                onCardItemClick = {},
                                modifier = Modifier
                            )
                        }
                    }
                }

            }

        }

    }

}

@Preview
@Composable
private fun WorkoutDetailScreenPreview() {
    AmwTheme {

        SharedTransitionLayout {
            AnimatedVisibility(true){
                WorkoutDetailScreen(
                    state = WorkoutDetailState(
                        workoutUi = WorkoutUi(
                            idWorkout = "id065181",
                            name = "Workout Name",
                            groups = listOf(
                                GroupUi("id065181", "group name"),
                                GroupUi("id065181", "group name 2"),
                            ),
                            exercises = listOf(
                                ExerciseUi(
                                    "id065181",
                                    name = "Exercise Name",
                                    //sets = emptyList(),
                                    bodyPart = listOf(
                                        BodyPartUi(idBodyPart = "123" , name = "Body Part 1"),
                                        BodyPartUi(idBodyPart = "123" , name = "Body Part 1"),
                                        BodyPartUi(idBodyPart = "123" , name = "Body Part 1"),
                                        BodyPartUi(idBodyPart = "1234" , name = "Body Part 2")
                                    ),
                                    exerciseType = "Exercise Type",
                                    isActive = true,
                                    createdAt = "01/01/2024 10:00",
                                    updatedAt = "01/01/2024 10:00"
                                ), ExerciseUi(
                                    "id065182",
                                    name = "Exercise Name",
                                    //sets = emptyList(),
                                    bodyPart = listOf(
                                        BodyPartUi(idBodyPart = "123" , name = "Body Part 1"),
                                        BodyPartUi(idBodyPart = "123" , name = "Body Part 1"),
                                        BodyPartUi(idBodyPart = "123" , name = "Body Part 1"),
                                        BodyPartUi(idBodyPart = "1234" , name = "Body Part 2")
                                    ),
                                    exerciseType = "Exercise Type",
                                    isActive = true,
                                    createdAt = "01/01/2024 10:00",
                                    updatedAt = "01/01/2024 10:00"
                                )
                            ),
                            isActive = true,
                            createdAt = "01/01/2024 10:00",
                            updatedAt = "01/01/2024 10:00",
                            startedAt = "01/01/2024 10:00",
                            endedAt = "01/01/2024 10:00"
                        )
                    ),
                    onAction = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedVisibility,
                )
            }
        }

    }

}