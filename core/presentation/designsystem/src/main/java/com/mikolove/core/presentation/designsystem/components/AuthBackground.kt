package com.mikolove.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikolove.core.presentation.designsystem.AmwTheme

@Composable
fun AuthBackground(
    modifier: Modifier = Modifier,
    hasToolbar : Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if(hasToolbar) {
                        Modifier
                    } else {
                        Modifier.systemBarsPadding()
                    }
                )
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun AuthBackgroundPreview(){
    AmwTheme {
        AuthBackground(
            modifier = Modifier.fillMaxSize(),
        ) { }
    }

}