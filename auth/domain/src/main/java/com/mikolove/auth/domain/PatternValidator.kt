package com.mikolove.auth.domain

interface PatternValidator {
    fun matches(value : String) : Boolean
}