package com.mikolove.auth.presentation.intro

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.auth.presentation.R
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.components.AmwActionButton
import com.mikolove.core.presentation.designsystem.components.AmwOutlinedActionButton
import com.mikolove.core.presentation.designsystem.components.AuthBackground

@Composable
fun IntroScreenRoot(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
){
    IntroScreen(
        onAction = { action ->
            when(action){
                IntroAction.OnSignUpClick -> onSignUpClick()
                IntroAction.OnSignInClick  -> onSignInClick()
            }
        }
    )

}

@Composable
fun IntroScreen(
    onAction : (IntroAction) -> Unit
){

    AuthBackground {

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            contentAlignment = Alignment.Center){
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 48.dp),
        ) {
            Text(
                text = stringResource(R.string.welcome_to_amw),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.amw_description),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )

            Spacer(modifier = Modifier.height(32.dp))

            AmwOutlinedActionButton(
                text = stringResource(R.string.sign_in),
                isLoading = false,
                onClick = {
                    onAction(IntroAction.OnSignInClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AmwActionButton(
                text = stringResource(R.string.sign_up),
                isLoading = false,
                onClick = {
                    onAction(IntroAction.OnSignUpClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

        }
    }

}

@Preview
@Composable
fun IntroScreenPreview(){
    AmwTheme {
        IntroScreen(
            onAction = {}
        )
    }
}