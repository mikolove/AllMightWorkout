package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.framework.datasource.cache.database.BODYPART_PAGINATION_PAGE_SIZE

interface BodyPartDaoService {

    suspend fun insertBodyPart(bodyPart: BodyPart, idWorkoutType: String) : Long

    suspend fun updateBodyPart(idBodyPart: String, name: String): Int

    suspend fun removeBodyPart(primaryKey: String) : Int

    suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart>

    suspend fun getBodyPartById(primaryKey: String) : BodyPart?

    suspend fun getTotalBodyPartsByWorkoutType(idWorkoutType: String) : Int

    suspend fun getTotalBodyParts() : Int

    suspend fun getBodyParts() : List<BodyPart>

    suspend fun getBodyPartOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = BODYPART_PAGINATION_PAGE_SIZE
    ): List<BodyPart>

    suspend fun getBodyPartOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = BODYPART_PAGINATION_PAGE_SIZE
    ): List<BodyPart>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<BodyPart>
}