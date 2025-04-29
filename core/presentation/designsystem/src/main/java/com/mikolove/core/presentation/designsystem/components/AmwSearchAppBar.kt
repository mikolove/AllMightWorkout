@file:OptIn(ExperimentalMaterial3Api::class)

package com.mikolove.core.presentation.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.BackIcon
import com.mikolove.core.presentation.designsystem.CloseIcon
import com.mikolove.core.presentation.designsystem.SearchIcon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmwSearchAppBar(
    searchQuery: TextFieldState,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    hint : String,
    onBackClick: () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
) {

    CenterAlignedTopAppBar(
        title = {
            SearchTextField(
                keyboardType = keyboardType,
                searchQuery = searchQuery,
                hint = hint
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = colors,
        modifier = modifier.testTag("TopExerciseSearchBar"),
    )
}

@Composable
private fun SearchTextField(
    keyboardType : KeyboardType,
    searchQuery: TextFieldState,
    hint : String
) {

    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        state = searchQuery,
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    true
                } else {
                    false
                }
            },
        decorator = { innerBox ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(color = MaterialTheme.colorScheme.outlineVariant)
                ,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if(searchQuery.text.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.4f
                            ),
                            modifier = Modifier.fillMaxWidth(),

                            )
                    }
                    innerBox()
                }

                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = {
                        searchQuery.clearText()
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,)
                }
            }
        }
    )
}

@Preview
@Composable
private fun SearchToolbarPreview() {
    AmwTheme {
        AmwSearchAppBar(
            navigationIcon = BackIcon,
            navigationIconContentDescription = "",
            searchQuery = rememberTextFieldState(),
            hint = "Search in exercise",
            onBackClick = {},

            )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchToolbarNightPreview() {
    AmwTheme {
        AmwSearchAppBar(
            navigationIcon = BackIcon,
            navigationIconContentDescription = "",
            searchQuery = rememberTextFieldState(),
            hint = "Search in exercise",
            onBackClick = {},

            )
    }
}