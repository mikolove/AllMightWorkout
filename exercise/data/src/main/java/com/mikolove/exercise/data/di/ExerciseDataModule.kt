package com.mikolove.exercise.data.di

import com.mikolove.core.data.workers.SyncExerciseScheduler
import com.mikolove.exercise.data.CreateExerciseWorker
import com.mikolove.exercise.data.DeleteExerciseWorker
import com.mikolove.exercise.data.FetchExerciseWorker
import com.mikolove.exercise.data.OfflineFirstExerciseRepository
import com.mikolove.exercise.data.SyncExerciseWorkerScheduler
import com.mikolove.exercise.domain.ExerciseRepository
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val exerciseDataModule = module {

    workerOf(::CreateExerciseWorker)
    workerOf(::FetchExerciseWorker)
    workerOf(::DeleteExerciseWorker)

    singleOf(::SyncExerciseWorkerScheduler).bind<SyncExerciseScheduler>()
    singleOf(::OfflineFirstExerciseRepository).bind<ExerciseRepository>()
}