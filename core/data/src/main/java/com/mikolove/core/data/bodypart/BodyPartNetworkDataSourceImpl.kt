package com.mikolove.core.data.bodypart

import com.mikolove.core.domain.bodypart.BodyPartNetworkDataSource
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.BodyPartFirestoreService

class BodyPartNetworkDataSourceImpl
constructor(private val bodyPartFirestoreService : BodyPartFirestoreService) :
    BodyPartNetworkDataSource {

    override suspend fun getAllBodyParts(): List<BodyPart> = bodyPartFirestoreService.getBodyParts()

}