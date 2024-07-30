package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.Timestamp

data class GroupNetworkEntity (
    @DocumentId
    var idWorkoutGroup : String,
    var name : String,
    var createdAt : Timestamp,
    var updatedAt : Timestamp,
){
    constructor() : this(
        "",
        "",
        Timestamp.now(),
        Timestamp.now(),
    )
}