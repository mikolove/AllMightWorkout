package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutTypeCacheEntity

class WorkoutTypeCacheMapper
constructor() : EntityMapper<WorkoutTypeCacheEntity, WorkoutType> {

    override fun mapFromEntity(entity: WorkoutTypeCacheEntity): WorkoutType {
        return WorkoutType(
            idWorkoutType = entity.idWorkoutType,
            name = entity.name,
            bodyParts = null
        )
    }

    override fun mapToEntity(domainModel: WorkoutType): WorkoutTypeCacheEntity {
        return WorkoutTypeCacheEntity(
            idWorkoutType = domainModel.idWorkoutType,
            name = domainModel.name
        )
    }

    override fun entityListToDomainList(entities: List<WorkoutTypeCacheEntity>): List<WorkoutType> {
        val list : ArrayList<WorkoutType> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<WorkoutType>): List<WorkoutTypeCacheEntity> {
        val entities : ArrayList<WorkoutTypeCacheEntity> = ArrayList()
        for(workoutType in domains){
            entities.add(mapToEntity(workoutType))
        }
        return entities
    }
}