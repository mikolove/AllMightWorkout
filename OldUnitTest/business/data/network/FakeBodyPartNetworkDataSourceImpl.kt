package com.mikolove.allmightworkout.business.data.network

import com.mikolove.core.data.bodypart.abstraction.BodyPartNetworkDataSource
import com.mikolove.core.domain.bodypart.BodyPart

class FakeBodyPartNetworkDataSourceImpl(
    private val bodyPartsData : HashMap<String, BodyPart>
) : BodyPartNetworkDataSource {

    override suspend fun getAllBodyParts(): List<BodyPart> {
        return ArrayList<BodyPart>(bodyPartsData.values)
    }
}