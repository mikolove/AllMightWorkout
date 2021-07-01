package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryExerciseFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseSetNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_USER_ID
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_EXERCISES_SETS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import kotlinx.coroutines.tasks.await

class HistoryExerciseFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val historyExerciseNetworkMapper: HistoryExerciseNetworkMapper,
    private val historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper,

    ) : HistoryExerciseFirestoreService{

    override suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ) {
        val entity = historyExerciseNetworkMapper.mapToEntity(historyExercise)

        val historyExerciseEntity = hashMapOf(
            "name" to entity.name,
            "bodyPart" to entity.bodyPart,
            "workoutType" to entity.workoutType,
            "exerciseType" to entity.exerciseType,
            "startedAt" to entity.startedAt,
            "endedAt" to entity.endedAt,
            "createdAt" to entity.createdAt,
            "updatedAt" to entity.updatedAt
         )
        firestore
            .collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser.uid)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(idHistoryWorkout)
            .collection(HISTORY_EXERCISES_COLLECTION)
            .document(entity.idHistoryExercise)
            .set(historyExerciseEntity)
            .addOnFailureListener {
                // send error reports to Firebase Crashlytics
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getHistoryExerciseByHistoryWorkoutId(workoutId: String): List<HistoryExercise> {
        val historyExercises =  firestore
            .collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser.uid)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(workoutId)
            .collection(HISTORY_EXERCISES_COLLECTION)
            .get()
            .addOnFailureListener{
                cLog(it.message)
            }
            .await().toObjects(HistoryExerciseNetworkEntity::class.java)?.let {
                historyExerciseNetworkMapper.entityListToDomainList(it)
            }

        historyExercises?.let { listOfHistoryExercise ->

            listOfHistoryExercise.forEach { historyExercise ->

                val sets  : List<HistoryExerciseSet> = firestore
                    .collection(USERS_COLLECTION)
                    .document(firebaseAuth.currentUser.uid)
                    .collection(HISTORY_WORKOUTS_COLLECTION)
                    .document(workoutId)
                    .collection(HISTORY_EXERCISES_COLLECTION)
                    .document(historyExercise.idHistoryExercise)
                    .collection(HISTORY_EXERCISES_SETS_COLLECTION)
                    .get()
                    .addOnFailureListener{
                        cLog(it.message)
                    }
                    .await().toObjects(HistoryExerciseSetNetworkEntity::class.java)?.let {
                        historyExerciseSetNetworkMapper.entityListToDomainList(it)
                    }

                if(sets.isNullOrEmpty()){
                    historyExercise.historySets = null
                }else{
                    historyExercise.historySets = sets
                }
            }
        }

        return historyExercises
    }

    override suspend fun getHistoryExerciseById(primaryKey: String, idHistoryWorkout: String): HistoryExercise? {

        val historyExercise =  firestore
            .collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser.uid)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(idHistoryWorkout)
            .collection(HISTORY_EXERCISES_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener{
                cLog(it.message)
            }
            .await().toObject(HistoryExerciseNetworkEntity::class.java)?.let {
                historyExerciseNetworkMapper.mapFromEntity(it)
            }

        historyExercise?.let { hExercise ->

                val sets = firestore
                    .collection(USERS_COLLECTION)
                    .document(firebaseAuth.currentUser.uid)
                    .collection(HISTORY_WORKOUTS_COLLECTION)
                    .document(idHistoryWorkout)
                    .collection(HISTORY_EXERCISES_COLLECTION)
                    .document(hExercise.idHistoryExercise)
                    .collection(HISTORY_EXERCISES_SETS_COLLECTION)
                    .get()
                    .addOnFailureListener{
                        cLog(it.message)
                    }
                    .await().toObjects(HistoryExerciseSetNetworkEntity::class.java)?.let {
                        historyExerciseSetNetworkMapper.entityListToDomainList(it)
                    }


            if(sets.isNullOrEmpty()){
                historyExercise.historySets = null
            }else{
                historyExercise.historySets = sets
            }

        }

        return historyExercise

    }
}