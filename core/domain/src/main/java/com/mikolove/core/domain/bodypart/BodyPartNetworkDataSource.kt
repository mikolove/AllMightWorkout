package com.mikolove.core.domain.bodypart

import com.mikolove.allmightworkout.business.domain.model.BodyPart

interface BodyPartNetworkDataSource {

    suspend fun getAllBodyParts(): List<BodyPart>
}