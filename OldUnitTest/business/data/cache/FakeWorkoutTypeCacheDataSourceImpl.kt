package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.core.data.workouttype.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUTTYPE_PAGINATION_PAGE_SIZE

const val FORCE_NEW_WORKOUTTYPE_EXCEPTION = "FORCE_NEW_WORKOUTTYPE_EXCEPTION"
const val FORCE_UPDATE_WORKOUTTYPE_EXCEPTION = "FORCE_UPDATE_WORKOUTTYPE_EXCEPTION"
const val FORCE_DELETE_WORKOUTTYPE_EXCEPTION = "FORCE_DELETE_WORKOUTTYPE_EXCEPTION"
const val FORCE_DELETES_WORKOUTTYPE_EXCEPTION = "FORCE_DELETES_WORKOUTTYPE_EXCEPTION"
const val FORCE_SEARCH_WORKOUTTYPE_EXCEPTION = "FORCE_SEARCH_WORKOUTTYPE_EXCEPTION"


class FakeWorkoutTypeCacheDataSourceImpl(
    private val workoutTypeDatas : HashMap<String, WorkoutType>
) : WorkoutTypeCacheDataSource {

    override suspend fun insertWorkoutType(workoutType: WorkoutType): Long {
        if(workoutType.idWorkoutType.equals(FORCE_NEW_WORKOUTTYPE_EXCEPTION)){
            throw Exception("Something went wrong inserting workoutType.")
        }
        if(workoutType.idWorkoutType.equals(FORCE_GENERAL_FAILURE)){
            return -1
        }
        workoutTypeDatas.put(workoutType.idWorkoutType,workoutType)
        return 1
    }

    override suspend fun updateWorkoutType(idWorkoutType: String, name: String): Int {
            if(idWorkoutType.equals(FORCE_UPDATE_WORKOUTTYPE_EXCEPTION)){
                throw Exception("Something went wrong updating workoutType.")
            }
            val updatedWorkoutType = WorkoutType(
                idWorkoutType = idWorkoutType,
                name = name,
                bodyParts = null
            )
            return workoutTypeDatas.get(idWorkoutType)?.let {
                workoutTypeDatas.put(idWorkoutType,updatedWorkoutType)
                1
            }?:-1

    }

    override suspend fun removeWorkoutType(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_WORKOUTTYPE_EXCEPTION)){
            throw Exception("Something went wrong deleting workoutType.")
        }
        if(primaryKey.equals(FORCE_DELETES_WORKOUTTYPE_EXCEPTION)){
            throw Exception("Something went wrong deleting workoutTypes.")
        }
        return workoutTypeDatas.remove(primaryKey)?.let {
            return 1
        } ?: -1
    }

    override suspend fun getWorkoutTypes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<WorkoutType> {

        if(query.equals(FORCE_SEARCH_WORKOUTTYPE_EXCEPTION)){
            throw Exception("Something went wrong searching the cache for workoutType")
        }

        val results : ArrayList<WorkoutType> = ArrayList()
        for(workoutType in workoutTypeDatas.values){
            if(workoutType.name.contains(query)){
                results.add(workoutType)
            }
            if(results.size > (page* WORKOUTTYPE_PAGINATION_PAGE_SIZE))
                break
        }

        return results
    }

    override suspend fun getWorkoutTypeById(primaryKey: String): WorkoutType? {
       return workoutTypeDatas[primaryKey]
    }

    override suspend fun getTotalWorkoutTypes(): Int {
        return workoutTypeDatas.size
    }

    override suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String?): WorkoutType? {
        var workoutType : WorkoutType? = null
        for(wT in workoutTypeDatas.values){
            wT.bodyParts?.let {
                for( bP in it){
                    if(bP.idBodyPart == idBodyPart)
                        workoutType = wT
                }
            }
        }
        return workoutType
    }
}