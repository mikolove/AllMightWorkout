package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class UserNetworkEntity(
    @DocumentId
    var idUser : String,
    var name : String?,
    var email : String?,
    var createdAt : Timestamp,
    var updatedAt : Timestamp
){
    constructor() : this(
        "",
        null,
        "",
        Timestamp.now(),
        Timestamp.now(),
    )
}
