package com.mikolove.core.domain.bodypart

interface BodyPartNetworkDataSource {

    suspend fun getAllBodyParts(): List<BodyPart>
}