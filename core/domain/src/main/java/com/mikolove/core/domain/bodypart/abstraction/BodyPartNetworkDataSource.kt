package com.mikolove.core.domain.bodypart.abstraction

import com.mikolove.core.domain.bodypart.BodyPart

interface BodyPartNetworkDataSource {

    suspend fun getAllBodyParts(): List<BodyPart>
}