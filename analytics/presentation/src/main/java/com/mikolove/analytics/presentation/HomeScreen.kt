package com.mikolove.analytics.presentation


import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState){


    val scope = rememberCoroutineScope()
    HomeScreenContent(
        showSnack = {
            scope.launch {
                snackbarHostState.showSnackbar("Test my snack")
            }
        })
}

@Composable
fun HomeScreenContent(
    showSnack : () -> Unit){
    Text(text = "Home screen")
    Button(onClick = { showSnack() }) {

    }
}