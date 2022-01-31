package com.mikolove.allmightworkout.framework.presentation.main.loading

data class AccountPreference(
    val accountType : AccountType,
    val email : String?,
    val password : String?
)

enum class AccountType(val value: String) {
    BASIC("basic"),
    GOOGLE("google")
}

fun getAccountTypeFromValue(value: String?): AccountType {
    return when(value){
        AccountType.BASIC.value -> AccountType.BASIC
        AccountType.GOOGLE.value -> AccountType.GOOGLE
        else -> AccountType.BASIC
    }
}