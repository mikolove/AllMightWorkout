package com.mikolove.auth.domain

class UserDataValidator (
    private val patternValidator: PatternValidator
) {
    fun isValidEmail(email : String) : Boolean{
        return patternValidator.matches(email)
    }

    fun validatePassword(password : String) : PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH

        return PasswordValidationState(
            hasMinLength = hasMinLength
        )
    }

    companion object{
        const val MIN_PASSWORD_LENGTH = 6
    }
}