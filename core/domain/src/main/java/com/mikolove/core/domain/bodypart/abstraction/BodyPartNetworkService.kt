package com.mikolove.core.domain.bodypart.abstraction

import com.mikolove.core.domain.bodypart.BodyPart

interface BodyPartNetworkService {

    suspend fun getBodyParts() : List<BodyPart>

}