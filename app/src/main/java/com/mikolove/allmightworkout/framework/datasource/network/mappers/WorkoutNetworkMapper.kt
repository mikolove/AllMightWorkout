package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.WorkoutNetworkEntity

class WorkoutNetworkMapper
constructor(private val dateUtil: DateUtil)
    : EntityMapper<WorkoutNetworkEntity,Workout>{

    override fun mapFromEntity(entity: WorkoutNetworkEntity): Workout {
        val exercises =  listOf<Exercise>()
        return Workout(
            idWorkout = entity.idWorkout,
            name = entity.name,
            exercises = exercises,
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Workout): WorkoutNetworkEntity {
        val exerciseIds = domainModel.exercises?.mapIndexed{index,exercise -> exercise.idExercise} ?: ArrayList()
        return WorkoutNetworkEntity(
            idWorkout = domainModel.idWorkout,
            name = domainModel.name,
            exerciseIds = exerciseIds,
            isActive = domainModel.isActive,
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }

    override fun entityListToDomainList(entities: List<WorkoutNetworkEntity>): List<Workout> {
        val list : ArrayList<Workout> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<Workout>): List<WorkoutNetworkEntity> {
        val entities : ArrayList<WorkoutNetworkEntity> = ArrayList()
        for(workout in domains){
            entities.add(mapToEntity(workout))
        }
        return entities
    }
}