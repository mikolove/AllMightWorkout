package com.mikolove.allmightworkout.framework.datasource.network.model

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