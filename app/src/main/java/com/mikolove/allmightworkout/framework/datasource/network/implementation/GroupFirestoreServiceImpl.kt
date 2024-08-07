package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.GroupFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.GroupNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.GroupNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.WORKOUT_GROUPS
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class GroupFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val groupNetworkMapper : GroupNetworkMapper,
    private val dateUtil: DateUtil
) : GroupFirestoreService{

    override suspend fun getWorkoutGroups(): List<Group> {
        return firebaseAuth.currentUser?.let { currentUser ->
            firestore.collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(WORKOUT_GROUPS)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await().toObjects(GroupNetworkEntity::class.java).let{
                    groupNetworkMapper.entityListToDomainList(it)
                }
        } ?: listOf()
    }

    override suspend fun insertWorkoutGroup(group: Group) {
        firebaseAuth.currentUser?.let { currentUser ->
            val entity = groupNetworkMapper.mapToEntity(group)
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

    override suspend fun updateWorkoutGroup(group: Group) {
        firebaseAuth.currentUser?.let { currentUser ->
            val entity = groupNetworkMapper.mapToEntity(group)
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

    override suspend fun deleteWorkoutGroup(group: Group) {
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