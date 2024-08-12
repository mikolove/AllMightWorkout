package com.mikolove.core.domain.bodypart

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BodyPartFactory
@Inject
constructor()
{

    fun createBodyPart(
        idBodyPart : String?,
        name : String?
    ) : BodyPart {
        return BodyPart(
            idBodyPart = idBodyPart ?: "",
            name = name ?: "New bodyPart"
        )
    }

}