package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.mappers.ExerciseSetNetworkMapper
import com.mikolove.allmightworkout.firebase.mappers.WorkoutNetworkMapper
import com.mikolove.allmightworkout.firebase.model.ExerciseSetNetworkEntity
import com.mikolove.allmightworkout.firebase.model.WorkoutNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.GroupFactory
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkService
import kotlinx.coroutines.tasks.await

class WorkoutFirestoreService
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val workoutNetworkMapper: WorkoutNetworkMapper,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper,
    private val exerciseFactory: ExerciseFactory,
    private val groupFactory: GroupFactory
) : WorkoutNetworkService {

    override suspend fun upsertWorkout(workout: Workout) {

        val userId = sessionStorage.get()?.userId ?: return

        var entity = workoutNetworkMapper.mapToEntity(workout)

        val mapOfExerciseWithSet : Map<String,List<ExerciseSetNetworkEntity>> =
            workout.exercises.associate{ exercise ->
                exercise.idExercise to exerciseSetNetworkMapper.domainListToEntityList(exercise.sets)
            }

        val listOfGroup : List<String> = workout.groups.mapIndexed { _, group -> group.idGroup }

        entity = entity.copy(exerciseIdWithSet = mapOfExerciseWithSet, groupIds = listOfGroup)

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

    override suspend fun getWorkouts(): List<Workout> {

        val userId = sessionStorage.get()?.userId ?: return listOf()

        //Get All workouts
        val workoutNetworkEntities : List<WorkoutNetworkEntity> = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(WORKOUTS_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObjects(WorkoutNetworkEntity::class.java)

        //Complete basic exercise and group only with IDS
        val workouts : MutableList<Workout> = mutableListOf()
        workoutNetworkEntities.forEach { entity ->
            val exercises = mutableListOf<Exercise>()
            entity.exerciseIdWithSet.forEach { (key, value) ->
                val sets = exerciseSetNetworkMapper.entityListToDomainList(value)
                exercises.add(
                    exerciseFactory.createExercise(
                        idExercise = key,
                        name = "incomplete",
                        sets = sets)
                )
            }

            val groups = mutableListOf<Group>()
            entity.groupIds?.forEach { idGroup ->
                groups.add(
                    groupFactory.createGroup(
                        idGroup = idGroup,
                        name = "incomplete"
                    )
                )
            }

            val workout = workoutNetworkMapper.mapFromEntity(entity)
            workout.exercises = exercises
            workout.groups = groups
            workouts.add(workout)

        }
        /*
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
        }*/

        return workouts
    }




}