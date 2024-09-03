package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.firebase.mappers.ExerciseSetNetworkMapper
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkService

class ExerciseSetFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val exerciseNetworkMapper : ExerciseNetworkMapper,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper
) : ExerciseSetNetworkService {


    override suspend fun upsertExerciseSet(
        exerciseSet: ExerciseSet,
        idExercise: String,
        idWorkout: String
    ) {
        /*val userId = sessionStorage.get()?.userId ?: return

        val entity = exerciseSetNetworkMapper.mapToEntity(exerciseSet)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .update("sets" , FieldValue.arrayUnion(entity))
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()*/
    }

    override suspend fun removeExerciseSetById(
        primaryKey: String,
        idExercise: String,
        idWorkout: String
    ) {
        /*TODO("Not yet implemented")*/
    }

}