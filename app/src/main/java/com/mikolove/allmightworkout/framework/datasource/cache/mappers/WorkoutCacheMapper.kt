package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject

class WorkoutCacheMapper
@Inject
constructor(
    private val roomDateUtil: RoomDateUtil)
: EntityMapper<WorkoutCacheEntity,Workout> {

    override fun mapFromEntity(entity: WorkoutCacheEntity): Workout {
        return Workout(
            idWorkout = entity.idWorkout,
            name = entity.name,
            exercises = null,
            isActive = entity.isActive,
            started_at = null,
            ended_at = null,
            created_at = roomDateUtil.convertDateToStringDate(entity.created_at),
            updated_at = roomDateUtil.convertDateToStringDate(entity.updated_at)
        )
    }

    override fun mapToEntity(domainModel: Workout): WorkoutCacheEntity {
        return WorkoutCacheEntity(
            idWorkout = domainModel.idWorkout,
            name = domainModel.name,
            isActive = domainModel.isActive,
            created_at = roomDateUtil.convertStringDateToDate(domainModel.created_at),
            updated_at = roomDateUtil.convertStringDateToDate(domainModel.updated_at)
        )
    }

    override fun entityListToDomainList(entities: List<WorkoutCacheEntity>): List<Workout> {
        val list : ArrayList<Workout> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<Workout>): List<WorkoutCacheEntity> {
        val entities : ArrayList<WorkoutCacheEntity> = ArrayList()
        for(workout in domains){
            entities.add(mapToEntity(workout))
        }
        return entities
    }

}