package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.BodyPartNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.BodyPartFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BodyPartNetworkDataSourceImpl
@Inject
constructor(private val bodyPartFirestoreService : BodyPartFirestoreService) : BodyPartNetworkDataSource{

    override suspend fun getBodyParts(): List<BodyPart>  = bodyPartFirestoreService.getBodyParts()

}