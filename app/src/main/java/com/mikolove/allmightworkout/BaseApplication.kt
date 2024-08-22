package com.mikolove.allmightworkout

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
@HiltAndroidApp
open class BaseApplication : Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override val workManagerConfiguration: Configuration
        get() =   Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}