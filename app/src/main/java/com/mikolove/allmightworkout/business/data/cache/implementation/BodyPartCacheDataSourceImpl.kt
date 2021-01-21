package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.BodyPartDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BodyPartCacheDataSourceImpl
@Inject
constructor( private val bodyPartDaoService : BodyPartDaoService) : BodyPartCacheDataSource {

    override suspend fun insertBodyPart(bodyParts: BodyPart, idWorkoutType: String): Long = bodyPartDaoService.insertBodyPart(bodyParts)

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

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart>? = bodyPartDaoService.getBodyPartsByWorkoutType(idWorkoutType)
}
