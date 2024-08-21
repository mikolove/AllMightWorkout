package com.mikolove.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

val BackIcon : ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24)

val LogoIcon : ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.baseline_android_24)

val ArrowRightIcon : ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.baseline_arrow_right_24)