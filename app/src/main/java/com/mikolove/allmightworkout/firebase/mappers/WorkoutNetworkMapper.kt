package com.mikolove.allmightworkout.firebase.mappers

import com.mikolove.allmightworkout.firebase.model.ExerciseSetNetworkEntity
import com.mikolove.allmightworkout.firebase.model.WorkoutNetworkEntity
import com.mikolove.allmightworkout.util.toFirebaseTimestamp
import com.mikolove.allmightworkout.util.toZoneDateTime
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.core.domain.workout.Workout

class WorkoutNetworkMapper(
    private val exerciseSetNetworkMapper: ExerciseSetNetworkMapper
)
    : EntityMapper<WorkoutNetworkEntity, Workout> {

    override fun mapFromEntity(entity: WorkoutNetworkEntity): Workout {

        val exercises = entity.
        return Workout(
            idWorkout = entity.idWorkout,
            name = entity.name,
            isActive = entity.isActive,
            startedAt = null,
            endedAt = null,
            createdAt = entity.createdAt.toZoneDateTime(),
            updatedAt = entity.updatedAt.toZoneDateTime()
        )
    }

    override fun mapToEntity(domainModel: Workout): WorkoutNetworkEntity {

        val mapOfExerciseWithSet : Map<String,List<ExerciseSetNetworkEntity>> =
            domainModel.exercises.associate{ exercise ->
                exercise.idExercise to exerciseSetNetworkMapper.domainListToEntityList(exercise.sets)
            }

        val listOfGroup : List<String> = domainModel.groups.mapIndexed { _, group -> group.idGroup }

        return WorkoutNetworkEntity(
            idWorkout = domainModel.idWorkout,
            name = domainModel.name,
            exerciseIdWithSet = mapOfExerciseWithSet,
            groupIds = listOfGroup,
            isActive = domainModel.isActive,
            createdAt = domainModel.createdAt.toFirebaseTimestamp(),
            updatedAt = domainModel.updatedAt.toFirebaseTimestamp()
        )
    }

}