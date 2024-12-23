package com.mikolove.workout.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.mikolove.core.domain.workout.Workout


@Composable
fun WorkoutScreenRoot(){
    WorkoutScreen()
}

@Composable
fun WorkoutScreen(){
    Text(text = "Workout screen")
}


/*@Composable
fun WorkoutScreen(
    navController: NavController,
){

    val viewModel : WorkoutListViewModel = hiltViewModel()

    val workoutListState by viewModel.state.collectAsStateWithLifecycle()

    WorkoutScreenContent(
        modifier = Modifier.fillMaxSize(),
        state =workoutListState,
        events =viewModel::onTriggerEvent)
}

@Composable
fun WorkoutScreenContent(
    modifier: Modifier = Modifier,
    state : WorkoutListState,
    events : (WorkoutListEvents) -> Unit
){
    LazyColumn(modifier = Modifier.fillMaxSize() ){

        item {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(start = 24.dp)
            ) {
                WorkoutTypeFilterBar(filters = state.listWorkoutTypeFilter, onSelectedEvent = events)
                SwitchGrid {}
            }
        }

        itemsIndexed(state.listWorkoutCollection){id,collection ->

            Spacer(modifier = Modifier.size(18.dp))
            
            WorkoutCollection(
                workoutCollection = collection,
                onWorkoutClick = events)

            WorkoutCollection(
                workoutCollection = collection,
                onWorkoutClick = events,
                modifier = modifier
            )
        }
    }

}

@Composable
fun WorkoutCollection(
    workoutCollection : WorkoutCollection,
    onWorkoutClick : (WorkoutListEvents) -> Unit,
    modifier: Modifier = Modifier
){

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = workoutCollection.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            IconButton(
                onClick = { },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null
                )
            }
        }

        Workouts(workoutCollection.workouts,onWorkoutClick)

    }

}

@Composable
fun Workouts(
    workouts : List<Workout>,
    onWorkoutClick : (WorkoutListEvents) -> Unit,
    modifier: Modifier = Modifier
){

    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ) {
        items(workouts) { workout ->
            WorkoutItem(workout, onWorkoutClick)
        }
    }

}

@Composable
fun AddWorkoutItem(
    event : (WorkoutListEvents) -> Unit
){

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.Add ,
                contentDescription = "Add workout")
        }
    }
}
@Composable
fun WorkoutItem(
    workout : Workout,
    event : (WorkoutListEvents) -> Unit
){

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = workout.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
            )
        }
    }
}

@Composable
fun WorkoutTypeFilterBar(
    filters : List<WorkoutTypeFilter>,
    onSelectedEvent: (WorkoutListEvents) -> Unit
){
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 8.dp),
        modifier = Modifier.heightIn(min = 56.dp)
    ){
        items(filters){filter ->
            WorkoutTypeFilterChip(
                workoutTypeFilter = filter,
                onSelectedEvent = onSelectedEvent)
        }
    }
}

@Composable
fun SwitchGrid(
    onSelectedEvent: (WorkoutListEvents) -> Unit
){
    Box(modifier = Modifier
        .fillMaxSize()) {

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            onClick = {},){
            Icon( imageVector = Icons.Filled.AddCircle,
                contentDescription = "Selected",)

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTypeFilterChip(
    workoutTypeFilter : WorkoutTypeFilter,
    onSelectedEvent : (WorkoutListEvents) -> Unit
){

    FilterChip(
        selected = workoutTypeFilter.selected ,
        onClick = { onSelectedEvent },
        label = { Text(workoutTypeFilter.workoutType.name) },
        leadingIcon = if(workoutTypeFilter.selected){
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Selected",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        }else{
            null
        }
    )
}

@Preview
@Composable
fun WorkoutScreen(){

    WorkoutScreenContent(
        state = FakeData.getWokoutListState()) {}
}*/