package com.mikolove.core.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutTypeFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.BodyPartNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutTypeNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.BodyPartNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutTypeNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.BODYPART_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.WORKOUT_TYPE_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class WorkoutTypeFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val workoutTypeNetworkMapper: WorkoutTypeNetworkMapper,
    private val bodyPartNetworkMapper: BodyPartNetworkMapper
) : WorkoutTypeFirestoreService {

    override suspend fun getWorkoutTypes(): List<WorkoutType> {
        var listOfWorkoutType : List<WorkoutType> = ArrayList()

        firebaseAuth.currentUser?.let{

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

        }

        return listOfWorkoutType
    }
}