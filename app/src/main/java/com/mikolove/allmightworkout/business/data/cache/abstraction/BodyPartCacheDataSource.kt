package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.BodyPart

interface BodyPartCacheDataSource {

    suspend fun insertBodyPart(bodyParts: List<BodyPart>) : Long

    suspend fun removeBodyPart() : Int

    suspend fun getBodyParts(query : String, filterAndOrder : String, page : Int) : List<BodyPart>

    suspend fun getBodyPartById(primaryKey: String) : BodyPart?

    suspend fun getTotalBodyParts(idWorkoutType : String) : Int
}

