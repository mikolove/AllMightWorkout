package com.mikolove.allmightworkout.framework.presentation.main.compose.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.main.NavGraphs
import com.mikolove.allmightworkout.framework.presentation.main.destinations.DirectionDestination
import com.mikolove.allmightworkout.framework.presentation.main.destinations.ExerciseScreenDestination
import com.mikolove.allmightworkout.framework.presentation.main.destinations.HomeScreenDestination
import com.mikolove.allmightworkout.framework.presentation.main.destinations.WorkoutScreenDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.isRouteOnBackStack

enum class NavigationBarItem(
    val direction: DirectionDestination,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Home(HomeScreenDestination, Icons.Default.Person, R.string.fragment_home_tab_layout_history),
    Workout(WorkoutScreenDestination, Icons.AutoMirrored.Filled.List, R.string.fragment_home_tab_layout_workout),
    Exercise(ExerciseScreenDestination, Icons.AutoMirrored.Filled.List, R.string.fragment_home_tab_layout_exercise)
}

@Composable
fun BottomBar(
    navController: NavHostController
) {
    NavigationBar {
        NavigationBarItem.values().forEach { destination ->
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)
            NavigationBarItem(
                selected = isCurrentDestOnBackStack,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        // When we click again on a bottom bar item and it was already selected
                        // we want to pop the back stack until the initial destination of this bottom bar item
                        navController.popBackStack(destination.direction, false)
                        return@NavigationBarItem
                    }

                    navController.navigate(destination.direction) {
                        // Pop up to the root of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(NavGraphs.root) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = { Text(stringResource(destination.label)) },)

        }
    }
}

