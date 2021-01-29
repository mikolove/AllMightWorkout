package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseSetCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class ExerciseSetCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil
): EntityMapper<ExerciseSetCacheEntity,ExerciseSet>{
    override fun mapFromEntity(entity: ExerciseSetCacheEntity): ExerciseSet {
        return ExerciseSet(
            idExerciseSet = entity.idExerciseSet,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            restTime = entity.restTime,
            started_at = null,
            ended_at = null,
            created_at = roomDateUtil.convertDateToStringDate(entity.created_at),
            updated_at = roomDateUtil.convertDateToStringDate(entity.updated_at)
        )
    }

    override fun mapToEntity(domainModel: ExerciseSet): ExerciseSetCacheEntity {
        return ExerciseSetCacheEntity(
            idExerciseSet = domainModel.idExerciseSet,
            idExercise = null,
            reps = domainModel.reps,
            weight = domainModel.weight,
            time = domainModel.time,
            restTime = domainModel.restTime,
            created_at = roomDateUtil.convertStringDateToDate(domainModel.created_at),
            updated_at = roomDateUtil.convertStringDateToDate(domainModel.updated_at)
        )
    }

    override fun entityListToDomainList(entities: List<ExerciseSetCacheEntity>): List<ExerciseSet> {
        val list : ArrayList<ExerciseSet> = ArrayList()
        for(entity in entities){
           list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<ExerciseSet>): List<ExerciseSetCacheEntity> {
        val entities : ArrayList<ExerciseSetCacheEntity> = ArrayList()
        for(exerciseSet in domains){
            entities.add(mapToEntity(exerciseSet))
        }
        return entities
    }
}