package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.BodyPartDao
import com.mikolove.core.database.mappers.BodyPartCacheMapper
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheService

class BodyPartDaoService
constructor(
    private val bodyPartDao : BodyPartDao,
    private val bodyPartCacheMapper: BodyPartCacheMapper
) : BodyPartCacheService {

    override suspend fun upsertBodyPart(bodyPart: BodyPart, idWorkoutType: String): Long {
        val bodyPartCacheEntity = bodyPartCacheMapper.mapToEntity(bodyPart)
        bodyPartCacheEntity.idWorkoutType = idWorkoutType
        return bodyPartDao.upsertBodyPart(bodyPartCacheEntity)
    }

    override suspend fun removeBodyPart(primaryKey: String): Int {
        return bodyPartDao.removeBodyPart(primaryKey)
    }

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart> {
        return bodyPartDao.getBodyPartsByWorkoutType(idWorkoutType).let {
            bodyPartCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getBodyPartById(primaryKey: String): BodyPart? {
        return bodyPartDao.getBodyPartById(primaryKey)?.let {
            bodyPartCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getBodyParts(): List<BodyPart> {
        return bodyPartCacheMapper.entityListToDomainList(
            bodyPartDao.getBodyParts()
        )
    }

}