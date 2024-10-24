package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkService
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.network.firebase.mappers.toHistoryWorkout
import com.mikolove.core.network.firebase.mappers.toHistoryWorkoutNetworkEntity
import com.mikolove.core.network.firebase.model.HistoryWorkoutNetworkEntity
import com.mikolove.core.network.util.FirestoreConstants.HISTORY_WORKOUTS_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await

data class AnalyticsFirestoreService(
    private val sessionStorage: SessionStorage,
    private val firestore: FirebaseFirestore
) : AnalyticsNetworkService {

    override suspend fun insertHistoryWorkouts(historyWorkout: HistoryWorkout) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = historyWorkout.toHistoryWorkoutNetworkEntity()

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(entity.idHistoryWorkout)
            .set(entity)
            .await()
    }

    override suspend fun removeHistoryWorkouts(historyWorkout: HistoryWorkout) {

        val userId = sessionStorage.get()?.userId ?: return

        val idToDelete = historyWorkout.idHistoryWorkout

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(idToDelete)
            .delete()
            .await()
    }

    override suspend fun getHistoryWorkouts(): List<HistoryWorkout> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        return firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .get()
            .await()
            .toObjects(HistoryWorkoutNetworkEntity::class.java).map {
                it.toHistoryWorkout()
            }

    }
}