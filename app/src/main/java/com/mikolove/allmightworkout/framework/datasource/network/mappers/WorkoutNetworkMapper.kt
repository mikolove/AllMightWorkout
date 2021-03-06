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
        return Workout(
            idWorkout = entity.idWorkout,
            name = entity.name,
            exercises = null,
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            exerciseIdsUpdatedAt = entity.exerciseIdsUpdatedAt?.let { dateUtil.convertFirebaseTimestampToStringData(it) },
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Workout): WorkoutNetworkEntity {
        return WorkoutNetworkEntity(
            idWorkout = domainModel.idWorkout,
            name = domainModel.name,
            exerciseIds = null,
            isActive = domainModel.isActive,
            exerciseIdsUpdatedAt = domainModel.exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToFirebaseTimestamp(it) },
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )
    }

}