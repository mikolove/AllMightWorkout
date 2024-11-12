/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mikolove.allmightworkout.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material.icons.outlined.SportsScore
import androidx.compose.ui.graphics.vector.ImageVector
import com.mikolove.allmightworkout.R
import kotlin.reflect.KClass

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.home_title,
        titleTextId = R.string.home_title,
        route = AnalyticsRoute::class,
        baseRoute = HomeRoute::class,

    ),
    WORKOUTS(
        selectedIcon = Icons.Filled.SportsScore,
        unselectedIcon = Icons.Outlined.SportsScore,
        iconTextId = R.string.workouts_title,
        titleTextId = R.string.workouts_title,
        route = WorkoutListRoute::class,
        baseRoute = WorkoutsRoute::class,
    ),
    EXERCISES(
        selectedIcon = Icons.Filled.LibraryAdd,
        unselectedIcon = Icons.Outlined.LibraryAdd,
        iconTextId = R.string.exercises_title,
        titleTextId = R.string.exercises_title,
        route = ExerciseListRoute::class ,
        baseRoute = ExercisesRoute::class ,
    ),
}
