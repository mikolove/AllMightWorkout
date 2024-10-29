package com.mikolove.auth.domain

data class PasswordValidationState(
    val hasMinLength : Boolean = false
){
    val isValidPassword : Boolean
        get() = hasMinLength
}