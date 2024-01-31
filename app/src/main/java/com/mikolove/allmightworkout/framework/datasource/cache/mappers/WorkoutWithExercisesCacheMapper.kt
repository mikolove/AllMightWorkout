package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutWithExercisesCacheEntity

class WorkoutWithExercisesCacheMapper
constructor(
    private val workoutCacheMapper: WorkoutCacheMapper,
    private val exerciseWithSetsCacheMapper: ExerciseWithSetsCacheMapper
): EntityMapper<WorkoutWithExercisesCacheEntity, Workout>{

    override fun mapFromEntity(entity: WorkoutWithExercisesCacheEntity): Workout {

        var workout = workoutCacheMapper.mapFromEntity(entity.workoutCacheEntity)
        var exercisesWithSets : List<Exercise>?
        if(!entity.listOfExerciseCacheEntity.isNullOrEmpty()) {
            exercisesWithSets = entity.listOfExerciseCacheEntity.let {
                exerciseWithSetsCacheMapper.entityListToDomainList(it)
            }
        }
        else{
            exercisesWithSets = null
        }

        workout.exercises = exercisesWithSets
        return workout
    }

    override fun mapToEntity(domainModel: Workout): WorkoutWithExercisesCacheEntity {
        TODO("Not yet implemented")
    }

    override fun entityListToDomainList(entities: List<WorkoutWithExercisesCacheEntity>): List<Workout> {
        val list : ArrayList<Workout> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<Workout>): List<WorkoutWithExercisesCacheEntity> {
        TODO("Not yet implemented")
    }
}