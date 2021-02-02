package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.BodyPartDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.BodyPartDao
import com.mikolove.allmightworkout.framework.datasource.cache.database.returnOrderedQuery
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.BodyPartCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BodyPartDaoServiceImpl
@Inject
constructor(
    private val bodyPartDao : BodyPartDao,
    private val bodyPartCacheMapper: BodyPartCacheMapper
) : BodyPartDaoService {

    override suspend fun insertBodyPart(bodyPart: BodyPart): Long {
        return bodyPartDao.insertBodyPart(bodyPartCacheMapper.mapToEntity(bodyPart))
    }

    override suspend fun updateBodyPart(idBodyPart: String, name: String): Int {
        return bodyPartDao.updateBodyPart(
            idBodyPart = idBodyPart,
            name = name
        )
    }

    override suspend fun removeBodyPart(primaryKey: String): Int {
        return bodyPartDao.removeBodyPart(primaryKey)
    }

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart>? {
        return bodyPartDao.getBodyPartsByWorkoutType(idWorkoutType)?.let {
            bodyPartCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getBodyPartById(primaryKey: String): BodyPart? {
        return bodyPartDao.getBodyPartById(primaryKey)?.let {
            bodyPartCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getTotalBodyPartsByWorkoutType(idWorkoutType: String): Int {
        return bodyPartDao.getTotalBodyPartsByWorkoutType(idWorkoutType)
    }

    override suspend fun getTotalBodyParts(): Int {
        return bodyPartDao.getTotalBodyParts()
    }

    override suspend fun getBodyParts(): List<BodyPart> {
        return bodyPartCacheMapper.entityListToDomainList(
            bodyPartDao.getBodyParts()
        )
    }

    override suspend fun getBodyPartOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<BodyPart> {
        return bodyPartCacheMapper.entityListToDomainList(
            bodyPartDao.getBodyPartOrderByNameDESC(query, page)
        )
    }

    override suspend fun getBodyPartOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<BodyPart> {
        return bodyPartCacheMapper.entityListToDomainList(
            bodyPartDao.getBodyPartOrderByNameASC(query, page)
        )
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<BodyPart> {
        return bodyPartCacheMapper.entityListToDomainList(
            bodyPartDao.returnOrderedQuery(query, filterAndOrder, page)
        )
    }
}