package com.mikolove.allmightworkout.framework.datasource.cache.mappers

import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.util.EntityMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.UserWithWorkoutAndExerciseCacheEntity

class UserWithWorkoutCacheMapper
constructor(
    private val userCacheMapper: UserCacheMapper,
    private val workoutWithExercisesCacheMapper: WorkoutWithExercisesCacheMapper
) : EntityMapper<UserWithWorkoutAndExerciseCacheEntity, User> {

    override fun mapFromEntity(entity: UserWithWorkoutAndExerciseCacheEntity): User {

        val workouts = entity.listOfWorkoutCacheEntity?.let {
            workoutWithExercisesCacheMapper.entityListToDomainList(it)
        }

        return userCacheMapper.mapFromEntity(entity.userCacheEntity).copy(workouts = workouts)
    }

    override fun mapToEntity(domainModel: User): UserWithWorkoutAndExerciseCacheEntity {
        TODO("Not yet implemented")
    }

    override fun entityListToDomainList(entities: List<UserWithWorkoutAndExerciseCacheEntity>): List<User> {
        return entities.map { mapFromEntity(it) }
    }

    override fun domainListToEntityList(domains: List<User>): List<UserWithWorkoutAndExerciseCacheEntity> {
        return super.domainListToEntityList(domains)
    }
}