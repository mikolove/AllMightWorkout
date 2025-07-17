package com.mikolove.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmwTextDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (String) -> Unit,
    title : String,
    hint : String,
    modifier : Modifier = Modifier,
){
    val textFieldState = rememberTextFieldState()

    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AmwTextField(
                    state = textFieldState,
                    startIcon = null,
                    endIcon = null,
                    hint = hint,
                    title = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardType = KeyboardType.Text
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    AmwFloatingButton(
                        title = stringResource(R.string.abort),
                    ) {
                        onDismissRequest()
                    }

                    Spacer(modifier = modifier.width(16.dp))

                    AmwFloatingButton(
                        title = stringResource(R.string.confirm),
                        enabled = textFieldState.text.isNotBlank()
                    ) {
                        onConfirmRequest(textFieldState.text.toString())
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun AmwTextDialogPreview(){
    AmwTheme {
        AmwTextDialog(
            onDismissRequest = {},
            onConfirmRequest = {},
            "Title",
            "Please enter a valid email",
        )
    }
}