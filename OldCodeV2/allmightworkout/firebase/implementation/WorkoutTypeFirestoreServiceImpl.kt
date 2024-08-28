package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.abstraction.WorkoutTypeFirestoreService
import com.mikolove.allmightworkout.firebase.mappers.BodyPartNetworkMapper
import com.mikolove.allmightworkout.firebase.mappers.WorkoutTypeNetworkMapper
import com.mikolove.allmightworkout.firebase.model.BodyPartNetworkEntity
import com.mikolove.allmightworkout.firebase.model.WorkoutTypeNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.BODYPART_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.WORKOUT_TYPE_COLLECTION
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.auth.SessionStorage
import kotlinx.coroutines.tasks.await

class WorkoutTypeFirestoreServiceImpl
constructor(
    private val firestore : FirebaseFirestore,
    private val workoutTypeNetworkMapper: WorkoutTypeNetworkMapper,
    private val bodyPartNetworkMapper: BodyPartNetworkMapper
) : WorkoutTypeFirestoreService {

    override suspend fun getWorkoutTypes(): List<WorkoutType> {

        var listOfWorkoutType : List<WorkoutType> = ArrayList()

        //Get WorkoutTypes
        listOfWorkoutType = firestore
            .collection(WORKOUT_TYPE_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObjects(WorkoutTypeNetworkEntity::class.java).let {
                workoutTypeNetworkMapper.entityListToDomainList(it)
            }

        //Get BodyParts for each workoutTypes
        for(workoutType in listOfWorkoutType){

            val bodyParts = firestore
                .collection(WORKOUT_TYPE_COLLECTION)
                .document(workoutType.idWorkoutType)
                .collection(BODYPART_COLLECTION)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
                .toObjects(BodyPartNetworkEntity::class.java).let {
                    bodyPartNetworkMapper.entityListToDomainList(it)
                }

            workoutType.bodyParts = bodyParts

        }

        return listOfWorkoutType
    }
}