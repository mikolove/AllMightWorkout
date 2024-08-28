package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.abstraction.UserFirestoreService
import com.mikolove.allmightworkout.firebase.mappers.UserNetworkMapper
import com.mikolove.allmightworkout.firebase.model.UserNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.user.User
import kotlinx.coroutines.tasks.await
import java.time.ZonedDateTime

class UserFirestoreServiceImpl
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val userNetworkMapper : UserNetworkMapper,
)
    : UserFirestoreService {

    override suspend fun insertUser(user: User) {

        val userId = sessionStorage.get()?.userId ?: return
        val entity = userNetworkMapper.mapToEntity(user)

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .set(entity)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun updateName(name: String) {
        val userId = sessionStorage.get()?.userId ?: return
        val updatedAt = ZonedDateTime.now().toFirebaseTimestamp()
        firestore
            .collection("users")
            .document(userId)
            .update(
                "name",name
                ,"updatedAt",updatedAt)
            .addOnFailureListener {
                cLog(it.message)
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