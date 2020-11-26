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

    override suspend fun insertBodyPart(bodyParts: List<BodyPart>): Long = bodyPartDaoService.insertBodyPart(bodyParts)

    override suspend fun removeBodyPart(): Int = bodyPartDaoService.removeBodyPart()

    override suspend fun getBodyParts(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<BodyPart>  {
        //TODO implement
        return listOf()
    }

    override suspend fun getBodyPartById(primaryKey: String): BodyPart? = bodyPartDaoService.getBodyPartById(primaryKey)

    override suspend fun getTotalBodyParts(idWorkoutType : String): Int = bodyPartDaoService.getTotalBodyParts(idWorkoutType)

}
