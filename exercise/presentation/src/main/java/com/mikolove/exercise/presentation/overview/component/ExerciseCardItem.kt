package com.mikolove.exercise.presentation.overview.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.mikolove.exercise.presentation.R
import com.mikolove.exercise.presentation.model.ExerciseUi

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
        modifier = modifier
            .padding(8.dp),
        onClick = { onCardItemClick() }
    ) {

        Text(
            text = exerciseUi.name,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
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
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
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
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
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