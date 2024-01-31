package com.mikolove.allmightworkout.framework.presentation.main.compose.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.mikolove.allmightworkout.framework.presentation.main.appCurrentDestinationAsState
import com.mikolove.allmightworkout.framework.presentation.main.destinations.Destination
import com.mikolove.allmightworkout.framework.presentation.main.startAppDestination
import com.ramcosta.composedestinations.spec.Route


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun BaseScaffold(
    startRoute: Route,
    navController: NavHostController,
    topBar: @Composable (Destination, NavBackStackEntry?) -> Unit,
    bottomBar: @Composable (Destination) -> Unit,
    floatingActionButton : @Composable (Destination) -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit,
) {
    val destination = navController.appCurrentDestinationAsState().value
        ?: startRoute.startAppDestination
    val navBackStackEntry = navController.currentBackStackEntry

    navController.currentBackStack.collectAsState().value.print()

    Scaffold(
        topBar = { topBar(destination, navBackStackEntry) },
        bottomBar = { bottomBar(destination) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton =  { floatingActionButton(destination) },
        content = content
    )

}

private fun Collection<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = map { it.destination.route }.toTypedArray().contentToString()
    println("$prefix = $stack")
}