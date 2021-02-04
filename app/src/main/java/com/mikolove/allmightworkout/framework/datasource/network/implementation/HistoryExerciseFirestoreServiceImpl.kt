package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_USER_ID
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class HistoryExerciseFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val historyExerciseNetworkMapper: HistoryExerciseNetworkMapper
) : HistoryExerciseFirestoreService{

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ) {
        val entity = historyExerciseNetworkMapper.mapToEntity(historyExercise)

        val historyExerciseEntity = hashMapOf(
            "idHistoryExercise" to entity.idHistoryExercise,
            "name" to entity.name,
            "bodyPart" to entity.bodyPart,
            "workoutType" to entity.workoutType,
            "exerciseType" to entity.exerciseType,
            "startedAt" to entity.startedAt,
            "endedAt" to entity.endedAt,
            "createdAt" to entity.createdAt,
            "updatedAt" to entity.updatedAt
         )
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(idHistoryWorkout)
            .collection(HISTORY_EXERCISES_COLLECTION)
            .document(entity.idHistoryExercise)
            .set(historyExercise)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getHistoryExerciseByHistoryWorkoutId(workoutId: String): List<HistoryExercise> {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(workoutId)
            .collection(HISTORY_EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener{
                cLog(it.message)
            }
            .await().toObjects(HistoryExerciseNetworkEntity::class.java)?.let {
                historyExerciseNetworkMapper.entityListToDomainList(it)
            }
    }

    override suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String): HistoryExercise? {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(idHistoryWorkout)
            .collection(HISTORY_EXERCISES_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener{
                cLog(it.message)
            }
            .await().toObject(HistoryExerciseNetworkEntity::class.java)?.let {
                historyExerciseNetworkMapper.mapFromEntity(it)
            }
    }
}