package com.mikolove.auth.presentation.register

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikolove.auth.presentation.AmwAccountManager
import com.mikolove.auth.presentation.R
import com.mikolove.auth.presentation.component.AmwActionButtonSiginWithGoogle
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.CheckIcon
import com.mikolove.core.presentation.designsystem.EmailIcon
import com.mikolove.core.presentation.designsystem.components.AmwActionButton
import com.mikolove.core.presentation.designsystem.components.AmwPasswordTextField
import com.mikolove.core.presentation.designsystem.components.AmwTextField
import com.mikolove.core.presentation.designsystem.components.AuthBackground
import com.mikolove.core.presentation.ui.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val accountManager = remember {
        AmwAccountManager(context as ComponentActivity)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is RegisterEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            RegisterEvent.AskForSaveCredential -> {
                keyboardController?.hide()

                scope.launch {
                    val result = accountManager.saveCredential(
                        username = viewModel.state.email.text.toString(),
                        password = viewModel.state.password.text.toString()
                    )
                    viewModel.onAction(RegisterAction.OnSaveCredentialClick(result))
                }

            }

            is RegisterEvent.RegistrationSuccess -> {
                onSuccessfulRegistration()
            }
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
                RegisterAction.OnSignInClick -> onSignInClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ){

            Text(
                text = stringResource(id = R.string.create_account),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.already_have_an_account) + " ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = stringResource(id = R.string.sign_in),
                            styles = TextLinkStyles(
                                SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            ),
                            linkInteractionListener = { onAction(RegisterAction.OnSignInClick)}
                        ),
                        block = { append(stringResource(id = R.string.sign_in)) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AmwTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if (state.isEmailValid) {
                    CheckIcon
                } else null,
                hint = stringResource(id = R.string.example_email),
                title = stringResource(id = R.string.email),
                modifier = Modifier.fillMaxWidth(),
                additionalInfo = stringResource(id = R.string.must_be_a_valid_email),
                keyboardType = KeyboardType.Email
            )

            AmwPasswordTextField(
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth()
            )

            AmwActionButton(
                text = stringResource(id = R.string.register),
                isLoading = state.isRegistering,
                enabled = state.canRegister,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(RegisterAction.OnSignUpClick)
                }
            )

            /*Text(
                text = stringResource(R.string.or_continue_with),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            AmwActionButtonSiginWithGoogle(
                isLoading = state.isRegisteringGoogle,
                enabled = state.canRegisterGoogle,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                   onAction(RegisterAction.OnSignUpWithGoogleClick)
                }
            )*/
        }
    }

}

@Preview
@Composable
private fun RegisterScreenRootScreenPreview() {
    AmwTheme{
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}