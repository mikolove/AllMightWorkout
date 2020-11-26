package com.mikolove.allmightworkout.framework.presentation

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
@HiltAndroidApp
open class BaseApplication : MultiDexApplication(){


    override fun onCreate() {
        super.onCreate()

    }


}