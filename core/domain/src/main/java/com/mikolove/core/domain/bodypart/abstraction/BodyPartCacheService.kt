package com.mikolove.core.domain.bodypart.abstraction

import com.mikolove.core.domain.bodypart.BodyPart


interface BodyPartCacheService {

    suspend fun upsertBodyPart(bodyPart: List<BodyPart>, idWorkoutType: String) : LongArray

    suspend fun removeBodyPart(primaryKey: String) : Int

    suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart>

    suspend fun getBodyPartById(primaryKey: String) : BodyPart

    suspend fun getBodyParts() : List<BodyPart>
}