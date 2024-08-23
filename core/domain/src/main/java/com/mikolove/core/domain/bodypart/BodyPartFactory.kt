package com.mikolove.core.domain.bodypart

import java.util.UUID


class BodyPartFactory
{

    fun createBodyPart(
        idBodyPart : String = UUID.randomUUID().toString(),
        name : String
    ) : BodyPart {
        return BodyPart(
            idBodyPart = idBodyPart ,
            name = name
        )
    }

}