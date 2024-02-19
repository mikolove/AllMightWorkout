package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutGroupFactory
@Inject
constructor(private val dateUtil: DateUtil){
    fun createGroup(
        idGroup : String ,
        name : String,
        createdAt : String,
        updatedAt : String,
    ) : WorkoutGroup{
        val currentDate = dateUtil.getCurrentTimestamp()
        return WorkoutGroup(
            idGroup = idGroup,
            name = name,
            createdAt = createdAt ?: currentDate,
            updatedAt = updatedAt ?: currentDate
        )
    }
}