package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.REMOVED_EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ExerciseFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val exerciseNetworkMapper: ExerciseNetworkMapper,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper,

    ) : ExerciseFirestoreService{

    override suspend fun insertExercise(exercise: Exercise) {

        firebaseAuth.currentUser?.let { currentUser ->

            val entity = exerciseNetworkMapper.mapToEntity(exercise)

            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(EXERCISES_COLLECTION)
                .document(entity.idExercise)
                .set(entity)
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun updateExercise(exercise: Exercise) {

        firebaseAuth.currentUser?.let { currentUser ->

            val entity = exerciseNetworkMapper.mapToEntity(exercise)
            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
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
    }

    override suspend fun removeExerciseById(primaryKey: String) {

        firebaseAuth.currentUser?.let { currentUser ->

            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(EXERCISES_COLLECTION)
                .document(primaryKey)
                .delete()
                .addOnFailureListener {
                    // send error reports to Firebase Crashlytics
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun getExercises(): List<Exercise> {

        var listOfExercise = listOf<Exercise>()

        firebaseAuth.currentUser?.let { currentUser ->

            //Get exercises and Sets
            listOfExercise = firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(EXERCISES_COLLECTION)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
                .toObjects(ExerciseNetworkEntity::class.java).let {
                    exerciseNetworkMapper.entityListToDomainList(it)
                }
        }

        return listOfExercise
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise? {

        var exercise : Exercise? = null

        firebaseAuth.currentUser?.let { currentUser ->

            //Get exercise
            exercise = firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(EXERCISES_COLLECTION)
                .document(primaryKey)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                    exerciseNetworkMapper.mapFromEntity(it)
                }
        }

        return exercise
    }

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? {

        //Get workout it contains exercise list
        var exercises = mutableListOf<Exercise>()

        firebaseAuth.currentUser?.let { currentUser ->

        firestore
            .collection(USERS_COLLECTION)
            .document(currentUser.uid)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(WorkoutNetworkEntity::class.java)?.let { workoutNetworkEntity ->
                workoutNetworkEntity.exerciseIds?.forEach {  idExercise ->
                    firestore
                        .collection(USERS_COLLECTION)
                        .document(currentUser.uid)
                        .collection(EXERCISES_COLLECTION)
                        .document(idExercise)
                        .get()
                        .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                            exercises.add(exerciseNetworkMapper.mapFromEntity(it))
                        }
                }
            }
        }
        return exercises
    }

    override suspend fun getTotalExercises(): Int {
        var totalExercise : Int = 0

        firebaseAuth.currentUser?.let { currentUser ->

            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(EXERCISES_COLLECTION)
                .get()
                .addOnSuccessListener { document ->
                    totalExercise = document.size()
                }
                .addOnFailureListener {
                    // send error reports to Firebase Crashlytics
                    cLog(it.message)
                }
                .await()
        }

        return totalExercise
    }

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String) {

        firebaseAuth.currentUser?.let { currentUser ->

            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(WORKOUTS_COLLECTION)
                .document(idWorkout)
                .update("exerciseIds", FieldValue.arrayUnion(idExercise))
                .addOnFailureListener {
                    // send error reports to Firebase Crashlytics
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String) {

        firebaseAuth.currentUser?.let { currentUser ->

        firestore
            .collection(USERS_COLLECTION)
            .document(currentUser.uid)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .update("exerciseIds",FieldValue.arrayRemove(idExercise))
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
        }
    }

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int {
        var isExerciseInWorkout  = 0

        firebaseAuth.currentUser?.let { currentUser ->

            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
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
        }

        return isExerciseInWorkout
    }

    override suspend fun getDeletedExercises(): List<Exercise> {
        var listOfExercise = listOf<Exercise>()

        firebaseAuth.currentUser?.let { currentUser ->

        listOfExercise = firestore
            .collection(USERS_COLLECTION)
            .document(currentUser.uid)
            .collection(REMOVED_EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObjects(ExerciseNetworkEntity::class.java).let {
                exerciseNetworkMapper.entityListToDomainList(it)
            }
        }
        return listOfExercise
    }

    override suspend fun insertDeletedExercise(exercise: Exercise) {

        firebaseAuth.currentUser?.let { currentUser ->

            val entity = exerciseNetworkMapper.mapToEntity(exercise)
            firestore
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .collection(REMOVED_EXERCISES_COLLECTION)
                .document(entity.idExercise)
                .set(entity)
                .addOnFailureListener {
                    // send error reports to Firebase Crashlytics
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun insertDeletedExercises(exercises: List<Exercise>) {

        firebaseAuth.currentUser?.let { currentUser ->

        if(exercises.size > 500){
            throw Exception("Cannot delete more than 500 exercises at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(USERS_COLLECTION)
            .document(currentUser.uid)
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
}