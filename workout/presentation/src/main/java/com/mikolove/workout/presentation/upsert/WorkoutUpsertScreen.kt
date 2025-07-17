@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.mikolove.workout.presentation.upsert

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.BackIcon
import com.mikolove.core.presentation.designsystem.MenuMoreIcon
import com.mikolove.core.presentation.designsystem.components.AmwChip
import com.mikolove.core.presentation.designsystem.components.AmwFloatingButton
import com.mikolove.core.presentation.designsystem.components.AmwRoundedContainer
import com.mikolove.core.presentation.designsystem.components.AmwScaffold
import com.mikolove.core.presentation.designsystem.components.AmwTextField
import com.mikolove.core.presentation.designsystem.components.AmwTopAppBar
import com.mikolove.core.presentation.ui.ObserveAsEvents
import com.mikolove.workout.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutUpsertScreenRoot(
    onBackClick: () -> Unit,
    viewModel: WorkoutUpsertViewModel = koinViewModel()
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is WorkoutUpsertEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            is WorkoutUpsertEvent.OnFinish -> {
                onBackClick()
            }
        }
    }

    WorkoutUpsertEventScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is WorkoutUpsertAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@Composable
private fun WorkoutUpsertEventScreen(
    state: WorkoutUpsertState,
    onAction: (WorkoutUpsertAction) -> Unit
) {

    AmwScaffold(
        topAppBar = {
            AmwTopAppBar(
                titleRes = stringResource(id = R.string.workout_upsert_title),
                navigationIcon = BackIcon,
                navigationIconContentDescription = stringResource(id = R.string.search_icon_content_description),
                actionIcon = MenuMoreIcon,
                actionIconEnabled = false,
                actionIconContentDescription = stringResource(id = R.string.action_icon_content_description),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                onActionClick = {},
                onNavigationClick = { onAction(WorkoutUpsertAction.OnBackClick) },
            )
        },
        floatingActionButton = {
            AmwFloatingButton(
                onClick = {
                    onAction(WorkoutUpsertAction.OnUpsertClick)
                },
                enabled = state.isWorkoutValid,
                title = if(state.workoutId.isNotBlank())
                    stringResource(R.string.update_workout)
                else
                    stringResource(R.string.add_workout)
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {

           AmwRoundedContainer {

                Column(modifier = Modifier
                        .padding(16.dp)) {

                    AmwTextField(
                        state = state.nameSelected,
                        startIcon = null,
                        endIcon = null,
                        hint = stringResource(id = R.string.workout_name_hint),
                        title = stringResource(id = R.string.workout_name_title),
                        modifier = Modifier
                            .fillMaxWidth(),
                        additionalInfo = stringResource(R.string.workout_name_additional_info),
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = stringResource(R.string.workout_group_name))

                    if(state.groups.isNotEmpty()){
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.groups.forEach { groupUi ->
                                AmwChip(
                                    title = groupUi.name,
                                    selected = groupUi.selected,
                                    onClick = {}
                                )
                            }
                        }
                    }else{
                        Text(
                            text = stringResource(R.string.workout_no_group_name),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                    }
                }

            }
        }

    }


}

@PreviewLightDark
@Composable
private fun WorkoutUpsertEventScreenPreview() {
    AmwTheme {
        WorkoutUpsertEventScreen(
            state = WorkoutUpsertState(
                isWorkoutValid = true,
                groups = listOf(

                )
            ),
            onAction = {}
        )

    }

}