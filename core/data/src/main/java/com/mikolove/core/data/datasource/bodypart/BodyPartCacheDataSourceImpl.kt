package com.mikolove.core.data.datasource.bodypart

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheService

class BodyPartCacheDataSourceImpl
constructor( private val bodyPartCacheService : BodyPartCacheService) : BodyPartCacheDataSource {

    override suspend fun upsertBodyPart(bodyPart: List<BodyPart>, idWorkoutType: String): LongArray
    = bodyPartCacheService.upsertBodyPart(bodyPart,idWorkoutType)

    override suspend fun removeBodyPart(primaryKey: String): Int = bodyPartCacheService.removeBodyPart(primaryKey)

    override suspend fun getBodyParts(): List<BodyPart>  = bodyPartCacheService.getBodyParts()

    override suspend fun getBodyPartById(primaryKey: String): BodyPart = bodyPartCacheService.getBodyPartById(primaryKey)

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart> = bodyPartCacheService.getBodyPartsByWorkoutType(idWorkoutType)
}
