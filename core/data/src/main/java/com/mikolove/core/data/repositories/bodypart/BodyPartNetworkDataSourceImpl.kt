package com.mikolove.core.data.repositories.bodypart

import com.mikolove.core.domain.bodypart.BodyPart

class BodyPartNetworkDataSourceImpl
constructor(private val bodyPartService : BodyPartNetworkService) :
    BodyPartNetworkDataSource {

    override suspend fun getAllBodyParts(): List<BodyPart> = bodyPartService.getBodyParts()

}