@file:OptIn(ExperimentalMaterial3Api::class)

package com.mikolove.core.presentation.designsystem.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.BackIcon
import com.mikolove.core.presentation.designsystem.MenuMoreIcon
import com.mikolove.core.presentation.designsystem.SearchIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmwTopAppBar(
    titleRes: String,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String,
    actionIconEnabled : Boolean = false,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = titleRes ) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            if(actionIconEnabled){
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag("TopAppBar"),
    )
}

@Composable
fun AmwLargeTopAppBar(
    titleRes: String,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String,
    actionIconEnabled : Boolean = false,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
){
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LargeTopAppBar(
        title = {
            Text(
                titleRes,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigationClick() }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription
                )
            }
        },
        actions = {
            if(actionIconEnabled){
                IconButton(onClick = { onActionClick() }) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription
                    )
                }
            }
        },
        colors = colors,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun AmwTopAppBarPreview() {
    AmwTheme  {
        AmwTopAppBar(
            titleRes = "Workouts",
            navigationIcon = SearchIcon,
            navigationIconContentDescription = "Navigation icon",
            actionIcon = MenuMoreIcon,
            actionIconContentDescription = "Action icon",
            actionIconEnabled = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Large Top App Bar")
@Composable
private fun AmwLargeTopAppBarPreview() {
    AmwTheme  {
        AmwLargeTopAppBar(
            titleRes = "Workouts",
            navigationIcon = BackIcon,
            navigationIconContentDescription = "Navigation icon",
            actionIcon = MenuMoreIcon,
            actionIconContentDescription = "Action icon",
            actionIconEnabled = true
        )
    }
}