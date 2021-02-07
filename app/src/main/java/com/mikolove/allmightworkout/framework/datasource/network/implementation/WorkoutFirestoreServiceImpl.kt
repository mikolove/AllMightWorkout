package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_USER_ID
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.REMOVED_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class WorkoutFirestoreServiceImpl
constructor(
    private val firebaseAuth : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val workoutNetworkMapper: WorkoutNetworkMapper,
    private val exerciseNetworkMapper: ExerciseNetworkMapper,
    private val dateUtil: DateUtil
) : WorkoutFirestoreService{

    override suspend fun insertWorkout(workout: Workout) {

        val entity = workoutNetworkMapper.mapToEntity(workout)
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(entity.idWorkout)
            .set(entity)
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun updateWorkout(workout: Workout) {
        val entity = workoutNetworkMapper.mapToEntity(workout)
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(entity.idWorkout)
            .update(
                "name" ,entity.name,
                "isActive", entity.isActive,
                "updatedAt", entity.updatedAt
            )
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun removeWorkout(id: String) {
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(id)
            .delete()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getExerciseIdsUpdate(idWorkout: String): String? {
        var exerciseIdsUpdatedAt :String? = null

        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(idWorkout)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(WorkoutNetworkEntity::class.java)?.exerciseIdsUpdatedAt?.let {
                exerciseIdsUpdatedAt = dateUtil.convertFirebaseTimestampToStringData(it)
            }

        return exerciseIdsUpdatedAt
    }

    override suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?) {
        val entityDate = exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToFirebaseTimestamp(it)}
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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

        //Get All workouts
         val workoutNetworkEntities = firestore
             .collection(USERS_COLLECTION)
             .document(FIRESTORE_USER_ID)
             .collection(WORKOUTS_COLLECTION)
             .get()
             .addOnFailureListener {
                 cLog(it.message)
             }
             .await().toObjects(WorkoutNetworkEntity::class.java)

        //Get All exercises
        val exercisesList = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObjects(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.entityListToDomainList(it)
            }

        //Match exercises into workouts
        val workouts : ArrayList<Workout> = ArrayList()
        for(workoutEntity in workoutNetworkEntities){

            //Convert workout
            val workout = workoutNetworkMapper.mapFromEntity(workoutEntity)

            //Filter exercises in workout
            val workoutExerciseList = workoutEntity.exerciseIds?.let { exerciseIds ->
                exercisesList.filter { exerciseIds.contains(it.idExercise) }
            } ?: ArrayList()

            //Build workout
            workout.exercises = workoutExerciseList
            workouts.add(workout)
        }

        return workouts
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {

        //Get specific workout
        val entity = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(WorkoutNetworkEntity::class.java)

        return entity?.let { workoutkNetworkEntity ->

            //Get All exercises
            val exerciseNetworkEntities = firestore
                .collection(USERS_COLLECTION)
                .document(FIRESTORE_USER_ID)
                .collection(EXERCISES_COLLECTION)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await().toObjects(ExerciseNetworkEntity::class.java)?.let {
                    exerciseNetworkMapper.entityListToDomainList(it)
                }

            //Filter exercises in workout
            val workoutExerciseList = workoutkNetworkEntity.exerciseIds?.let { exerciseIds ->
                exerciseNetworkEntities.filter { exerciseIds.contains(it.idExercise) }
            } ?: ArrayList()

            //Build workout
            val workout = workoutNetworkMapper.mapFromEntity(workoutkNetworkEntity)
            workout.exercises = workoutExerciseList
            workout
        }

    }

    override suspend fun getWorkoutTotalNumber(): Int {
        var totalWorkout : Int = 0
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(REMOVED_WORKOUTS_COLLECTION)
            .get()
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await().toObjects(WorkoutNetworkEntity::class.java)?.let {
                workoutNetworkMapper.entityListToDomainList(it)
            }
    }

    override suspend fun insertDeleteWorkout(workout: Workout) {
        val entity = workoutNetworkMapper.mapToEntity(workout)
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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

        if(workouts.size > 500){
            throw Exception("Cannot delete more than 500 workouts at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(REMOVED_WORKOUTS_COLLECTION)

        firestore.runBatch { batch ->
            for(workout in workouts){
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