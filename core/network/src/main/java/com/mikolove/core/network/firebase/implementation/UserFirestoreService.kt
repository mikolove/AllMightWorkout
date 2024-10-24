package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.user.abstraction.UserNetworkService
import com.mikolove.core.network.firebase.mappers.toUser
import com.mikolove.core.network.firebase.mappers.toUserNetworkEntity
import com.mikolove.core.network.firebase.model.UserNetworkEntity
import com.mikolove.core.network.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await

class UserFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
)
    : UserNetworkService {

    override suspend fun upsertUser(user: User) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = user.toUserNetworkEntity()

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .set(entity)
            .await()
    }

    override suspend fun getUser(primaryKey: String): User? {

        val userId = sessionStorage.get()?.userId ?: return null

        val user = firestore
            .collection(USERS_COLLECTION)
            .document(primaryKey)
            .get()
            .await()
            .toObject(UserNetworkEntity::class.java)?.let {
                it.toUser()
            }

        return user
    }

}