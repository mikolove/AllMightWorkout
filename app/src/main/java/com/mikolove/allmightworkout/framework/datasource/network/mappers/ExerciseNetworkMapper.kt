package com.mikolove.allmightworkout.framework.datasource.network.mappers

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.network.model.ExerciseNetworkEntity

class ExerciseNetworkMapper
constructor(
    private val dateUtil: DateUtil,
    private val bodyPartNetworkMapper : BodyPartNetworkMapper,
    private val exerciseSetNetworkMapper : ExerciseSetNetworkMapper
) : EntityMapper<ExerciseNetworkEntity,Exercise>{

    override fun mapFromEntity(entity: ExerciseNetworkEntity): Exercise {

        val bodyPart = entity.bodyPart?.let { bodyPartNetworkMapper.mapFromEntity(it) } ?: null
        val sets = exerciseSetNetworkMapper.entityListToDomainList(entity.sets)

        return Exercise(
            idExercise = entity.idExercise,
            name = entity.name,
            sets = sets,
            bodyPart = bodyPart,
            exerciseType = ExerciseType.valueOf(entity.exerciseType),
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = dateUtil.convertFirebaseTimestampToStringData(entity.createdAt),
            updatedAt = dateUtil.convertFirebaseTimestampToStringData(entity.updatedAt)
        )
    }

    override fun mapToEntity(domainModel: Exercise): ExerciseNetworkEntity {

        val bodyPart = domainModel.bodyPart?.let { bodyPartNetworkMapper.mapToEntity(it) }
        val sets = exerciseSetNetworkMapper.domainListToEntityList(domainModel.sets)

        return ExerciseNetworkEntity(
            idExercise = domainModel.idExercise,
            name = domainModel.name,
            sets = sets,
            bodyPart = bodyPart,
            exerciseType = domainModel.exerciseType.name,
            isActive = domainModel.isActive,
            createdAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.createdAt),
            updatedAt = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updatedAt)
        )

    }

    override fun entityListToDomainList(entities: List<ExerciseNetworkEntity>): List<Exercise> {
        val list : ArrayList<Exercise> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    override fun domainListToEntityList(domains: List<Exercise>): List<ExerciseNetworkEntity> {
        val entities : ArrayList<ExerciseNetworkEntity> = ArrayList()
        for(exercise in domains){
            entities.add(mapToEntity(exercise))
        }
        return entities
    }
}