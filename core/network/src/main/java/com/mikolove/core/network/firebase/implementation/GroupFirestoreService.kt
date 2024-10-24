package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.workout.abstraction.GroupNetworkService
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.network.firebase.mappers.toGroup
import com.mikolove.core.network.firebase.mappers.toGroupNetworkEntity
import com.mikolove.core.network.firebase.model.GroupNetworkEntity
import com.mikolove.core.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.WORKOUT_GROUPS_COLLECTION
import kotlinx.coroutines.tasks.await

class GroupFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
) : GroupNetworkService {

    override suspend fun getWorkoutGroups(): List<Group> {

        val userId = sessionStorage.get()?.userId ?: return listOf()
        return firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUT_GROUPS_COLLECTION)
            .get()
            .await()
            .toObjects(GroupNetworkEntity::class.java).let{ groupNetworkEntities ->
                groupNetworkEntities.map {  it.toGroup() }
            }

    }

    override suspend fun upsertGroup(group: Group) {
        val userId = sessionStorage.get()?.userId ?: return
        val entity = group.toGroupNetworkEntity()
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUT_GROUPS_COLLECTION)
            .document(entity.idWorkoutGroup)
            .set(entity)
            .await()
    }

    override suspend fun deleteWorkoutGroup(idGroup: String) {
        val userId = sessionStorage.get()?.userId ?: return
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUT_GROUPS_COLLECTION)
            .document(idGroup)
            .delete()
            .await()
    }
}