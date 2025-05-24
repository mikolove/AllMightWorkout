package com.mikolove.core.domain.loading

import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult

interface LoadingRepository {

    suspend fun loadWorkoutTypes() : EmptyResult<DataError>

    suspend fun loadGroups() : EmptyResult<DataError>
}