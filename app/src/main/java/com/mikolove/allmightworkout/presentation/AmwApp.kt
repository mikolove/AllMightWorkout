@file:OptIn(ExperimentalMaterial3Api::class)

package com.mikolove.allmightworkout.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mikolove.allmightworkout.navigation.AuthRoute
import com.mikolove.allmightworkout.navigation.HomeRoute
import com.mikolove.allmightworkout.navigation.NavigationRoot
import com.mikolove.core.presentation.designsystem.components.AmwNavigationSuiteScaffold
import kotlin.reflect.KClass


@Composable
fun AmwAppRoot(
    appState: AmwAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    isLoggedIn :Boolean,
    onLogout: () -> Unit = {}
){

    /*
        Here
        Apply custom settings background,...

        Store some state UI status like show or hide menu in rememberSaveable

        Could deal with some state status in snackbar
        Connected / Disconnected
     */

    AmwApp(
        appState = appState,
        modifier = modifier,
        windowAdaptiveInfo = windowAdaptiveInfo,
        isLoggedIn = isLoggedIn,
        onLogout = onLogout
    )
}

@Composable
fun AmwApp(
    modifier: Modifier = Modifier,
    appState: AmwAppState,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    isLoggedIn: Boolean,
    onLogout: () -> Unit = {}
){

    val currentDestination = appState.currentDestination

    AmwNavigationSuiteScaffold(
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination
                    .isRouteInHierarchy(destination.baseRoute)
                item(
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.iconTextId)) },
                )
            }
        },
        shouldHideBottomBar = currentDestination.isRouteInHierarchy(AuthRoute::class),
        windowAdaptiveInfo = windowAdaptiveInfo,

        ) {

        NavigationRoot(
            appState = appState,
            route = if(isLoggedIn) HomeRoute else AuthRoute,
        )

        /*Scaffold(
             containerColor = Color.Transparent,
             contentColor = MaterialTheme.colorScheme.onBackground,
             contentWindowInsets = WindowInsets(0, 0, 0, 0),
         ) { padding ->
             Column(
                 Modifier
                     .fillMaxSize()
                     .padding(padding)
                     .consumeWindowInsets(padding)
                     .windowInsetsPadding(
                         WindowInsets.safeDrawing.only(
                             WindowInsetsSides.Horizontal,
                         ),
                     ),
             ) {


                 val destination = appState.currentTopLevelDestination
                 var shouldShowTopAppBar = false

                 Timber.d("AmwApp current Destination ${destination}")

                 if(destination != null){
                     shouldShowTopAppBar = true
                     AmwTopAppBar(
                         titleRes = stringResource(destination.titleTextId),
                         navigationIcon = SearchIcon,
                         navigationIconContentDescription = stringResource(id = R.string.search_icon_content_description),
                         actionIcon = MenuMoreIcon,
                         actionIconContentDescription = stringResource(id = R.string.action_icon_content_description),
                         colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                             containerColor = Color.Transparent,
                         ),
                         onActionClick = { },
                         onNavigationClick = {  },
                     )

                 }

                 Box(
                     modifier = Modifier.consumeWindowInsets(
                         if (shouldShowTopAppBar) {
                             WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                         } else {
                             WindowInsets(0, 0, 0, 0)
                         },
                     ),
                 ) {
                     NavigationRoot(
                         appState = appState,
                         route = if(isLoggedIn) HomeRoute else AuthRoute,
                     )
                 }

             }
         }*/
    }

}

fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
