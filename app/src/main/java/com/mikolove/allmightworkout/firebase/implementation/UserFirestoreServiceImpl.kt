package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.firebase.mappers.UserNetworkMapper
import com.mikolove.allmightworkout.firebase.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.firebase.model.UserNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.util.DateUtil
import kotlinx.coroutines.tasks.await

class UserFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val userNetworkMapper : UserNetworkMapper,
    private val workoutNetworkMapper: WorkoutNetworkMapper,
    private val exerciseNetworkMapper: ExerciseNetworkMapper,
    private val dateUtil: DateUtil,
)
    : com.mikolove.allmightworkout.firebase.abstraction.UserFirestoreService {

    override suspend fun insertUser(user: User) {

        val entity = userNetworkMapper.mapToEntity(user)
        firebaseAuth.currentUser?.uid?.let {
            firestore
                .collection(USERS_COLLECTION)
                .document(entity.idUser)
                .set(entity)
                .addOnSuccessListener {

                }
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun updateName(name: String, primaryKey: String) {
        firebaseAuth.currentUser?.uid?.let {
            val updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(dateUtil.getCurrentTimestamp())
            firestore
                .collection("users")
                .document(primaryKey)
                .update("name",name
                    ,"updatedAt",updatedAt)
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun getUser(primaryKey: String): User? {

        //printLogD("UserFirestoreImpl","TRy TO GET USER ${primaryKey} ")

        var user : User? = null
        firebaseAuth.currentUser?.uid?.let {

            firestore
                .collection(USERS_COLLECTION)
                .document(primaryKey)
                .get()
                .addOnFailureListener {
                    //printLogD("UserFirestoreImpl","LOG FAILURE"+it.message)
                }
                .await()
                .toObject(UserNetworkEntity::class.java)?.let {
                        user = userNetworkMapper.mapFromEntity(it)
                        //printLogD("UserFirestoreImpl","user on success ${user}")
                }
        }

        //printLogD("UserFirestoreImpl","User returned ${user} ")
        return user
    }

    override suspend fun getUserWithWorkouts(primaryKey: String): User? {
        TODO("Not yet implemented")
    }
}