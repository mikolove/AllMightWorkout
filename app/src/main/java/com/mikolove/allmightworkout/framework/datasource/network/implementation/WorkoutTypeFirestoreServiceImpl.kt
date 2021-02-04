package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutTypeFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutTypeNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutTypeNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.WORKOUT_TYPE_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class WorkoutTypeFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val workoutTypeNetworkMapper: WorkoutTypeNetworkMapper
) : WorkoutTypeFirestoreService {

    override suspend fun getWorkoutTypes(): List<WorkoutType> {
        return firestore
            .collection(WORKOUT_TYPE_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObjects(WorkoutTypeNetworkEntity::class.java)?.let {
                workoutTypeNetworkMapper.entityListToDomainList(it)
            }
    }
}