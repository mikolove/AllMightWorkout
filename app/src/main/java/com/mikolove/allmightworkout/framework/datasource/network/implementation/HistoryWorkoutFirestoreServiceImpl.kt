package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryWorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryWorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryWorkoutNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_USER_ID
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class HistoryWorkoutFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val historyWorkoutNetworkMapper: HistoryWorkoutNetworkMapper,
    private val dateUtil: DateUtil
) : HistoryWorkoutFirestoreService{

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) {
        val entity = historyWorkoutNetworkMapper.mapToEntity(historyWorkout)

        val historyWorkoutEntity = hashMapOf(
            "idHistoryWorkout" to entity.idHistoryWorkout,
            "name" to entity.name,
            "startedAt" to entity.startedAt,
            "endedAt" to entity.endedAt,
            "createdAt" to entity.createdAt,
            "updatedAt" to entity.updatedAt
        )
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(entity.idHistoryWorkout)
            .set(historyWorkout)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getLastHistoryWorkout(): List<HistoryWorkout>? {

        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .whereGreaterThan("createdAd",dateUtil.getCurrentDateLessMonth(3))
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
            .toObjects(HistoryWorkoutNetworkEntity::class.java)?.let {
                historyWorkoutNetworkMapper.entityListToDomainList(it)
            }
    }

    override suspend fun getHistoryWorkout(): List<HistoryWorkout> {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
            .toObjects(HistoryWorkoutNetworkEntity::class.java)?.let {
                historyWorkoutNetworkMapper.entityListToDomainList(it)
            }
    }

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout? {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
            .toObject(HistoryWorkoutNetworkEntity::class.java)?.let {
                historyWorkoutNetworkMapper.mapFromEntity(it)
            }
    }
}