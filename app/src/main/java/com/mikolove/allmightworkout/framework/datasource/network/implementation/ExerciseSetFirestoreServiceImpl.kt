package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseSetFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.ExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseSetNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_USER_ID
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.REMOVED_EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.REMOVED_EXERCISE_SETS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ExerciseSetFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val exerciseNetworkMapper : ExerciseNetworkMapper,
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper
) : ExerciseSetFirestoreService{

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String) {

        val entity = exerciseSetNetworkMapper.mapToEntity(exerciseSet)

        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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
            var listOfSets : ArrayList<ExerciseSet> = ArrayList()
            for(networkSet in exercise.sets){
                if(networkSet.idExerciseSet == exerciseSet.idExerciseSet )
                    listOfSets.add(exerciseSet)
                else
                    listOfSets.add(networkSet)
            }
            it.sets = listOfSets

            //Update entity
            val updatedEntity = exerciseNetworkMapper.mapToEntity(it)
            firestore
                .collection(USERS_COLLECTION)
                .document(FIRESTORE_USER_ID)
                .collection(EXERCISES_COLLECTION)
                .document(idExercise)
                .set(updatedEntity)
                .addOnFailureListener {
                    cLog(it.message)
                }
                .await()
        }

    }

    override suspend fun updateExerciseSets(exerciseSet: List<ExerciseSet>, idExercise: String) {

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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
            it.sets = exerciseSet

            val updatedEntity = exerciseNetworkMapper.mapToEntity(it)
            firestore
                .collection(USERS_COLLECTION)
                .document(FIRESTORE_USER_ID)
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
        if(exerciseSets.size > 500){
            throw Exception("Cannot delete more than 500 exercise sets at a time in firestore.")
        }

        val collectionRef = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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
                .document(FIRESTORE_USER_ID)
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

        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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
                .document(FIRESTORE_USER_ID)
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
        var exerciseSet : ExerciseSet? = null
        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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
        var exerciseSets : List<ExerciseSet>? = null
        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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
        var totalExerciseSet : Int = 0
        var exerciseSets : List<ExerciseSet>? = null
        val exercise = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
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

    //TODO : Change this
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