package com.mikolove.core.network.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkService
import com.mikolove.core.network.firebase.mappers.toBodyPart
import com.mikolove.core.network.firebase.mappers.toExercise
import com.mikolove.core.network.firebase.mappers.toGroup
import com.mikolove.core.network.firebase.mappers.toWorkout
import com.mikolove.core.network.firebase.mappers.toWorkoutNetworkEntity
import com.mikolove.core.network.firebase.model.BodyPartNetworkEntity
import com.mikolove.core.network.firebase.model.ExerciseNetworkEntity
import com.mikolove.core.network.firebase.model.GroupNetworkEntity
import com.mikolove.core.network.firebase.model.WorkoutNetworkEntity
import com.mikolove.core.network.util.FirestoreConstants.BODYPART_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.WORKOUTS_COLLECTION
import com.mikolove.core.network.util.FirestoreConstants.WORKOUT_GROUPS_COLLECTION
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class WorkoutFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
) : WorkoutNetworkService {

    override suspend fun upsertWorkout(workout: Workout) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = workout.toWorkoutNetworkEntity()

        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .document(entity.idWorkout)
            .set(entity)
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
            .await()

    }

    override suspend fun getWorkouts(): List<Workout> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        //Get body parts
        val bodyPartEntities : List<BodyPart> = firestore
            .collectionGroup(BODYPART_COLLECTION)
            .get()
            .await()
            .toObjects(BodyPartNetworkEntity::class.java).let{ bodyPartNetworkEntities ->
                bodyPartNetworkEntities.map { it.toBodyPart() }
            }

        //Get Exercises
        val exerciseEntities : List<Exercise> = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .get()
            .await()
            .toObjects(ExerciseNetworkEntity::class.java).let { exerciseEntities ->
                exerciseEntities.map { it.toExercise(bodyPartEntities) }
            }

        //Get Group
        val groupEntities : List<Group> = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUT_GROUPS_COLLECTION)
            .get()
            .await()
            .toObjects(GroupNetworkEntity::class.java).let { groupEntities ->
                groupEntities.map { it.toGroup() }
            }


        //Get All workouts
        val workouts : List<Workout> = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .get()
            .await()
            .toObjects(WorkoutNetworkEntity::class.java).let { workoutNetworkEntities ->
                workoutNetworkEntities.map {
                    workoutNetworkEntity ->
                        val groups = groupEntities.filter { it.idGroup in workoutNetworkEntity.groupIds }
                        val exercises = exerciseEntities.filter { it.idExercise in workoutNetworkEntity.exerciseIds }
                        workoutNetworkEntity.toWorkout(exercises,groups)
                }
            }

        return workouts
    }




}