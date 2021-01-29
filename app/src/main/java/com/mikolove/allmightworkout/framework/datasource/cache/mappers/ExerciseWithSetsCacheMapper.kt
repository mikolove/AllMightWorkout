package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseWithSetsCacheEntity
import javax.inject.Inject

class ExerciseWithSetsCacheMapper
@Inject
constructor(
    private val bodyPartCacheMapper: BodyPartCacheMapper,
    private val exerciseCacheMapper: ExerciseCacheMapper,
    private val exerciseSetCacheMapper: ExerciseSetCacheMapper
) : EntityMapper<ExerciseWithSetsCacheEntity,Exercise>{

    override fun mapFromEntity(entity: ExerciseWithSetsCacheEntity): Exercise {

        var exercise = exerciseCacheMapper.mapFromEntity(entity.exerciseCacheEntity)
        var bodyPart = bodyPartCacheMapper.mapFromEntity(entity.bodyPartCacheEntity)
        var listOfSets = exerciseSetCacheMapper.entityListToDomainList(entity.listOfExerciseSetCacheEntity)

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