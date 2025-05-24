package com.mikolove.core.network.firebase.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.Timestamp

data class GroupNetworkEntity (
    @DocumentId
    var idWorkoutGroup : String,
    var name : String,
){
    constructor() : this(
        "",
        "",
    )
}