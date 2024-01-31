package com.mikolove.allmightworkout.framework.presentation.main.compose.ui


import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch


@Destination
@Composable
fun HomeScreen(
    navController: NavController,
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