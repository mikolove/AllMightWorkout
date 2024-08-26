package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.abstraction.ExerciseFirestoreService
import com.mikolove.allmightworkout.firebase.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.firebase.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.firebase.model.WorkoutNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.REMOVED_EXERCISES_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.Exercise
import kotlinx.coroutines.tasks.await

class ExerciseFirestoreServiceImpl
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore: FirebaseFirestore,
    private val exerciseNetworkMapper: ExerciseNetworkMapper

) : ExerciseFirestoreService {

    override suspend fun insertExercise(exercise: Exercise) {

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

    override suspend fun updateExercise(exercise: Exercise) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = exerciseNetworkMapper.mapToEntity(exercise)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(entity.idExercise)
            .update(
                "name", entity.name,
                "bodyPart", entity.bodyPart,
                "exerciseType", entity.exerciseType,
                "isActive", entity.isActive,
                "updatedAt", entity.updatedAt
            )
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()

    }

    override suspend fun removeExerciseById(primaryKey: String) {

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

    override suspend fun getExerciseById(primaryKey: String): Exercise? {

        val userId = sessionStorage.get()?.userId ?: return null

        //Get exercise
        val  exercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.mapFromEntity(it)
            }

        return exercise
    }

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? {

        //Get workout it contains exercise list
        val userId = sessionStorage.get()?.userId ?: return  listOf()
        val exercises = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObject(WorkoutNetworkEntity::class.java)?.let { workoutNetworkEntity ->
                workoutNetworkEntity.exerciseIds?.let{  ids ->
                    firestore
                        .collection(USERS_COLLECTION)
                        .document(userId)
                        .collection(EXERCISES_COLLECTION)
                        .whereIn(FieldPath.documentId(),ids)
                        .get()
                        .await()
                        .toObjects(ExerciseNetworkEntity::class.java).let {
                            exerciseNetworkMapper.entityListToDomainList(it)
                        }
                }
            }

        return exercises
    }

    override suspend fun getTotalExercises(): Int {

        val userId = sessionStorage.get()?.userId ?: return  0
        var totalExercise : Int = 0
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .count()
            .get(AggregateSource.SERVER)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    totalExercise = task.result.count.toInt()
                }
            }
            .await()

        return totalExercise
    }

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String) {

        val userId = sessionStorage.get()?.userId ?: return

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .update("exerciseIds", FieldValue.arrayUnion(idExercise))
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String) {

        val userId = sessionStorage.get()?.userId ?: return

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .update("exerciseIds",FieldValue.arrayRemove(idExercise))
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()

    }

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int {

        val userId = sessionStorage.get()?.userId ?: return 0

        var isExerciseInWorkout  = 0
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObject(WorkoutNetworkEntity::class.java)?.let { workoutNetworkEntity ->
                workoutNetworkEntity.exerciseIds?.let {
                    if (idExercise in it)
                        isExerciseInWorkout = 1
                }
            }

        return isExerciseInWorkout
    }

    override suspend fun getDeletedExercises(): List<Exercise> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        val listOfExercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(REMOVED_EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObjects(ExerciseNetworkEntity::class.java).let {
                exerciseNetworkMapper.entityListToDomainList(it)
            }

        return listOfExercise
    }

    override suspend fun insertDeletedExercise(exercise: Exercise) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = exerciseNetworkMapper.mapToEntity(exercise)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(REMOVED_EXERCISES_COLLECTION)
            .document(entity.idExercise)
            .set(entity)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()

    }

    override suspend fun insertDeletedExercises(exercises: List<Exercise>) {

        val userId = sessionStorage.get()?.userId ?: return

        if(exercises.size > 500){
            throw Exception("Cannot delete more than 500 exercises at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(REMOVED_EXERCISES_COLLECTION)

        firestore.runBatch { batch ->
            for(exercise in exercises){
                val documentRef = collectionRef.document(exercise.idExercise)
                batch.set(documentRef, exerciseNetworkMapper.mapToEntity(exercise))
            }
        }
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()

    }
}