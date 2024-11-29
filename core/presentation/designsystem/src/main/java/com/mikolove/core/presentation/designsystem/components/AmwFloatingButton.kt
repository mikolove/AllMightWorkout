package com.mikolove.core.presentation.designsystem.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mikolove.core.presentation.designsystem.AmwTheme

@Composable
fun AmwFloatingButton(
    modifier : Modifier = Modifier,
    title : String,
    enabled : Boolean = true,
    onClick : () -> Unit
    ){

    ElevatedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier.height(IntrinsicSize.Min)
    ){
        Text(
            text = title,
            modifier = Modifier,
            fontWeight = FontWeight.Medium
        )
    }

}

@Preview
@Composable
fun AmwFloatingButtonPreview(){
    AmwTheme {
        AmwFloatingButton(
            title = "Test preview text",
            onClick = {}
        )
    }
}