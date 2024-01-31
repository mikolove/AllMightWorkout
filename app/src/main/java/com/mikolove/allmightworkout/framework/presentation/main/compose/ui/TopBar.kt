@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.mikolove.allmightworkout.framework.presentation.main.destinations.Destination
import com.mikolove.allmightworkout.framework.presentation.main.destinations.ExerciseScreenDestination
import com.mikolove.allmightworkout.framework.presentation.main.destinations.HomeScreenDestination
import com.mikolove.allmightworkout.framework.presentation.main.destinations.LoadingScreenDestination
import com.mikolove.allmightworkout.framework.presentation.main.destinations.WorkoutScreenDestination

@Composable
fun TopBar(
    destination: Destination,
    navBackStackEntry: NavBackStackEntry?
) {
    TopAppBar(
        title = {
            Spacer(Modifier.width(8.dp))
            Text(
                text = destination.topBarTitle(navBackStackEntry),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp)
        })
}



@Composable
fun Destination.topBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (this) {
        /*TaskScreenDestination -> {
            // Here you can also call another Composable on another file like TaskScreenTopBar
            // 👇 access the same viewmodel instance the screen is using, by passing the back stack entry
            val task = navBackStackEntry?.let {
                viewModel<TaskDetailsViewModel>(navBackStackEntry).task.collectAsState().value
            }
            task?.title ?: ""
        }
        StepScreenDestination -> {
            // Here you can also call another Composable on another file like StepScreenTopBar
            // 👇 access the same viewmodel instance the screen is using, by passing the back stack entry
            val viewModel = navBackStackEntry?.let { viewModel<StepDetailsViewModel>(navBackStackEntry) }
            val step = viewModel?.let {
                viewModel.step.collectAsState().value
            }
            val task = viewModel?.let {
                viewModel.task.collectAsState().value
            }
            "${task?.title ?: ""}: ${step?.title ?: ""}"
        }*/
        LoadingScreenDestination, HomeScreenDestination, WorkoutScreenDestination, ExerciseScreenDestination -> javaClass.simpleName.removeSuffix("Destination")
        else -> ""
        //TaskListScreenDestination -> javaClass.simpleName.removeSuffix("Destination")
    }
}
