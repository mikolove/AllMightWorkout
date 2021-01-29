package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class ExerciseCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil
) : EntityMapper<ExerciseCacheEntity,Exercise>{

    override fun mapFromEntity(entity: ExerciseCacheEntity): Exercise {
        return Exercise(
            idExercise = entity.idExercise,
            name = entity.name,
            sets = listOf(),
            bodyPart = null,
            exerciseType = ExerciseType.valueOf(entity.exerciseType),
            isActive = entity.isActive,
            started_at = null,
            ended_at = null,
            created_at = roomDateUtil.convertDateToStringDate(entity.created_at),
            updated_at = roomDateUtil.convertDateToStringDate(entity.updated_at)
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseCacheEntity {
        return ExerciseCacheEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            idBodyPart = domainModel.bodyPart?.idBodyPart,
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            created_at = roomDateUtil.convertStringDateToDate(domainModel.created_at),
            updated_at = roomDateUtil.convertStringDateToDate(domainModel.updated_at)
        )
    }

    override fun entityListToDomainList(entities: List<ExerciseCacheEntity>): List<Exercise> {
        val list : ArrayList<Exercise> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<Exercise>): List<ExerciseCacheEntity> {
        val entities : ArrayList<ExerciseCacheEntity> = ArrayList()
        for(exercise in domains){
            entities.add(mapToEntity(exercise))
        }
        return entities
    }

}