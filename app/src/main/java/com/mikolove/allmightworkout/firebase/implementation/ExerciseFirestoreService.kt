package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.firebase.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkService
import kotlinx.coroutines.tasks.await

class ExerciseFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore: FirebaseFirestore,
    private val exerciseNetworkMapper: ExerciseNetworkMapper

) : ExerciseNetworkService {

    override suspend fun upsertExercise(exercise: Exercise) {
        val userId = sessionStorage.get()?.userId ?: return

        val entity = exerciseNetworkMapper.mapToEntity(exercise)

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

        var listOfExercise = listOf<Exercise>()

        val userId = sessionStorage.get()?.userId ?: return listOf()

        //Get exercises and Sets
        listOfExercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObjects(ExerciseNetworkEntity::class.java).let {
                exerciseNetworkMapper.entityListToDomainList(it)
            }

        return listOfExercise
    }
}