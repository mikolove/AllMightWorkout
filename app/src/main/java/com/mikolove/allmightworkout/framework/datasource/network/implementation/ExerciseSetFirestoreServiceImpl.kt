package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseSetFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseSetNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_USER_ID
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.EXERCISE_SETS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.REMOVED_EXERCISE_SETS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class ExerciseSetFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper
) : ExerciseSetFirestoreService{

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String) {

        val entity = exerciseSetNetworkMapper.mapToEntity(exerciseSet)

        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .collection(EXERCISE_SETS_COLLECTION)
            .document(entity.idExerciseSet)
            .set(entity)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun updateExerciseSet(
        exerciseSet: ExerciseSet,
        idExercise: String
    ) {
        val entity = exerciseSetNetworkMapper.mapToEntity(exerciseSet)

        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .collection(EXERCISE_SETS_COLLECTION)
            .document(entity.idExerciseSet)
            .update(
                "reps",entity.reps,
                "weight",entity.weight,
                "time",entity.time,
                "restTime",entity.restTime,
                "updateAd",entity.updatedAt
            )
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise: String) {
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .collection(EXERCISE_SETS_COLLECTION)
            .document(primaryKey)
            .delete()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .collection(EXERCISE_SETS_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObject(ExerciseSetNetworkEntity::class.java)?.let {
                exerciseSetNetworkMapper.mapFromEntity(it)
            }
    }

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet>? {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .collection(EXERCISE_SETS_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObjects(ExerciseSetNetworkEntity::class.java)?.let {
                exerciseSetNetworkMapper.entityListToDomainList(it)
            }
    }

    override suspend fun getTotalExercisesSetByExercise(idExercise: String): Int {
        var totalExerciseSet : Int = 0
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .collection(EXERCISE_SETS_COLLECTION)
            .get()
            .addOnSuccessListener { document ->
                totalExerciseSet = document.size()
            }
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
        return totalExerciseSet
    }

    override suspend fun insertDeletedExerciseSet(exerciseSet: ExerciseSet) {
        val entity = exerciseSetNetworkMapper.mapToEntity(exerciseSet)
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(REMOVED_EXERCISE_SETS_COLLECTION)
            .document(entity.idExerciseSet)
            .set(entity)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getDeletedExerciseSets(): List<ExerciseSet> {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(REMOVED_EXERCISE_SETS_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObjects(ExerciseSetNetworkEntity::class.java)?.let {
                exerciseSetNetworkMapper.entityListToDomainList(it)
            }
    }
}