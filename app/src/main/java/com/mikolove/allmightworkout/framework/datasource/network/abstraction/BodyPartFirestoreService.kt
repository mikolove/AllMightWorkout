package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.BodyPart

interface BodyPartFirestoreService {

    suspend fun getBodyParts(name : String) : List<BodyPart>

}