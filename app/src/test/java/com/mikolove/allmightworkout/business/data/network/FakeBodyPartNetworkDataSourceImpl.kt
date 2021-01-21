package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.BodyPartNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart

class FakeBodyPartNetworkDataSourceImpl(
    private val bodyPartsData : HashMap<String, BodyPart>
) : BodyPartNetworkDataSource{

    override suspend fun getAllBodyParts(): List<BodyPart> {
        return ArrayList<BodyPart>(bodyPartsData.values)
    }
}