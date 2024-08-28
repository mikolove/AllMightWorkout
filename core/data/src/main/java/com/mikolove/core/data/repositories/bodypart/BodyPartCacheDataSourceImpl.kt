package com.mikolove.core.data.repositories.bodypart

import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheService
import com.mikolove.core.domain.bodypart.BodyPart

class BodyPartCacheDataSourceImpl
constructor( private val bodyPartDaoService : BodyPartCacheService) : BodyPartCacheDataSource {

    override suspend fun insertBodyPart(bodyParts: BodyPart, idWorkoutType: String): Long = bodyPartDaoService.insertBodyPart(
        bodyParts,idWorkoutType
    )

    override suspend fun removeBodyPart(primaryKey: String): Int = bodyPartDaoService.removeBodyPart(primaryKey)

    override suspend fun updateBodyPart(idBodyPart: String, name: String): Int  = bodyPartDaoService.updateBodyPart(idBodyPart,name)

    override suspend fun getBodyParts(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<BodyPart>  {
        return bodyPartDaoService.returnOrderedQuery(query,filterAndOrder,page)
    }

    override suspend fun getBodyPartById(primaryKey: String): BodyPart? = bodyPartDaoService.getBodyPartById(primaryKey)

    override suspend fun getTotalBodyPartsByWorkoutType(idWorkoutType: String): Int = bodyPartDaoService.getTotalBodyPartsByWorkoutType(idWorkoutType)

    override suspend fun getTotalBodyParts(): Int = bodyPartDaoService.getTotalBodyParts()

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart> = bodyPartDaoService.getBodyPartsByWorkoutType(idWorkoutType)
}
