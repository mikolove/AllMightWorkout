package com.mikolove.allmightworkout.framework.presentation

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
open class BaseApplication : MultiDexApplication(){


    override fun onCreate() {
        super.onCreate()

    }


}