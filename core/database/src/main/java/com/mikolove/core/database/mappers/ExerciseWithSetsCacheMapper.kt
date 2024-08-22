package com.mikolove.core.database.mappers

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseWithSetsCacheEntity

class ExerciseWithSetsCacheMapper
constructor(
    private val bodyPartCacheMapper: BodyPartCacheMapper,
    private val exerciseCacheMapper: ExerciseCacheMapper,
    private val exerciseSetCacheMapper: ExerciseSetCacheMapper
) : EntityMapper<ExerciseWithSetsCacheEntity, Exercise> {

    override fun mapFromEntity(entity: ExerciseWithSetsCacheEntity): Exercise {

        val exercise = exerciseCacheMapper.mapFromEntity(entity.exerciseCacheEntity)
        val bodyPart = entity.bodyPartCacheEntity?.let { bodyPartCacheMapper.mapFromEntity(it) }
        val listOfSets : List<ExerciseSet> = if(!entity.listOfExerciseSetCacheEntity.isNullOrEmpty()){
            entity.listOfExerciseSetCacheEntity.let { exerciseSetCacheMapper.entityListToDomainList(it) }
        }else{
            listOf()
        }

        exercise.bodyPart = bodyPart
        exercise.sets = listOfSets

        return exercise
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseWithSetsCacheEntity {
        TODO("Not yet implemented")
    }

    override fun entityListToDomainList(entities: List<ExerciseWithSetsCacheEntity>): List<Exercise> {
        val list : ArrayList<Exercise> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<Exercise>): List<ExerciseWithSetsCacheEntity> {
        TODO("Not yet implemented")
    }
}