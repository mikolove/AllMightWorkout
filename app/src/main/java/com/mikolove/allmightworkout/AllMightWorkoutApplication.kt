package com.mikolove.allmightworkout

import android.app.Application
import com.mikolove.allmightworkout.di.appModule
import com.mikolove.auth.data.di.authDataModule
import com.mikolove.auth.presentation.di.authViewModule
import com.mikolove.core.data.di.coreDataModule
import com.mikolove.core.database.di.databaseModule
import com.mikolove.core.network.di.networkModule
import com.mikolove.exercise.presentation.di.exerciseViewModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber


class AllMightWorkoutApplication : Application(){

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


        startKoin {
            androidLogger()
            androidContext(this@AllMightWorkoutApplication)
            workManagerFactory()
            modules(
                appModule,
                coreDataModule,
                databaseModule,
                networkModule,
                authDataModule,
                authViewModule,
                exerciseViewModule
            )

        }
    }
}