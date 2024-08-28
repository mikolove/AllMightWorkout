package com.mikolove.core.domain.bodypart.abstraction

import com.mikolove.core.domain.bodypart.BodyPart


interface BodyPartCacheService {

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
        pageSize: Int
    ): List<BodyPart>

    suspend fun getBodyPartOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<BodyPart>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<BodyPart>
}