@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)

package com.mikolove.core.presentation.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.ui.model.BodyPartUi
import com.mikolove.core.presentation.ui.model.ExerciseUi
import com.mikolove.core.presentation.ui.model.WorkoutUi
import com.mikolove.core.presentation.ui.R


@Composable
fun WorkoutCardItem(
    workoutUi: WorkoutUi,
    containerAnimationKey : String,
    titleAnimationKey : String,
    onCardItemClick : () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
){
    with(sharedTransitionScope){
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = modifier
                .padding(8.dp)
                .sharedBounds(
                    sharedContentState = sharedTransitionScope.rememberSharedContentState(key = "$containerAnimationKey-${workoutUi.idWorkout}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                ),
            onClick = { onCardItemClick() }
        ) {

            Text(
                text = workoutUi.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(8.dp)
                    .sharedElement(
                        sharedContentState = sharedTransitionScope.rememberSharedContentState(key ="$titleAnimationKey-${workoutUi.idWorkout}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )

            ExerciseSection(
                modifier = Modifier
                    .padding(8.dp),
                exercises = workoutUi.exercises,
            )

            DateSection(
                modifier = Modifier
                    .padding(8.dp),
                createdAt = workoutUi.createdAt
            )

        }
    }
}

@Composable
fun ExerciseSection(
    exercises: List<ExerciseUi> = emptyList(),
    modifier: Modifier = Modifier
){

    Row(
        modifier =modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "${exercises.size} "+stringResource(R.string.exercises),
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
        )
    }
}

@Composable
fun DateSection(
    createdAt : String,
    modifier: Modifier = Modifier
){

    Row(
        modifier =modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(
            text = stringResource(R.string.created_at).plus(" $createdAt"),
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
        )
    }
}

@Composable
fun ExerciseCardItem(
    exerciseUi: ExerciseUi,
    onCardItemClick : () -> Unit,
    modifier: Modifier = Modifier
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier,
        onClick = { onCardItemClick() }
    ) {

        Text(
            text = exerciseUi.name,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        BodyPartSection(
            modifier = Modifier
                .padding(8.dp),
            bodyParts = exerciseUi.bodyPart,
        )

        DateSection(
            modifier = Modifier
                .padding(8.dp),
            createdAt = exerciseUi.createdAt
        )

    }
}

@Composable
fun BodyPartSection(
    bodyParts: List<BodyPartUi> = emptyList(),
    modifier: Modifier = Modifier
){

    Row(
        modifier =modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = bodyParts.joinToString { it.name },
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontStyle = FontStyle.Italic,
        )
    }
}



@Preview
@Composable
fun ExerciseCardItemPreview(){
    AmwTheme{
        ExerciseCardItem(
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
            ),
            {}
        )
    }
}


@Preview
@Composable
fun WorkoutCardItemPreview(){
    AmwTheme{
        SharedTransitionLayout {
            AnimatedVisibility(
                true
            ) {
                WorkoutCardItem(
                    workoutUi = WorkoutUi(
                        idWorkout = "id065181",
                        name = "Workout Name",
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
                    ),
                    onCardItemClick = {},
                    modifier = Modifier,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    containerAnimationKey = "w-container",
                    titleAnimationKey = "w-title"
                )
            }
        }
    }
}
