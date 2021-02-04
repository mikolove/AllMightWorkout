package com.mikolove.allmightworkout.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.data.network.abstraction.*
import com.mikolove.allmightworkout.business.data.network.implementation.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.*
import com.mikolove.allmightworkout.framework.datasource.network.implementation.*
import com.mikolove.allmightworkout.framework.datasource.network.mappers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    /*
    Network mapper
     */
    @Singleton
    @Provides
    fun provideBodyPartNetworkMapper() : BodyPartNetworkMapper{
        return BodyPartNetworkMapper()
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeNetworkMapper(bodyPartNetworkMapper:BodyPartNetworkMapper) : WorkoutTypeNetworkMapper{
        return WorkoutTypeNetworkMapper(bodyPartNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideWorkoutNetworkMapper(dateUtil : DateUtil) : WorkoutNetworkMapper{
        return WorkoutNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideExerciseSetNetworkMappedr(dateUtil: DateUtil) : ExerciseSetNetworkMapper{
        return ExerciseSetNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideExerciseNetworkMapper(dateUtil: DateUtil, bodyPartNetworkMapper: BodyPartNetworkMapper, exerciseSetNetworkMapper: ExerciseSetNetworkMapper) : ExerciseNetworkMapper{
        return ExerciseNetworkMapper(dateUtil, bodyPartNetworkMapper ,exerciseSetNetworkMapper )
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetNetworkMapper(dateUtil: DateUtil) : HistoryExerciseSetNetworkMapper{
        return HistoryExerciseSetNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseNetworkMapper(dateUtil: DateUtil, historyExerciseSetNetworkMapper : HistoryExerciseSetNetworkMapper) : HistoryExerciseNetworkMapper{
        return HistoryExerciseNetworkMapper(dateUtil,historyExerciseSetNetworkMapper )
    }

    @Singleton
    @Provides
    fun provideHistoryWorkout(dateUtil: DateUtil, historyExerciseNetworkMapper : HistoryExerciseNetworkMapper) : HistoryWorkoutNetworkMapper{
        return HistoryWorkoutNetworkMapper(dateUtil,historyExerciseNetworkMapper)
    }

    /*
    Firestore service
     */

    @Singleton
    @Provides
    fun provideWorkoutFirestoreService( firebaseAuth : FirebaseAuth, firestore : FirebaseFirestore, workoutNetworkMapper : WorkoutNetworkMapper, exerciseNetworkMapper : ExerciseNetworkMapper ) : WorkoutFirestoreService{
        return WorkoutFirestoreServiceImpl(firebaseAuth,firestore  ,workoutNetworkMapper  ,exerciseNetworkMapper )
    }

    @Singleton
    @Provides
    fun provideExerciseFirestoreService( firebaseAuth : FirebaseAuth, firestore : FirebaseFirestore, exerciseNetworkMapper: ExerciseNetworkMapper ) : ExerciseFirestoreService{
        return ExerciseFirestoreServiceImpl(firebaseAuth,firestore, exerciseNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideExerciseSetFirestoreService( firebaseAuth : FirebaseAuth, firestore : FirebaseFirestore, exerciseSetNetworkMapper: ExerciseSetNetworkMapper ) : ExerciseSetFirestoreService {
        return ExerciseSetFirestoreServiceImpl(firebaseAuth,firestore,exerciseSetNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeFirestoreService(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore, workoutTypeNetworkMapper: WorkoutTypeNetworkMapper) : WorkoutTypeFirestoreService{
        return WorkoutTypeFirestoreServiceImpl(firebaseAuth,firestore,workoutTypeNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutFirestoreService(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore,historyWorkoutNetworkMapper: HistoryWorkoutNetworkMapper,dateUtil : DateUtil) : HistoryWorkoutFirestoreService {
        return HistoryWorkoutFirestoreServiceImpl(firebaseAuth,firestore,historyWorkoutNetworkMapper,dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseFirestoreService(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore, historyExerciseNetworkMapper: HistoryExerciseNetworkMapper) : HistoryExerciseFirestoreService{
        return HistoryExerciseFirestoreServiceImpl(firebaseAuth,firestore,historyExerciseNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetFirestoreService(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore,historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper) : HistoryExerciseSetFirestoreService{
        return HistoryExerciseSetFirestoreServiceImpl(firebaseAuth,firestore, historyExerciseSetNetworkMapper)
    }

    /*
    Network data source
     */

    @Singleton
    @Provides
    fun provideWorkoutTypeNetworkDataSource( workoutTypeFirestoreService: WorkoutTypeFirestoreService) : WorkoutTypeNetworkDataSource{
        return WorkoutTypeNetworkDataSourceImpl(workoutTypeFirestoreService)
    }

    @Singleton
    @Provides
    fun provideWorkoutNetworkDataSource( workoutFirestoreService: WorkoutFirestoreService) : WorkoutNetworkDataSource{
        return WorkoutNetworkDataSourceImpl(workoutFirestoreService)
    }

    @Singleton
    @Provides
    fun provideExerciseNetworkDataSource( exerciseFirestoreService : ExerciseFirestoreService ) : ExerciseNetworkDataSource {
        return ExerciseNetworkDateSourceImpl(exerciseFirestoreService)
    }

    @Singleton
    @Provides
    fun provideExerciseSetNetworkDataSource( exerciseSetFirestoreService: ExerciseSetFirestoreService) : ExerciseSetNetworkDataSource {
        return ExerciseSetNetworkDataSourceImpl(exerciseSetFirestoreService)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutNetworkDataSource ( historyWorkoutFirestoreService : HistoryWorkoutFirestoreService) : HistoryWorkoutNetworkDataSource{
        return HistoryWorkoutNetworkDataSourceImpl(historyWorkoutFirestoreService)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseNetworkDataSource ( historyExerciseFirestoreService : HistoryExerciseFirestoreService) : HistoryExerciseNetworkDataSource{
        return HistoryExerciseNetworkDataSourceImpl(historyExerciseFirestoreService)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetNetworkDataSource ( historyExerciseSetFirestoreService : HistoryExerciseSetFirestoreService) : HistoryExerciseSetNetworkDataSource{
        return HistoryExerciseSetNetworkDataSourceImpl(historyExerciseSetFirestoreService)
    }
}