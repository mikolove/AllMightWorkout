package com.mikolove.core.domain.bodypart

import java.util.UUID

data class BodyPart(
    var idBodyPart: String = UUID.randomUUID().toString(),
    var name: String) {

    override fun toString(): String {
        return name.replaceFirstChar { it.uppercaseChar() }
    }
}