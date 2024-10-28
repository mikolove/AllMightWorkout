package com.mikolove.auth.presentation.intro

sealed interface IntroAction{

    data object OnSignInClick : IntroAction

    data object OnSignUpClick : IntroAction
}