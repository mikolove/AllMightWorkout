package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.Workout
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
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class WorkoutFirestoreServiceImpl
constructor(
    //private val firebaseAuth : FirebaseAuth
    private val firestore : FirebaseFirestore,
    private val workoutNetworkMapper: WorkoutNetworkMapper,
    private val exerciseNetworkMapper: ExerciseNetworkMapper
) : WorkoutFirestoreService{

    override suspend fun insertWorkout(workout: Workout) {

        val entity = workoutNetworkMapper.mapToEntity(workout)
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(entity.idWorkout)
            .set(entity)
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
            .await()
    }

    override suspend fun removeWorkout(id: String) {
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(id)
            .delete()
            .await()
    }

    override suspend fun getWorkouts(): List<Workout> {

        //Get All workouts
         val workoutNetworkEntities = firestore
             .collection(USERS_COLLECTION)
             .document(FIRESTORE_USER_ID)
             .collection(WORKOUTS_COLLECTION)
             .get()
             .await().toObjects(WorkoutNetworkEntity::class.java)

        //Get All exercises
        val exerciseNetworkEntities = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(EXERCISES_COLLECTION)
            .get()
            .await().toObjects(ExerciseNetworkEntity::class.java)

        //Match exercises into workouts
        val workouts : ArrayList<Workout> = ArrayList()
        for(workoutEntity in workoutNetworkEntities){

            //Convert workout
            val workout = workoutNetworkMapper.mapFromEntity(workoutEntity)

            //Filter exercises in workout
            val exercises = exerciseNetworkMapper.entityListToDomainList(

                exerciseNetworkEntities.filter { exerciseNetworkEntity
                -> workoutEntity.exerciseIds.contains(exerciseNetworkEntity.idExercise)
                }
            )

            //Build workout
            workout.exercises = exercises
            workouts.add(workout)
        }

        return workouts
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {

        //Get specific workout
        val workoutNetworkEntity = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(WORKOUTS_COLLECTION)
            .document(primaryKey)
            .get()
            .await().toObject(WorkoutNetworkEntity::class.java)

        return workoutNetworkEntity?.let { wkNetworkEntity ->

            //Get All exercises
            val exerciseNetworkEntities = firestore
                .collection(USERS_COLLECTION)
                .document(FIRESTORE_USER_ID)
                .collection(EXERCISES_COLLECTION)
                .get()
                .await().toObjects(ExerciseNetworkEntity::class.java)

            //Filter exercises in workout
            val exercises = exerciseNetworkMapper.entityListToDomainList(

                exerciseNetworkEntities.filter { exerciseNetworkEntity
                    -> wkNetworkEntity.exerciseIds.contains(exerciseNetworkEntity.idExercise)
                }
            )

            //Build workout
            val workout = workoutNetworkMapper.mapFromEntity(wkNetworkEntity)
            workout.exercises = exercises
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
            .await()

        return totalWorkout
    }

    override suspend fun getDeletedWorkouts(): List<Workout> {
        return firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(REMOVED_WORKOUTS_COLLECTION)
            .get()
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
        }.await()
    }
}