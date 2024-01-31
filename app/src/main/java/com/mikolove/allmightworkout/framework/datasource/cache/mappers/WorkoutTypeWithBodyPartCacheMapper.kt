package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutTypeWithBodyPartCacheEntity

class WorkoutTypeWithBodyPartCacheMapper
constructor(
    private val workoutTypeCacheMapper: WorkoutTypeCacheMapper,
    private val bodyPartCacheMapper : BodyPartCacheMapper
) : EntityMapper<WorkoutTypeWithBodyPartCacheEntity, WorkoutType>{

    override fun mapFromEntity(entity: WorkoutTypeWithBodyPartCacheEntity): WorkoutType {

        var workoutType = workoutTypeCacheMapper.mapFromEntity(entity.workoutTypeCacheEntity)
        var bodyParts : List<BodyPart>?
        if(!entity.listOfBodyPartCacheEntity.isNullOrEmpty()){
            bodyParts = entity.listOfBodyPartCacheEntity.let {
                    bodyPartCacheMapper.entityListToDomainList(it)
                }
        }else{
            bodyParts = null
        }

        workoutType.bodyParts = bodyParts

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