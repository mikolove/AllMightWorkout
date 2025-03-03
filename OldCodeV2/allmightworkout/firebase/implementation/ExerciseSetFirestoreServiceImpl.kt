package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.firebase.abstraction.ExerciseSetFirestoreService
import com.mikolove.allmightworkout.firebase.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.firebase.mappers.ExerciseSetNetworkMapper
import com.mikolove.allmightworkout.firebase.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.REMOVED_EXERCISE_SETS_COLLECTION
import com.mikolove.allmightworkout.firebase.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.ExerciseSet
import kotlinx.coroutines.tasks.await

class ExerciseSetFirestoreServiceImpl
constructor(
    private val sessionStorage: SessionStorage,
    private val firestore : FirebaseFirestore,
    private val exerciseNetworkMapper : ExerciseNetworkMapper,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper
) : ExerciseSetFirestoreService {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String) {

        val userId = sessionStorage.get()?.userId ?: return

        val entity = exerciseSetNetworkMapper.mapToEntity(exerciseSet)
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .update("sets" , FieldValue.arrayUnion(entity))
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

        val userId = sessionStorage.get()?.userId ?: return

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.mapFromEntity(it)
            }

        exercise?.let {

            //Update sets
            var listOfSets: ArrayList<ExerciseSet> = ArrayList()
            for (networkSet in exercise.sets) {
                if (networkSet.idExerciseSet == exerciseSet.idExerciseSet)
                    listOfSets.add(exerciseSet)
                else
                    listOfSets.add(networkSet)
            }
            it.sets = listOfSets

            //Update entity
            val updatedEntity = exerciseNetworkMapper.mapToEntity(it)
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXERCISES_COLLECTION)
                .document(idExercise)
                .set(updatedEntity)
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }

    }

    override suspend fun updateExerciseSets(exerciseSets: List<ExerciseSet>, idExercise: String) {

        val userId = sessionStorage.get()?.userId ?: return

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.mapFromEntity(it)
            }

        exercise?.let {

            //Update entity
            it.sets = exerciseSets

            val updatedEntity = exerciseNetworkMapper.mapToEntity(it)
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXERCISES_COLLECTION)
                .document(idExercise)
                .set(updatedEntity)
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }

    }

    override suspend fun insertDeletedExerciseSets(exerciseSets: List<ExerciseSet>) {

        val userId = sessionStorage.get()?.userId ?: return

        if(exerciseSets.size > 500){
            throw Exception("Cannot delete more than 500 exercise sets at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(REMOVED_EXERCISE_SETS_COLLECTION)

        firestore.runBatch { batch ->
            for(set in exerciseSets){
                val documentRef = collectionRef.document(set.idExerciseSet)
                batch.set(documentRef, exerciseSetNetworkMapper.mapToEntity(set))
            }
        }.addOnFailureListener {
            // send error reports to Firebase Crashlytics
            cLog(it.message)
        }.await()


    }

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise: String) {

        val userId = sessionStorage.get()?.userId ?: return

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.mapFromEntity(it)
            }

        exercise?.let {

            //Remove set
            val newSets = it.sets.filter { set -> set.idExerciseSet != primaryKey }
            it.sets = newSets

            //Update entity
            val updatedEntity = exerciseNetworkMapper.mapToEntity(it)

            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXERCISES_COLLECTION)
                .document(idExercise)
                .set(updatedEntity)
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun removeExerciseSetsById(primaryKeys: List<String>, idExercise: String) {

        val userId = sessionStorage.get()?.userId ?: return
        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.mapFromEntity(it)
            }

        exercise?.let {

            //Remove set
            val newSets = it.sets.filter { set -> !primaryKeys.contains(set.idExerciseSet) }
            it.sets = newSets

            //Update entity
            val updatedEntity = exerciseNetworkMapper.mapToEntity(it)

            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXERCISES_COLLECTION)
                .document(idExercise)
                .set(updatedEntity)
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }
    }

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? {

        val userId = sessionStorage.get()?.userId ?: return null
        var exerciseSet : ExerciseSet? = null

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.mapFromEntity(it)
            }

        exercise?.let {
            exerciseSet = it.sets.find { set ->
                set.idExerciseSet == primaryKey
            }
        }

        return exerciseSet

    }

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet>? {

        val userId = sessionStorage.get()?.userId ?: return null
        var exerciseSets : List<ExerciseSet>? = null

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXERCISES_COLLECTION)
            .document(idExercise)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                exerciseNetworkMapper.mapFromEntity(it)
            }

        exercise?.sets?.let {
            exerciseSets = it
        }

        return exerciseSets
    }

    override suspend fun getTotalExercisesSetByExercise(idExercise: String): Int {

        val userId = sessionStorage.get()?.userId ?: return 0
        var totalExerciseSet : Int = 0

            val exercise = firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXERCISES_COLLECTION)
                .document(idExercise)
                .get()
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await().toObject(ExerciseNetworkEntity::class.java)?.let {
                    exerciseNetworkMapper.mapFromEntity(it)
                }

            exercise?.sets?.let {
                totalExerciseSet = it.size
            }

        return totalExerciseSet
    }

    //TODO : Change this
    override suspend fun insertDeletedExerciseSet(exerciseSet: ExerciseSet) {

        /*        firebaseAuth.currentUser?.let { currentUser ->

                    val entity = exerciseSetNetworkMapper.mapToEntity(exerciseSet)
                    firestore
                        .collection(USERS_COLLECTION)
                        .document(userId)
                        .collection(REMOVED_EXERCISE_SETS_COLLECTION)
                        .document(entity.idExerciseSet)
                        .set(entity)
                        .addOnFailureListener {
                            // send error reports to Firebase Crashlytics
                            cLog(it.message)
                        }
                        .await()
                }*/
    }

    //TODO : Change this
    override suspend fun getDeletedExerciseSets(): List<ExerciseSet> {

        return listOf()
        /*  firebaseAuth.currentUser?.let { currentUser ->

              return firestore
                  .collection(USERS_COLLECTION)
                  .document(FIRESTORE_USER_ID)
                  .collection(REMOVED_EXERCISE_SETS_COLLECTION)
                  .get()
                  .addOnFailureListener {
                      // send error reports to Firebase Crashlytics
                      cLog(it.message)
                  }
                  .await().toObjects(ExerciseSetNetworkEntity::class.java).let {
                      exerciseSetNetworkMapper.entityListToDomainList(it)
                  }
          }*/
    }
}