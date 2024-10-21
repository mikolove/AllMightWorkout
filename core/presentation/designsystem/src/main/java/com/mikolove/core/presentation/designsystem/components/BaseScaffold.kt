package com.mikolove.core.presentation.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

/*
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun BaseScaffold(
    startRoute: Route,
    navController: NavHostController,
    topBar: @Composable (DestinationSpec, NavBackStackEntry?) -> Unit,
    bottomBar: @Composable (DestinationSpec) -> Unit,
    floatingActionButton : @Composable (DestinationSpec) -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit,
) {
    val destination = navController.currentDestinationAsState().value
        ?: startRoute.startDestination
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
}*/