package com.mikolove.auth.presentation.login

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikolove.auth.presentation.AmwAccountManager
import com.mikolove.auth.presentation.R
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.EmailIcon
import com.mikolove.core.presentation.designsystem.components.AmwActionButton
import com.mikolove.core.presentation.designsystem.components.AmwPasswordTextField
import com.mikolove.core.presentation.designsystem.components.AmwTextField
import com.mikolove.core.presentation.designsystem.components.Background
import com.mikolove.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onSignUpClick : () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val accountManager = remember {
        AmwAccountManager(context as ComponentActivity)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            LoginEvent.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.youre_logged_in,
                    Toast.LENGTH_LONG
                ).show()

                onLoginSuccess()
            }
        }
    }

    LaunchedEffect(key1 = true) {
        val result = accountManager.loadCredential()
        viewModel.onAction(LoginAction.OnUseCredentialsClick(result))
    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
                LoginAction.OnSignUpClick ->onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {

    Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(id = R.string.welcome_sign_in),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(id = R.string.sign_in_welcome_text),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)


            AmwTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = null,
                hint = stringResource(id = R.string.example_email) ,
                keyboardType = KeyboardType.Email,
                title = stringResource(id = R.string.email),
                modifier= Modifier.fillMaxWidth()
            )
            AmwPasswordTextField(
                state =state.password ,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth()
            )

            AmwActionButton(
                text = stringResource(id = R.string.sign_in),
                isLoading =state.isLoggingIn,
                enabled = state.canLogin && !state.isLoggingIn,
                onClick = {
                    onAction(LoginAction.OnSignInClick)
                })

            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.dont_have_an_account) + " ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = stringResource(id = R.string.sign_up),
                            styles = TextLinkStyles(
                                SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            ),
                            linkInteractionListener = { onAction(LoginAction.OnSignUpClick)}
                        ),
                        block = { append(stringResource(id = R.string.sign_up)) }
                    )
                }
            )
        }
    }

}

@Preview
@Composable
private fun LoginScreenPreview() {
    AmwTheme  {

        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}