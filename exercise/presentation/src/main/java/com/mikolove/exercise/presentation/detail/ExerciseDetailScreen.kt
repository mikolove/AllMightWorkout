@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.mikolove.exercise.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.BackIcon
import com.mikolove.core.presentation.designsystem.DeleteIcon
import com.mikolove.core.presentation.designsystem.MenuMoreIcon
import com.mikolove.core.presentation.designsystem.components.AmwChip
import com.mikolove.core.presentation.designsystem.components.AmwDropDownTextField
import com.mikolove.core.presentation.designsystem.components.AmwFloatingButton
import com.mikolove.core.presentation.designsystem.components.AmwScaffold
import com.mikolove.core.presentation.designsystem.components.AmwTextField
import com.mikolove.core.presentation.designsystem.components.AmwTopAppBar
import com.mikolove.core.presentation.ui.ObserveAsEvents
import com.mikolove.core.presentation.ui.model.BodyPartUi
import com.mikolove.core.presentation.ui.model.WorkoutTypeUi
import com.mikolove.core.presentation.ui.model.getNames
import com.mikolove.exercise.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExerciseDetailScreenRoot(
    onFinish : () -> Unit,
    onBack : () -> Unit,
    viewModel: ExerciseDetailViewModel = koinViewModel()
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is ExerciseDetailEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            is ExerciseDetailEvent.ExerciseSaved,
                 ExerciseDetailEvent.ExerciseDeleted-> onFinish()
        }
    }

    if(viewModel.state.isDataLoaded) {
        ExerciseDetailScreen(
            state = viewModel.state,
            onAction = { action ->
                when(action) {
                    is ExerciseDetailAction.onBackClick->{
                        //Maybe add processing blocker
                        onBack()
                    }
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseDetailScreen(
    state: ExerciseDetailState,
    onAction: (ExerciseDetailAction) -> Unit
) {

    AmwScaffold(
        topAppBar = {
            AmwTopAppBar(
                titleRes = stringResource(id = R.string.exercise_detail_title),
                navigationIcon = BackIcon,
                navigationIconContentDescription = stringResource(id = R.string.back_icon_content_description),
                actionIcon = DeleteIcon,
                actionIconEnabled =  state.exerciseId.isNotBlank(),
                actionIconContentDescription = stringResource(id = R.string.action_icon_content_description),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                onActionClick = {
                        onAction(ExerciseDetailAction.onDeleteClick(state.exerciseId))
                },
                onNavigationClick = {
                    onAction(ExerciseDetailAction.onBackClick)
                },
            )
        },
        floatingActionButton = {
            AmwFloatingButton(
                onClick = {
                    onAction(ExerciseDetailAction.onUpsertClick)
                },
                enabled = state.isExerciseValid,
                title = if(state.exerciseId.isNotBlank())
                    stringResource(R.string.update_exercise)
                else
                    stringResource(R.string.insert_exercise)
            )
        },
    ) {
            paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                AmwTextField(
                    state = state.nameSelected,
                    startIcon = null,
                    endIcon = null,
                    hint = stringResource(R.string.exercise_name_hint),
                    title = stringResource(R.string.exercise_name_title),
                    modifier = Modifier.fillMaxWidth(),
                    additionalInfo = stringResource(R.string.exercise_name_error),
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(16.dp))

                AmwDropDownTextField(
                    state = state.exerciseTypeSelected,
                    options = state.exerciseTypes.getNames(),
                    hint = stringResource(R.string.exercise_type_hint),
                    title = stringResource(R.string.exercise_type_title),
                    modifier = Modifier,
                    additionalInfo = stringResource(R.string.error_must_select),
                )

                Spacer(modifier = Modifier.height(16.dp))

                AmwDropDownTextField(
                    state = state.workoutTypeSelected,
                    options = state.workoutTypes.getNames(),
                    hint = stringResource(R.string.exercise_type_hint),
                    title = stringResource(R.string.exercise_wkt_title),
                    modifier = Modifier,
                    additionalInfo = stringResource(R.string.error_must_select),
                )

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    state.bodyParts.forEach { bodyPartUi ->
                        AmwChip(
                            title = bodyPartUi.name,
                            selected = bodyPartUi.selected,
                            onClick = {
                                onAction(ExerciseDetailAction.onBodyPartClick(bodyPartUi.idBodyPart))
                            }
                        )
                    }
                }
            }
        }
    }

    }

@Preview
@Composable
private fun ExerciseDetailScreenPreview() {
    AmwTheme {
        ExerciseDetailScreen(
            state = ExerciseDetailState(
                workoutTypes = listOf(
                    WorkoutTypeUi("Id","Arm"),
                    WorkoutTypeUi("Id","Legs"),
                ),
                bodyParts = listOf(
                    BodyPartUi("id","Body1"),
                    BodyPartUi("id","Body2"),
                )
            ),
            onAction = {}
        )
    }
}