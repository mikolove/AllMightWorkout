package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.UserFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.UserNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.UserNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.allmightworkout.util.printLogD
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
    : UserFirestoreService{

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