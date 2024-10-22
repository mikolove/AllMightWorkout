package com.mikolove.core.network.firebase.model

import com.google.firebase.firestore.DocumentId

data class BodyPartNetworkEntity(
    @DocumentId
    var idBodyPart : String,
    var name : String
) {

    constructor() : this(
        "",
        ""
    )
}