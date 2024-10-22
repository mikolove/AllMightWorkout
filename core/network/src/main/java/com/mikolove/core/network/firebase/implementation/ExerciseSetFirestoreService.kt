package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkService

class ExerciseSetFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
) : ExerciseSetNetworkService {


    override suspend fun upsertExerciseSet(
        exerciseSet: ExerciseSet,
        idExercise: String,
        idWorkout: String
    ) {
    }

    override suspend fun removeExerciseSetById(
        primaryKey: String,
        idExercise: String,
        idWorkout: String
    ) {
    }

}