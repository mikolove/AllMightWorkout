package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.abstraction.WorkoutFirestoreService
import com.mikolove.allmightworkout.firebase.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.firebase.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.firebase.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.firebase.model.WorkoutNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.REMOVED_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.workout.Workout
import kotlinx.coroutines.tasks.await
import java.time.ZonedDateTime

class WorkoutFirestoreServiceImpl
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val workoutNetworkMapper: WorkoutNetworkMapper,
    private val exerciseNetworkMapper: ExerciseNetworkMapper,
) : WorkoutFirestoreService {

    override suspend fun insertWorkout(workout: Workout) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = workoutNetworkMapper.mapToEntity(workout)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(entity.idWorkout)
            .set(entity)
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()

    }

    override suspend fun updateWorkout(workout: Workout) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = workoutNetworkMapper.mapToEntity(workout)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(entity.idWorkout)
            .update(
                "name", entity.name,
                "isActive", entity.isActive,
                "updatedAt", entity.updatedAt
            )
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()

    }

    override suspend fun removeWorkout(id: String) {

        val userId = sessionStorage.get()?.userId ?: return

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(id)
            .delete()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()

    }

    override suspend fun getExerciseIdsUpdate(idWorkout: String): ZonedDateTime? {
        var exerciseIdsUpdatedAt :ZonedDateTime? = null
        val userId = sessionStorage.get()?.userId ?: return exerciseIdsUpdatedAt

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(WorkoutNetworkEntity::class.java)?.exerciseIdsUpdatedAt?.let {
                exerciseIdsUpdatedAt = it.toZoneDateTime()
            }

        return exerciseIdsUpdatedAt
    }

    override suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: ZonedDateTime) {

        val userId = sessionStorage.get()?.userId ?: return

        val entityDate = exerciseIdsUpdatedAt.toFirebaseTimestamp()
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .update(
                "exerciseIdsUpdatedAt", entityDate
            )
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()


    }

    override suspend fun getWorkouts(): List<Workout> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        //Get All workouts
        val workoutNetworkEntities: MutableList<WorkoutNetworkEntity> = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObjects(WorkoutNetworkEntity::class.java)

        //Get All exercises
        val exercisesList = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObjects(ExerciseNetworkEntity::class.java).let {
                exerciseNetworkMapper.entityListToDomainList(it)
            }

        //Match exercises into workouts
        val workouts : MutableList<Workout> = mutableListOf()

        for(workoutEntity in workoutNetworkEntities){

            //Convert workout
            val workout = workoutNetworkMapper.mapFromEntity(workoutEntity)

            //Filter exercises in workout
            val workoutExerciseList = workoutEntity.exerciseIds?.let { exerciseIds ->
                exercisesList.filter { exerciseIds.contains(it.idExercise) }
            } ?: listOf()

            //Build workout
            workout.exercises = workoutExerciseList
            workouts.add(workout)
        }

        return workouts
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {

        var workout : Workout? = null

        val userId = sessionStorage.get()?.userId ?: return null

        //Get specific workout
        val entity = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(WorkoutNetworkEntity::class.java)

        entity?.let { workoutkNetworkEntity ->

            //Get All exercises
            val exerciseNetworkEntities = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXERCISES_COLLECTION)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await().toObjects(ExerciseNetworkEntity::class.java).let {
                    exerciseNetworkMapper.entityListToDomainList(it)
                }

            //Filter exercises in workout
            val workoutExerciseList = workoutkNetworkEntity.exerciseIds?.let { exerciseIds ->
                exerciseNetworkEntities.filter { exerciseIds.contains(it.idExercise) }
            } ?: listOf()

            //Build workout
            workout = workoutNetworkMapper.mapFromEntity(workoutkNetworkEntity).apply {
                exercises = workoutExerciseList
            }
        }

        return workout
    }

    override suspend fun getWorkoutTotalNumber(): Int {
        var totalWorkout : Int = 0
        val userId = sessionStorage.get()?.userId ?: return 0

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .get()
            .addOnSuccessListener { document ->
                totalWorkout = document.size()
            }
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()

        return totalWorkout
    }

    override suspend fun getDeletedWorkouts(): List<Workout> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        val listOfWorkout = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(REMOVED_WORKOUTS_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObjects(WorkoutNetworkEntity::class.java).let {
                workoutNetworkMapper.entityListToDomainList(it)
            }

        return listOfWorkout
    }

    override suspend fun insertDeleteWorkout(workout: Workout) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = workoutNetworkMapper.mapToEntity(workout)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(REMOVED_WORKOUTS_COLLECTION)
            .document(entity.idWorkout)
            .set(entity)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()

    }

    override suspend fun insertDeleteWorkouts(workouts: List<Workout>) {

        val userId = sessionStorage.get()?.userId ?: return

        if (workouts.size > 500) {
            throw Exception("Cannot delete more than 500 workouts at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(REMOVED_WORKOUTS_COLLECTION)

        firestore.runBatch { batch ->
            for (workout in workouts) {
                val documentRef = collectionRef.document(workout.idWorkout)
                batch.set(documentRef, workoutNetworkMapper.mapToEntity(workout))
            }
        }
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()


    }
}