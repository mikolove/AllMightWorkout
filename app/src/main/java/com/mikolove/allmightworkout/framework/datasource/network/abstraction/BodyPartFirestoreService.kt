package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.core.domain.bodypart.BodyPart

interface BodyPartFirestoreService {

    suspend fun getBodyParts() : List<BodyPart>

}