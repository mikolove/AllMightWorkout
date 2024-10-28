package com.mikolove.allmightworkout

import android.app.Application
import com.mikolove.allmightworkout.di.appModule
import com.mikolove.auth.presentation.di.authModule
import com.mikolove.core.data.di.coreDataModule
import com.mikolove.core.database.di.databaseModule
import com.mikolove.core.network.di.networkModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin


class AllMightWorkoutApplication : Application(){

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AllMightWorkoutApplication)
            workManagerFactory()
            modules(
                appModule,
                coreDataModule,
                databaseModule,
                networkModule,
                authModule,
            )

        }
    }
}