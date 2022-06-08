package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.UserFireStoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutNetworkMapper

class UserFireStoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val userNetworkMapper : UserNetworkMapper,
    private val workoutNetworkMapper: WorkoutNetworkMapper,
    private val exerciseNetworkMapper: ExerciseNetworkMapper,
    private val dateUtil: DateUtil
)
: UserFireStoreService{

    override suspend fun insertUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateName(name: String, primaryKey: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserWithWorkouts(primaryKey: String): User? {
        TODO("Not yet implemented")
    }
}