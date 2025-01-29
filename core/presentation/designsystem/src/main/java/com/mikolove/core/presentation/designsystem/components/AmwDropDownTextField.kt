@file:OptIn(ExperimentalMaterial3Api::class)

package com.mikolove.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.DropDownIcon
import com.mikolove.core.presentation.designsystem.DropUpIcon

@Composable
fun AmwDropDownTextField(
    state : TextFieldState,
    options : List<String>,
    hint : String,
    title : String?,
    modifier: Modifier = Modifier,
    error: String? = null,
    additionalInfo: String? = null
) {

    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (title != null) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            } else if (additionalInfo != null) {
                Text(
                    text = additionalInfo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.fillMaxWidth(),
            onExpandedChange = { expanded = it },
        ) {
            BasicTextField(
                state = state,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                readOnly = true,
                modifier = Modifier
                    .background(
                        if (isFocused) {
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.05f
                            )
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = if (isFocused) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        },
                    )
                    .padding(12.dp)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    }
                    .menuAnchor(
                        MenuAnchorType.PrimaryNotEditable,
                        true
                        ),
                decorator = { innerBox ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            if(state.text.isEmpty() && !isFocused) {
                                Text(
                                    text = hint,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.4f
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            innerBox()
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector =
                            if(expanded) DropUpIcon
                                else DropDownIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    }
                }


            )
            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = { expanded = false},
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            state.setTextAndPlaceCursorAtEnd(option)
                            expanded = false
                        }
                    )
                }
            }

        }

    }
}

@Preview
@Composable
private fun AmwDropDownTextFieldPreview() {
    AmwTheme {
        AmwDropDownTextField(
            state = rememberTextFieldState(),
            options = listOf("option1", "option2", "option3"),
            hint = "selected",
            title = "Choose one",
            additionalInfo = "Must not be empty",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
