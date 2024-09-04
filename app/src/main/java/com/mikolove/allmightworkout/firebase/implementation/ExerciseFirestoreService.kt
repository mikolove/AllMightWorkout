package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.firebase.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.bodypart.BodyPartFactory
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkService
import kotlinx.coroutines.tasks.await

class ExerciseFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore: FirebaseFirestore,
    private val exerciseNetworkMapper: ExerciseNetworkMapper,
    private val bodyPartFactory: BodyPartFactory

) : ExerciseNetworkService {

    override suspend fun upsertExercise(exercise: Exercise) {
        val userId = sessionStorage.get()?.userId ?: return

        val entity = exerciseNetworkMapper.mapToEntity(exercise)

        val groupIds : List<String> = exercise.bodyPart.mapIndexed{_,bp -> bp.idBodyPart }

        entity.bodyPartIds  = groupIds

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(entity.idExercise)
            .set(entity)
            .addOnFailureListener {
                cLog(it.message)
            }
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
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()

    }

    override suspend fun getExercises(): List<Exercise> {

        val listOfExercise: MutableList<Exercise> = mutableListOf<Exercise>()

        val userId = sessionStorage.get()?.userId ?: return listOf()

        //Get exercises and Sets
        val exerciseNetworkEntities = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObjects(ExerciseNetworkEntity::class.java)

        exerciseNetworkEntities.forEach { entity ->

            val bodyParts : MutableList<BodyPart> = mutableListOf()
            entity.bodyPartIds.forEach { idBp ->
                bodyParts.add(
                    bodyPartFactory.createBodyPart(
                        idBodyPart = idBp,
                        name = "incomplete"
                    )
                )

                val exercise  = exerciseNetworkMapper.mapFromEntity(entity)
                exercise.bodyPart = bodyParts

                listOfExercise.add(exercise)
            }
        }

        return listOfExercise
    }
}