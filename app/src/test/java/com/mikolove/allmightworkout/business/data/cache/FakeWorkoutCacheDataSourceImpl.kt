package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_PAGINATION_PAGE_SIZE

const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

const val FORCE_NEW_WORKOUT_EXCEPTION = "FORCE_NEW_WORKOUT_EXCEPTION"
const val FORCE_UPDATE_WORKOUT_EXCEPTION = "FORCE_UPDATE_WORKOUT_EXCEPTION"
const val FORCE_UPDATE_WORKOUT_EXERCISE_IDS_EXCEPTION = "FORCE_UPDATE_WORKOUT_EXERCISE_IDS_EXCEPTION"
const val FORCE_DELETE_WORKOUT_EXCEPTION = "FORCE_DELETE_WORKOUT_EXCEPTION"
const val FORCE_DELETES_WORKOUT_EXCEPTION = "FORCE_DELETES_WORKOUT_EXCEPTION"
const val FORCE_SEARCH_WORKOUTS_EXCEPTION = "FORCE_SEARCH_WORKOUTS_EXCEPTION"
const val FORCE_GET_WORKOUT_BY_ID_EXCEPTION = "FORCE_GET_WORKOUT_BY_ID_EXCEPTION"

class FakeWorkoutCacheDataSourceImpl
constructor(
    private val workoutsData: HashMap<String, Workout>,
    private val dateUtil: DateUtil
): WorkoutCacheDataSource{

    override suspend fun insertWorkout(workout: Workout): Long {
        if(workout.idWorkout.equals(FORCE_NEW_WORKOUT_EXCEPTION)){
            throw Exception("Something went wrong inserting the workout.")
        }
        if(workout.idWorkout.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        workoutsData.put(workout.idWorkout, workout)
        return 1 // success
    }

    override suspend fun updateWorkout(
        primaryKey: String,
        name: String,
        isActive: Boolean,
        updatedAt: String
    ): Int {
        if(primaryKey.equals(FORCE_UPDATE_WORKOUT_EXCEPTION)){
            throw Exception("Something went wrong updating the workout.")
        }
        val updatedWorkout = Workout(
            idWorkout = primaryKey,
            name = name,
            exercises = null,
            isActive = isActive,
            startedAt = null,
            endedAt = null,
            exerciseIdsUpdatedAt = null,
            createdAt = workoutsData.get(primaryKey)?.createdAt ?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp()
        )
        return workoutsData.get(primaryKey)?.let {
            workoutsData.put(primaryKey, updatedWorkout)
            1 // success
        }?: -1 // nothing to update
    }

    override suspend fun removeWorkout(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_WORKOUT_EXCEPTION)){
            throw Exception("Something went wrong deleting the workout.")
        }
        else if(primaryKey.equals(FORCE_DELETES_WORKOUT_EXCEPTION)){
            throw Exception("Something went wrong deleting workouts.")
        }
        return workoutsData.remove(primaryKey)?.let {
            1 // return 1 for success
        }?: - 1 // -1 for failure
    }

    override suspend fun getExerciseIdsUpdate(idWorkout: String): String? {
        return workoutsData[idWorkout]?.exerciseIdsUpdatedAt ?:""
    }

    override suspend fun updateExerciseIdsUpdatedAt(
        idWorkout: String,
        exerciseIdsUpdatedAt: String?
    ): Int {

        if(idWorkout.equals(FORCE_UPDATE_WORKOUT_EXERCISE_IDS_EXCEPTION)){
            throw Exception("Something went wrong updating exerciseIds date.")
        }
        return workoutsData.get(idWorkout)?.let { workout ->
            val updatedWorkout = Workout(
                idWorkout = workout.idWorkout,
                name = workout.name,
                exercises = null,
                isActive = workout.isActive,
                startedAt = null,
                endedAt = null,
                exerciseIdsUpdatedAt = exerciseIdsUpdatedAt,
                createdAt = workoutsData.get(idWorkout)?.createdAt ?: dateUtil.getCurrentTimestamp(),
                updatedAt = workoutsData.get(idWorkout)?.updatedAt ?: dateUtil.getCurrentTimestamp()
            )
            workoutsData.put(idWorkout, updatedWorkout)
            1 // success
        }?: 0
    }

    override suspend fun getWorkouts(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Workout> {
        if(query.equals(FORCE_SEARCH_WORKOUTS_EXCEPTION)){
            throw Exception("Something went wrong searching the cache for workouts.")
        }
        val results: ArrayList<Workout> = ArrayList()
        for(workout in workoutsData.values){
            if(workout.name.contains(query)){
                results.add(workout)
            }
            if(results.size > (page * WORKOUT_PAGINATION_PAGE_SIZE)){
                break
            }
        }
        return results
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {
        if(primaryKey.equals(FORCE_GET_WORKOUT_BY_ID_EXCEPTION)){
            throw Exception("Something went wrong retrieving workout by id.")
        }
        return workoutsData[primaryKey]
    }

    override suspend fun getTotalWorkout(): Int {
        return workoutsData.size
    }

    override suspend fun removeWorkouts(workouts: List<Workout>): Int {

        workouts.let { listWorkout ->
            for(workout in listWorkout){
                workoutsData.remove(workout.idWorkout)
            }
            return workouts.size
        }
        return -1
    }
}