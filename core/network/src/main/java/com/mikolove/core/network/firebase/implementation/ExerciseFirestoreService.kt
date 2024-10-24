package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkService
import com.mikolove.core.network.firebase.mappers.toBodyPart
import com.mikolove.core.network.firebase.mappers.toExercise
import com.mikolove.core.network.firebase.mappers.toExerciseNetworkEntity
import com.mikolove.core.network.firebase.model.BodyPartNetworkEntity
import com.mikolove.core.network.firebase.model.ExerciseNetworkEntity
import com.mikolove.core.network.util.FirestoreConstants.BODYPART_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await

class ExerciseFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore: FirebaseFirestore,

) : ExerciseNetworkService {

    override suspend fun upsertExercise(exercise: Exercise) {

        val userId = sessionStorage.get()?.userId ?: return
        val entity = exercise.toExerciseNetworkEntity()

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(entity.idExercise)
            .set(entity)
            .await()
    }

    override suspend fun removeExercise(primaryKey: String) {
        val userId = sessionStorage.get()?.userId ?: return
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(primaryKey)
            .delete()
            .await()

    }

    override suspend fun getExercises(): List<Exercise> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        //Get body parts
        val bodyPartsEntities : List<BodyPart> = firestore
            .collectionGroup(BODYPART_COLLECTION)
            .get()
            .await()
            .toObjects(BodyPartNetworkEntity::class.java).let{ bodyPartNetworkEntities ->
                bodyPartNetworkEntities.map { it.toBodyPart() }
            }

        //Get exercises and Sets
        val listOfExercise: List<Exercise> = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .get()
            .await()
            .toObjects(ExerciseNetworkEntity::class.java).let{ networkEntities ->
                networkEntities.map { networkEntity ->
                    val bodyParts = bodyPartsEntities.filter { it.idBodyPart in networkEntity.bodyPartIds }
                    networkEntity.toExercise(bodyParts)
                }
            }

        return listOfExercise
    }
}