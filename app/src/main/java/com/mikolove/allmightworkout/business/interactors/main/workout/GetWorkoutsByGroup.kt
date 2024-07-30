package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.abstraction.GroupCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Group
import com.mikolove.allmightworkout.business.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutsByGroup (
    private val groupCacheDataSource: GroupCacheDataSource
){
    fun execute(
        idUser : String
    ) : Flow<DataState<List<Group>>> = flow{

    }
}