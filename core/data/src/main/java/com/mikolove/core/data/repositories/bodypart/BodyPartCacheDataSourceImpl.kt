package com.mikolove.core.data.repositories.bodypart

import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheService
import com.mikolove.core.domain.bodypart.BodyPart

class BodyPartCacheDataSourceImpl
constructor( private val bodyPartDaoService : BodyPartCacheService) : BodyPartCacheDataSource {

    override suspend fun upsertBodyPart(bodyParts: BodyPart, idWorkoutType: String): Long = bodyPartDaoService.upsertBodyPart(
        bodyParts,idWorkoutType
    )

    override suspend fun getBodyParts(): List<BodyPart>  = bodyPartDaoService.getBodyParts()

    override suspend fun getBodyPartById(primaryKey: String): BodyPart? = bodyPartDaoService.getBodyPartById(primaryKey)

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart> = bodyPartDaoService.getBodyPartsByWorkoutType(idWorkoutType)
}
