package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.user.abstraction.UserNetworkService
import com.mikolove.core.network.firebase.mappers.UserNetworkMapper
import com.mikolove.core.network.firebase.model.UserNetworkEntity
import com.mikolove.core.network.firebase.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await

class UserFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val userNetworkMapper : UserNetworkMapper,
)
    : UserNetworkService {

    override suspend fun upsertUser(user: User) {

        val userId = sessionStorage.get()?.userId ?: return
        val entity = userNetworkMapper.mapToEntity(user)

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .set(entity)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
            }
            .await()
    }

    override suspend fun getUser(primaryKey: String): User? {

        val userId = sessionStorage.get()?.userId ?: return null

        val user = firestore
            .collection(USERS_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener {}
            .await()
            .toObject(UserNetworkEntity::class.java)?.let {
                userNetworkMapper.mapFromEntity(it)
            }

        return user
    }

}