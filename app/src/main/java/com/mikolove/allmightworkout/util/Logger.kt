package com.mikolove.allmightworkout.util

import android.util.Log
import com.mikolove.allmightworkout.util.Constants.DEBUG
import com.mikolove.allmightworkout.util.Constants.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}