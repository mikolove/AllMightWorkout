package com.mikolove.allmightworkout.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mikolove.allmightworkout.util.Constants.DEBUG
import com.mikolove.allmightworkout.util.Constants.TAG
import timber.log.Timber

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Timber.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        Timber.d("$className: $message")
    }
}

fun cLog(msg :String?){
    msg?.let {
        if(!DEBUG){
            FirebaseCrashlytics.getInstance().log(it)
        }
    }
}