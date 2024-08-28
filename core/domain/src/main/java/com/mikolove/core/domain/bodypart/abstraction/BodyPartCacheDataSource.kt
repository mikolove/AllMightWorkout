package com.mikolove.core.domain.bodypart.abstraction

import com.mikolove.core.domain.bodypart.BodyPart


interface BodyPartCacheDataSource {

    suspend fun insertBodyPart(bodyParts: BodyPart, idWorkoutType: String) : Long

    suspend fun updateBodyPart(idBodyPart: String, name: String) : Int

    suspend fun removeBodyPart(primaryKey: String) : Int

    suspend fun getBodyParts(query : String, filterAndOrder : String, page : Int) : List<BodyPart>

    suspend fun getBodyPartsByWorkoutType(idWorkoutType: String ): List<BodyPart>

    suspend fun getBodyPartById(primaryKey: String) : BodyPart?

    suspend fun getTotalBodyParts() : Int

    suspend fun getTotalBodyPartsByWorkoutType(idWorkoutType: String) : Int
}

