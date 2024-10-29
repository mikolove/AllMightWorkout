package com.mikolove.auth.data

import com.mikolove.auth.domain.PatternValidator
import android.util.Patterns

object EmailPatternValidator  : PatternValidator{

    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}