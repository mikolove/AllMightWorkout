package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.abstraction.HistoryWorkoutFirestoreService
import com.mikolove.allmightworkout.firebase.mappers.HistoryWorkoutNetworkMapper
import com.mikolove.allmightworkout.firebase.model.HistoryWorkoutNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.HISTORY_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.auth.SessionStorage
import kotlinx.coroutines.tasks.await
import java.time.ZonedDateTime

class HistoryWorkoutFirestoreServiceImpl
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val historyWorkoutNetworkMapper: HistoryWorkoutNetworkMapper,
) : HistoryWorkoutFirestoreService {

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = historyWorkoutNetworkMapper.mapToEntity(historyWorkout)

        val historyWorkoutEntity = hashMapOf(
            "name" to entity.name,
            "startedAt" to entity.startedAt,
            "endedAt" to entity.endedAt,
            "createdAt" to entity.createdAt,
        )
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(entity.idHistoryWorkout)
            .set(historyWorkoutEntity)
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getLastHistoryWorkout(): List<HistoryWorkout> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        val  historyWorkouts = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(HISTORY_WORKOUTS_COLLECTION)
                .whereGreaterThan("createdAt", ZonedDateTime.now().minusMonths(3))
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
                .toObjects(HistoryWorkoutNetworkEntity::class.java).let {
                    historyWorkoutNetworkMapper.entityListToDomainList(it)
                }

        return historyWorkouts
    }

    override suspend fun getHistoryWorkout(): List<HistoryWorkout> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        val  historyWorkouts = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(HISTORY_WORKOUTS_COLLECTION)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
                .toObjects(HistoryWorkoutNetworkEntity::class.java).let {
                    historyWorkoutNetworkMapper.entityListToDomainList(it)
                }

        return historyWorkouts
    }

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout? {

        val userId = sessionStorage.get()?.userId ?: return null

        val  historyWorkout = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(HISTORY_WORKOUTS_COLLECTION)
                .document(primaryKey)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
                .toObject(HistoryWorkoutNetworkEntity::class.java)?.let {
                    historyWorkoutNetworkMapper.mapFromEntity(it)
                }

        return historyWorkout
    }
}