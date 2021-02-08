package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.api.ResourceDescriptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.HistoryWorkoutFirestoreService
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryExerciseSetNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.mappers.HistoryWorkoutNetworkMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryExerciseSetNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.model.HistoryWorkoutNetworkEntity
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth.FIRESTORE_USER_ID
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_EXERCISES_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_EXERCISES_SETS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.HISTORY_WORKOUTS_COLLECTION
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreConstants.USERS_COLLECTION
import com.mikolove.allmightworkout.util.cLog
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.tasks.await

class HistoryWorkoutFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val historyWorkoutNetworkMapper: HistoryWorkoutNetworkMapper,
    private val historyExerciseNetworkMapper: HistoryExerciseNetworkMapper,
    private val historyExerciseSetNetworkMapper: HistoryExerciseSetNetworkMapper,
    private val dateUtil: DateUtil
) : HistoryWorkoutFirestoreService{

    override suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout) {
        val entity = historyWorkoutNetworkMapper.mapToEntity(historyWorkout)

        val historyWorkoutEntity = hashMapOf(
            "name" to entity.name,
            "startedAt" to entity.startedAt,
            "endedAt" to entity.endedAt,
            "createdAt" to entity.createdAt,
            "updatedAt" to entity.updatedAt
        )
        firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(entity.idHistoryWorkout)
            .set(historyWorkoutEntity)
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
    }

    override suspend fun getLastHistoryWorkout(): List<HistoryWorkout>? {

        var historyWorkouts = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .whereGreaterThan("createdAt",dateUtil.getCurrentDateLessMonth(3))
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObjects(HistoryWorkoutNetworkEntity::class.java)?.let {
                historyWorkoutNetworkMapper.entityListToDomainList(it)
            }

        return historyWorkouts?.let { fillHistoryWorkouts(it) }
    }

    override suspend fun getHistoryWorkout(): List<HistoryWorkout> {
        var historyWorkouts = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObjects(HistoryWorkoutNetworkEntity::class.java)?.let {
                historyWorkoutNetworkMapper.entityListToDomainList(it)
            }

        return historyWorkouts?.let { fillHistoryWorkouts(it) } ?: ArrayList()
    }

    override suspend fun getHistoryWorkoutById(primaryKey: String): HistoryWorkout? {

        var historyWorkout = firestore
            .collection(USERS_COLLECTION)
            .document(FIRESTORE_USER_ID)
            .collection(HISTORY_WORKOUTS_COLLECTION)
            .document(primaryKey)
            .get()
            .addOnFailureListener {
                cLog(it.message)
            }
            .await()
            .toObject(HistoryWorkoutNetworkEntity::class.java)?.let {
                historyWorkoutNetworkMapper.mapFromEntity(it)
            }

        return historyWorkout?.let { fillHistoryWorkout(it) }
    }

    private suspend fun fillHistoryWorkouts(historyWorkouts : List<HistoryWorkout>?) : List<HistoryWorkout>?{
        if(historyWorkouts.isNullOrEmpty()){
            return null
        }else{
            val listHistoryWorkout : ArrayList<HistoryWorkout> = ArrayList()
            for(historyWorkout in historyWorkouts){
                val toAdd = fillHistoryWorkout(historyWorkout)
                toAdd?.let { listHistoryWorkout.add(it) }
            }
            return listHistoryWorkout.toList()
        }
    }
    private suspend fun fillHistoryWorkout(historyWorkout : HistoryWorkout) : HistoryWorkout?{

        if(historyWorkout == null){

            return null
        }else{

            historyWorkout?.let { hWorkout ->

                var historyExercises : List<HistoryExercise>? = firestore
                    .collection(USERS_COLLECTION)
                    .document(FIRESTORE_USER_ID)
                    .collection(HISTORY_WORKOUTS_COLLECTION)
                    .document(hWorkout.idHistoryWorkout)
                    .collection(HISTORY_EXERCISES_COLLECTION)
                    .get()
                    .addOnFailureListener {
                        cLog(it.message)
                    }
                    .await()
                    .toObjects(HistoryExerciseNetworkEntity::class.java)?.let {
                        historyExerciseNetworkMapper.entityListToDomainList(it)
                    }
                if(historyExercises.isNullOrEmpty()) {
                    historyExercises = null
                }else{
                    historyExercises.forEach { historyExercise ->

                        var historySets : List<HistoryExerciseSet>? = firestore
                            .collection(USERS_COLLECTION)
                            .document(FIRESTORE_USER_ID)
                            .collection(HISTORY_WORKOUTS_COLLECTION)
                            .document(historyWorkout.idHistoryWorkout)
                            .collection(HISTORY_EXERCISES_COLLECTION)
                            .document(historyExercise.idHistoryExercise)
                            .collection(HISTORY_EXERCISES_SETS_COLLECTION)
                            .get()
                            .addOnFailureListener {
                                cLog(it.message)
                            }
                            .await()
                            .toObjects(HistoryExerciseSetNetworkEntity::class.java)?.let {
                                historyExerciseSetNetworkMapper.entityListToDomainList(it)
                            }

                        //Add Exercises Set
                        if(historySets.isNullOrEmpty()){
                            historyExercise.historySets = null
                        }else{
                            historyExercise.historySets = historySets
                        }
                    }
                }
                //Add Exercises to workout
                hWorkout.historyExercises = historyExercises
            }
        }
        return historyWorkout
    }

}