package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutGroupFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutGroupNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutGroupNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.WORKOUT_GROUPS
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class WorkoutGroupFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val workoutGroupNetworkMapper : WorkoutGroupNetworkMapper,
    private val dateUtil: DateUtil
) : WorkoutGroupFirestoreService{

    override suspend fun getWorkoutGroups(): List<WorkoutGroup> {
        return firebaseAuth.currentUser?.let { currentUser ->
            firestore.collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(WORKOUT_GROUPS)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await().toObjects(WorkoutGroupNetworkEntity::class.java).let{
                    workoutGroupNetworkMapper.entityListToDomainList(it)
                }
        } ?: listOf()
    }

    override suspend fun insertWorkoutGroup(workoutGroup: WorkoutGroup) {
        firebaseAuth.currentUser?.let { currentUser ->
            val entity = workoutGroupNetworkMapper.mapToEntity(workoutGroup)
            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(WORKOUT_GROUPS)
                .document(entity.idWorkoutGroup)
                .set(entity)
                .addOnFailureListener{
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun updateWorkoutGroup(workoutGroup: WorkoutGroup) {
        firebaseAuth.currentUser?.let { currentUser ->
            val entity = workoutGroupNetworkMapper.mapToEntity(workoutGroup)
            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(WORKOUT_GROUPS)
                .document(entity.idWorkoutGroup)
                .update(
                    "name",entity.name,
                    "updatedAt",entity.updatedAt
                )
                .addOnFailureListener{
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun deleteWorkoutGroup(group: WorkoutGroup) {
        firebaseAuth.currentUser?.let { currentUser ->
            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(WORKOUT_GROUPS)
                .document(group.idGroup)
                .delete()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }
    }
}