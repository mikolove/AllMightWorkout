package com.mikolove.allmightworkout.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.network.abstraction.*
import com.mikolove.core.network.implementation.*
import com.mikolove.core.network.mappers.*
import com.mikolove.core.data.analytics.HistoryExerciseNetworkDataSourceImpl
import com.mikolove.core.data.analytics.HistoryExerciseSetNetworkDataSourceImpl
import com.mikolove.core.data.analytics.HistoryWorkoutNetworkDataSourceImpl
import com.mikolove.core.data.exercise.ExerciseNetworkDateSourceImpl
import com.mikolove.core.data.exercise.ExerciseSetNetworkDataSourceImpl
import com.mikolove.core.data.user.UserNetworkDataSourceImpl
import com.mikolove.core.data.workout.GroupNetworkDataSourceImpl
import com.mikolove.core.data.workout.WorkoutNetworkDataSourceImpl
import com.mikolove.core.data.workouttype.WorkoutTypeNetworkDataSourceImpl
import com.mikolove.core.domain.analytics.HistoryExerciseNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseSetNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryWorkoutNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.user.UserNetworkDataSource
import com.mikolove.core.domain.workout.GroupNetworkDataSource
import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
import com.mikolove.core.domain.workouttype.WorkoutTypeNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideGroupNetworkMapper(dateUtil: DateUtil) : GroupNetworkMapper{
        return GroupNetworkMapper(dateUtil)
    }
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
    fun provideWorkoutTypeNetworkMapper() : WorkoutTypeNetworkMapper{
        return WorkoutTypeNetworkMapper()
    }

    @Singleton
    @Provides
    fun provideWorkoutNetworkMapper(dateUtil : DateUtil) : WorkoutNetworkMapper{
        return WorkoutNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideExerciseSetNetworkMapper(dateUtil: DateUtil) : ExerciseSetNetworkMapper{
        return ExerciseSetNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideBodyPartExerciseNetworkMapper() : BodyPartExerciseNetworkMapper{
        return BodyPartExerciseNetworkMapper()
    }

    @Singleton
    @Provides
    fun provideExerciseNetworkMapper(dateUtil: DateUtil, bodyPartExerciseNetworkMapper: BodyPartExerciseNetworkMapper, exerciseSetNetworkMapper: ExerciseSetNetworkMapper) : ExerciseNetworkMapper{
        return ExerciseNetworkMapper(dateUtil, bodyPartExerciseNetworkMapper,exerciseSetNetworkMapper )
    }

    @Singleton
    @Provides
    fun provideUserNetworkMapper(dateUtil: DateUtil) : UserNetworkMapper{
        return UserNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetNetworkMapper(dateUtil: DateUtil) : HistoryExerciseSetNetworkMapper{
        return HistoryExerciseSetNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseNetworkMapper(dateUtil: DateUtil) : HistoryExerciseNetworkMapper{
        return HistoryExerciseNetworkMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkout(dateUtil: DateUtil) : HistoryWorkoutNetworkMapper{
        return HistoryWorkoutNetworkMapper(dateUtil)
    }


    /*
    Firestore service
     */

    @Singleton
    @Provides
    fun provideUserFirestoreService( firebaseAuth: FirebaseAuth, firestore : FirebaseFirestore,
                                     userNetworkMapper : UserNetworkMapper,
                                     workoutNetworkMapper: WorkoutNetworkMapper,
                                     exerciseNetworkMapper: ExerciseNetworkMapper,
                                     dateUtil: DateUtil
    ) : com.mikolove.allmightworkout.firebase.abstraction.UserFirestoreService {
        return UserFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            userNetworkMapper,
            workoutNetworkMapper,
            exerciseNetworkMapper,
            dateUtil
        )
    }

    @Singleton
    @Provides
    fun provideWorkoutFirestoreService( firebaseAuth : FirebaseAuth, firestore : FirebaseFirestore, workoutNetworkMapper : WorkoutNetworkMapper, exerciseNetworkMapper : ExerciseNetworkMapper, dateUtil: DateUtil) : com.mikolove.allmightworkout.firebase.abstraction.WorkoutFirestoreService {
        return WorkoutFirestoreServiceImpl(firebaseAuth,firestore  ,workoutNetworkMapper  ,exerciseNetworkMapper, dateUtil )
    }

    @Singleton
    @Provides
    fun provideExerciseFirestoreService( firebaseAuth : FirebaseAuth, firestore : FirebaseFirestore, exerciseNetworkMapper: ExerciseNetworkMapper, exerciseSetNetworkMapper: ExerciseSetNetworkMapper ) : com.mikolove.allmightworkout.firebase.abstraction.ExerciseFirestoreService {
        return com.mikolove.allmightworkout.firebase.implementation.ExerciseFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            exerciseNetworkMapper,
            exerciseSetNetworkMapper
        )
    }

    @Singleton
    @Provides
    fun provideExerciseSetFirestoreService( firebaseAuth : FirebaseAuth, firestore : FirebaseFirestore,
                                            exerciseNetworkMapper: ExerciseNetworkMapper,
                                            exerciseSetNetworkMapper: ExerciseSetNetworkMapper ) : com.mikolove.allmightworkout.firebase.abstraction.ExerciseSetFirestoreService {
        return com.mikolove.allmightworkout.firebase.implementation.ExerciseSetFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            exerciseNetworkMapper,
            exerciseSetNetworkMapper
        )
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeFirestoreService(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore, workoutTypeNetworkMapper: WorkoutTypeNetworkMapper, bodyPartNetworkMapper: BodyPartNetworkMapper) : com.mikolove.allmightworkout.firebase.abstraction.WorkoutTypeFirestoreService {
        return WorkoutTypeFirestoreServiceImpl(firebaseAuth,firestore,workoutTypeNetworkMapper,bodyPartNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutFirestoreService(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore,
                                              historyWorkoutNetworkMapper: HistoryWorkoutNetworkMapper,
                                              historyExerciseNetworkMapper: HistoryExerciseNetworkMapper,
                                              historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper,
                                              dateUtil : DateUtil
    ) : com.mikolove.allmightworkout.firebase.abstraction.HistoryWorkoutFirestoreService {
        return HistoryWorkoutFirestoreServiceImpl(firebaseAuth,firestore,historyWorkoutNetworkMapper,historyExerciseNetworkMapper,historyExerciseSetNetworkMapper,dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseFirestoreService(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore, historyExerciseNetworkMapper: HistoryExerciseNetworkMapper,historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper) : com.mikolove.allmightworkout.firebase.abstraction.HistoryExerciseFirestoreService {
        return com.mikolove.allmightworkout.firebase.implementation.HistoryExerciseFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            historyExerciseNetworkMapper,
            historyExerciseSetNetworkMapper
        )
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetFirestoreService(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore,historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper) : com.mikolove.allmightworkout.firebase.abstraction.HistoryExerciseSetFirestoreService {
        return HistoryExerciseSetFirestoreServiceImpl(firebaseAuth,firestore, historyExerciseSetNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideGroupFirestoreService(firebaseAuth: FirebaseAuth, firestore:FirebaseFirestore, groupNetworkMapper: GroupNetworkMapper, dateUtil: DateUtil) : com.mikolove.allmightworkout.firebase.abstraction.GroupFirestoreService {
        return com.mikolove.allmightworkout.firebase.implementation.GroupFirestoreServiceImpl(
            firebaseAuth,
            firestore,
            groupNetworkMapper,
            dateUtil
        )
    }

    /*
    Network data source
     */

    @Singleton
    @Provides
    fun provideGroupNetworkDataSource(groupFirestoreService: com.mikolove.allmightworkout.firebase.abstraction.GroupFirestoreService) : GroupNetworkDataSource {
        return GroupNetworkDataSourceImpl(groupFirestoreService)
    }
    @Singleton
    @Provides
    fun provideWorkoutTypeNetworkDataSource( workoutTypeFirestoreService: com.mikolove.allmightworkout.firebase.abstraction.WorkoutTypeFirestoreService) : WorkoutTypeNetworkDataSource {
        return WorkoutTypeNetworkDataSourceImpl(workoutTypeFirestoreService)
    }

    @Singleton
    @Provides
    fun provideWorkoutNetworkDataSource( workoutFirestoreService: com.mikolove.allmightworkout.firebase.abstraction.WorkoutFirestoreService) : WorkoutNetworkDataSource {
        return WorkoutNetworkDataSourceImpl(workoutFirestoreService)
    }

    @Singleton
    @Provides
    fun provideExerciseNetworkDataSource( exerciseFirestoreService : com.mikolove.allmightworkout.firebase.abstraction.ExerciseFirestoreService) : ExerciseNetworkDataSource {
        return ExerciseNetworkDateSourceImpl(exerciseFirestoreService)
    }

    @Singleton
    @Provides
    fun provideExerciseSetNetworkDataSource( exerciseSetFirestoreService: com.mikolove.allmightworkout.firebase.abstraction.ExerciseSetFirestoreService) : ExerciseSetNetworkDataSource {
        return ExerciseSetNetworkDataSourceImpl(exerciseSetFirestoreService)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutNetworkDataSource ( historyWorkoutFirestoreService : com.mikolove.allmightworkout.firebase.abstraction.HistoryWorkoutFirestoreService) : HistoryWorkoutNetworkDataSource {
        return HistoryWorkoutNetworkDataSourceImpl(historyWorkoutFirestoreService)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseNetworkDataSource ( historyExerciseFirestoreService : com.mikolove.allmightworkout.firebase.abstraction.HistoryExerciseFirestoreService) : HistoryExerciseNetworkDataSource {
        return HistoryExerciseNetworkDataSourceImpl(historyExerciseFirestoreService)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetNetworkDataSource ( historyExerciseSetFirestoreService : com.mikolove.allmightworkout.firebase.abstraction.HistoryExerciseSetFirestoreService) : HistoryExerciseSetNetworkDataSource {
        return HistoryExerciseSetNetworkDataSourceImpl(historyExerciseSetFirestoreService)
    }

    @Singleton
    @Provides
    fun provideUserNetworkDataSource( userNetworkFireStoreService: com.mikolove.allmightworkout.firebase.abstraction.UserFirestoreService) : UserNetworkDataSource {
        return UserNetworkDataSourceImpl(userNetworkFireStoreService)
    }
}