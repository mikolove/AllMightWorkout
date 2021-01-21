package com.mikolove.allmightworkout.business.domain.model

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


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