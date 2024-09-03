package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.mappers.GroupNetworkMapper
import com.mikolove.allmightworkout.firebase.model.GroupNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.WORKOUT_GROUPS
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.workout.abstraction.GroupNetworkService
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.workout.Group
import kotlinx.coroutines.tasks.await

class GroupFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val groupNetworkMapper : GroupNetworkMapper,
) : GroupNetworkService {

    override suspend fun getWorkoutGroups(): List<Group> {

        val userId = sessionStorage.get()?.userId ?: return listOf()
        return firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUT_GROUPS)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObjects(GroupNetworkEntity::class.java).let{
                groupNetworkMapper.entityListToDomainList(it)
            }

    }

    override suspend fun upsertGroup(group: Group) {
        val userId = sessionStorage.get()?.userId ?: return
        val entity = groupNetworkMapper.mapToEntity(group)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUT_GROUPS)
            .document(entity.idWorkoutGroup)
            .set(entity)
            .addOnFailureListener{
                cLog(it.message)
            }
            .await()
    }

    override suspend fun deleteWorkoutGroup(idGroup: String) {
        val userId = sessionStorage.get()?.userId ?: return
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUT_GROUPS)
            .document(idGroup)
            .delete()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }
}