package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.BodyPartDao
import com.mikolove.core.database.mappers.toBodyPart
import com.mikolove.core.database.mappers.toBodyPartCacheEntity
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheService

class BodyPartDaoService(
    private val bodyPartDao : BodyPartDao,
) : BodyPartCacheService {

    override suspend fun upsertBodyPart(bodyPart: BodyPart,idWorkoutType: String): Long {
        return bodyPartDao.upsertBodyPart(bodyPart.toBodyPartCacheEntity(idWorkoutType))
    }

    override suspend fun removeBodyPart(primaryKey: String): Int {
        return bodyPartDao.removeBodyPart(primaryKey)
    }

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart> {
        return bodyPartDao.getBodyPartsByWorkoutType(idWorkoutType).map { it.toBodyPart() }
    }

    override suspend fun getBodyPartById(primaryKey: String): BodyPart {
        return bodyPartDao.getBodyPartById(primaryKey).toBodyPart()
    }

    override suspend fun getBodyParts(): List<BodyPart> {
        return bodyPartDao.getBodyParts().map { it.toBodyPart() }
    }

}