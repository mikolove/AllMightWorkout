package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseWithSetsCacheEntity

class ExerciseWithSetsCacheMapper
constructor(
    private val bodyPartCacheMapper: BodyPartCacheMapper,
    private val exerciseCacheMapper: ExerciseCacheMapper,
    private val exerciseSetCacheMapper: ExerciseSetCacheMapper
) : EntityMapper<ExerciseWithSetsCacheEntity,Exercise>{

    override fun mapFromEntity(entity: ExerciseWithSetsCacheEntity): Exercise {

        var exercise = exerciseCacheMapper.mapFromEntity(entity.exerciseCacheEntity)
        var bodyPart = entity.bodyPartCacheEntity?.let { bodyPartCacheMapper.mapFromEntity(it) }
        var listOfSets : List<ExerciseSet>?
        if(!entity.listOfExerciseSetCacheEntity.isNullOrEmpty()){
            listOfSets = entity.listOfExerciseSetCacheEntity.let { exerciseSetCacheMapper.entityListToDomainList(it) }
        }else{
            listOfSets = listOf()
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