package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseSetFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseSetNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_EXERCISES_SETS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class HistoryExerciseSetFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper
) : HistoryExerciseSetFirestoreService{

    override suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        historyExerciseId: String,
        historyWorkoutId: String
    ) {

        firebaseAuth.currentUser?.let { currentUser ->

            val entity = historyExerciseSetNetworkMapper.mapToEntity(historyExerciseSet)

            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(HISTORY_WORKOUTS_COLLECTION)
                .document(historyWorkoutId)
                .collection(HISTORY_EXERCISES_COLLECTION)
                .document(historyExerciseId)
                .collection(HISTORY_EXERCISES_SETS_COLLECTION)
                .document(entity.idHistoryExerciseSet)
                .set(entity)
                .addOnFailureListener {
                    // send error reports to Firebase Crashlytics
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun getHistoryExerciseSetsByHistoryExerciseId(
        idHistoryExerciseId: String,
        idHistoryWorkoutId: String
    ): List<HistoryExerciseSet> {

        var listOfSet = listOf<HistoryExerciseSet>()

        firebaseAuth.currentUser?.let { currentUser ->

            listOfSet = firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(HISTORY_WORKOUTS_COLLECTION)
                .document(idHistoryWorkoutId)
                .collection(HISTORY_EXERCISES_COLLECTION)
                .document(idHistoryExerciseId)
                .collection(HISTORY_EXERCISES_SETS_COLLECTION)
                .get()
                .addOnFailureListener {
                    // send error reports to Firebase Crashlytics
                    cLog(it.message)
                }
                .await().toObjects(HistoryExerciseSetNetworkEntity::class.java).let {
                    historyExerciseSetNetworkMapper.entityListToDomainList(it)
                }
        }

        return listOfSet
    }
}