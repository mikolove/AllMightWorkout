package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.WorkoutTypeWithBodyPartCacheEntity
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.core.domain.workouttype.WorkoutType

class WorkoutTypeWithBodyPartCacheMapper
constructor(
    private val workoutTypeCacheMapper: WorkoutTypeCacheMapper,
    private val bodyPartCacheMapper : BodyPartCacheMapper
) : EntityMapper<WorkoutTypeWithBodyPartCacheEntity, WorkoutType> {

    override fun mapFromEntity(entity: WorkoutTypeWithBodyPartCacheEntity): WorkoutType {

        val workoutType = workoutTypeCacheMapper.mapFromEntity(entity.workoutTypeCacheEntity)
        workoutType.bodyParts = if(!entity.listOfBodyPartCacheEntity.isNullOrEmpty()){
            bodyPartCacheMapper.entityListToDomainList(entity.listOfBodyPartCacheEntity)
        }else{
            listOf()
        }
        return workoutType
    }

    override fun mapToEntity(domainModel: WorkoutType): WorkoutTypeWithBodyPartCacheEntity {
        TODO("Not yet implemented")
    }

    override fun entityListToDomainList(entities: List<WorkoutTypeWithBodyPartCacheEntity>): List<WorkoutType> {
        val list : ArrayList<WorkoutType> = ArrayList()
        entities.forEach { entity ->
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<WorkoutType>): List<WorkoutTypeWithBodyPartCacheEntity> {
        TODO("Not yet implemented")
    }
}