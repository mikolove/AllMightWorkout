package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.BodyPart

interface BodyPartCacheDataSource {

    suspend fun insertBodyPart(bodyParts: BodyPart) : Long

    suspend fun removeBodyPart(primaryKey: String) : Int

    suspend fun getBodyParts(query : String, filterAndOrder : String, page : Int) : List<BodyPart>

    suspend fun getBodyPartsByWorkoutType( idWorkoutType: String ) : List<BodyPart>?

    suspend fun getBodyPartById(primaryKey: String) : BodyPart?

    suspend fun getTotalBodyParts() : Int

    suspend fun getTotalBodyPartsByWorkoutType(idWorkoutType: String) : Int
}

