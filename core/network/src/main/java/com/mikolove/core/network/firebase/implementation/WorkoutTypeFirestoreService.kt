package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeNetworkService
import com.mikolove.core.network.firebase.mappers.toBodyPart
import com.mikolove.core.network.firebase.mappers.toWorkoutType
import com.mikolove.core.network.firebase.model.BodyPartNetworkEntity
import com.mikolove.core.network.firebase.model.WorkoutTypeNetworkEntity
import com.mikolove.core.network.util.FirestoreConstants.BODYPART_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.WORKOUT_TYPE_COLLECTION
import kotlinx.coroutines.tasks.await

class WorkoutTypeFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
) : WorkoutTypeNetworkService {

    override suspend fun getWorkoutTypes(): List<WorkoutType> {

        if( sessionStorage.get()?.userId.isNullOrEmpty() ) return listOf()

        val listOfWorkoutType : List<WorkoutType> = firestore
            .collection(WORKOUT_TYPE_COLLECTION)
            .get()
            .await()
            .toObjects(WorkoutTypeNetworkEntity::class.java).let { workoutTypeNetworkEntity ->
                workoutTypeNetworkEntity.map {  it.toWorkoutType() }
            }

        //Get BodyParts for each workoutTypes
        for(workoutType in listOfWorkoutType){

            val bodyParts = firestore
                .collection(WORKOUT_TYPE_COLLECTION)
                .document(workoutType.idWorkoutType)
                .collection(BODYPART_COLLECTION)
                .get()
                .await()
                .toObjects(BodyPartNetworkEntity::class.java).let { bodyPartNetworkEntity ->
                    bodyPartNetworkEntity.map { it.toBodyPart() }
                }

            workoutType.bodyParts = bodyParts

        }

        return listOfWorkoutType
    }
}