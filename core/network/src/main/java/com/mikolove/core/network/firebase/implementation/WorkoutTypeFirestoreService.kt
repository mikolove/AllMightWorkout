package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeNetworkService
import com.mikolove.core.network.firebase.mappers.toBodyPart
import com.mikolove.core.network.firebase.mappers.toWorkoutType
import com.mikolove.core.network.firebase.model.BodyPartNetworkEntity
import com.mikolove.core.network.firebase.model.WorkoutTypeNetworkEntity
import com.mikolove.core.network.util.FirestoreConstants.BODYPART_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.WORKOUT_TYPE_COLLECTION
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class WorkoutTypeFirestoreService
constructor(
    private val firestore : FirebaseFirestore,
) : WorkoutTypeNetworkService {

    override suspend fun getWorkoutTypes(): List<WorkoutType> {

        /*  Something is wrong with the following code. Need to get more knowledgee on collectionGroup and security Rules

        val listOfWorkoutTypeTest : List<WorkoutType> = firestore
            .collectionGroup(WORKOUT_TYPE_COLLECTION)
            .get()
            .addOnSuccessListener { snap ->
                Timber.d("snap: $snap")
            }
            .addOnFailureListener { excep ->
                Timber.d("excep: $excep")
            }
            .await()
            .toObjects()

        Timber.d("SNAP BODY $listOfWorkoutTypeTest")*/

        val listOfWorkoutType : List<WorkoutType> = firestore
            .collection(WORKOUT_TYPE_COLLECTION)
            .get()
            .addOnSuccessListener { snap ->
                Timber.d("snap: $snap")
            }
            .addOnFailureListener { excep ->
                Timber.d("excep: $excep")
            }
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
                .addOnSuccessListener { snap ->
                    Timber.d("snap: $snap")
                }
                .addOnFailureListener { excep ->
                    Timber.d("excep: $excep")
                }
                .await()
                .toObjects(BodyPartNetworkEntity::class.java).let { bodyPartNetworkEntity ->
                    bodyPartNetworkEntity.map { it.toBodyPart() }
                }

            workoutType.bodyParts = bodyParts

        }

        return listOfWorkoutType
    }
}