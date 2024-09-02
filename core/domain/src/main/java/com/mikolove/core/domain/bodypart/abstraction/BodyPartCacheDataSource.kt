package com.mikolove.core.domain.bodypart.abstraction

import com.mikolove.core.domain.bodypart.BodyPart


interface BodyPartCacheDataSource {

    suspend fun upsertBodyPart(bodyParts: BodyPart, idWorkoutType: String) : Long

    suspend fun getBodyParts() : List<BodyPart>

    suspend fun getBodyPartsByWorkoutType(idWorkoutType: String ): List<BodyPart>

    suspend fun getBodyPartById(primaryKey: String) : BodyPart?

}

