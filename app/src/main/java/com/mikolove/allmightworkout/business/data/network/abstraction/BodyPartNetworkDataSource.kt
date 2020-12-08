package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.BodyPart

interface BodyPartNetworkDataSource {

    suspend fun getBodyParts() : List<BodyPart>
}