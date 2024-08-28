package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.core.data.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.database.database.BODYPART_PAGINATION_PAGE_SIZE
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.workouttype.WorkoutType



const val FORCE_NEW_BODYPART_EXCEPTION = "FORCE_NEW_BODYPART_EXCEPTION"
const val FORCE_UPDATE_BODYPART_EXCEPTION = "FORCE_UPDATE_BODYPART_EXCEPTION"
const val FORCE_DELETE_BODYPART_EXCEPTION = "FORCE_DELETE_BODYPART_EXCEPTION"
const val FORCE_DELETES_BODYPART_EXCEPTION = "FORCE_DELETES_BODYPART_EXCEPTION"
const val FORCE_SEARCH_BODYPART_EXCEPTION = "FORCE_SEARCH_BODYPART_EXCEPTION"
const val FORCE_GET_BODYPART_BY_WORKOUT_TYPE_EXCEPTION = "FORCE_GET_BODYPART_BY_WORKOUT_TYPE_EXCEPTION"

class FakeBodyPartCacheDataSourceImpl
constructor(
    private val workoutTypesData : HashMap<String, WorkoutType>,
    private val bodyPartsData : HashMap<String, BodyPart>
) : BodyPartCacheDataSource {

    override suspend fun insertBodyPart(bodyPart: BodyPart, idWorkoutType: String): Long {
        if(bodyPart.idBodyPart.equals(FORCE_NEW_BODYPART_EXCEPTION)){
            throw Exception("Something went wrong inserting bodypart.")
        }
        if(bodyPart.idBodyPart.equals(FORCE_GENERAL_FAILURE)){
            return -1
        }
        bodyPartsData.put(bodyPart.idBodyPart, bodyPart)
        return 1
    }

    override suspend fun updateBodyPart(idBodyPart: String, name: String): Int {
        if(idBodyPart.equals(FORCE_UPDATE_BODYPART_EXCEPTION)){
            throw Exception("Something went wrong updating bodypart.")
        }
        val updatedBodyPart = BodyPart(
            idBodyPart = idBodyPart,
            name = name
        )
        return bodyPartsData.get(idBodyPart)?.let {
            bodyPartsData.put(idBodyPart,updatedBodyPart)
            1
        }?:-1
    }

    override suspend fun removeBodyPart(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_BODYPART_EXCEPTION)){
            throw Exception("Something went wrong deleting bodypart.")
        }
        if(primaryKey.equals(FORCE_DELETES_BODYPART_EXCEPTION)){
            throw Exception("Something went wrong deleting bodyparts.")
        }
        return bodyPartsData.remove(primaryKey)?.let {
            1
        }?: -1
    }

    override suspend fun getBodyParts(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<BodyPart> {

        if(query.equals(FORCE_SEARCH_BODYPART_EXCEPTION)){
            throw Exception("Something went wrong searching the cache for bodyparts")
        }

        val results : ArrayList<BodyPart> = ArrayList()
        for(bodyPart in bodyPartsData.values){
            if(bodyPart.name.contains(query)){
                results.add(bodyPart)
            }
            if(results.size > (page* BODYPART_PAGINATION_PAGE_SIZE)){
                break
            }
        }

        return results
    }

    override suspend fun getBodyPartById(primaryKey: String): BodyPart? {
        return bodyPartsData[primaryKey]
    }

    override suspend fun getTotalBodyPartsByWorkoutType(idWorkoutType: String): Int {
        return workoutTypesData[idWorkoutType]?.bodyParts?.size ?:0
    }

    override suspend fun getTotalBodyParts(): Int {
        return bodyPartsData.size
    }

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart> {

        if(idWorkoutType.equals(FORCE_GET_BODYPART_BY_WORKOUT_TYPE_EXCEPTION)){
            throw Exception("Something went wrong retieving bodyparts")
        }

        return workoutTypesData[idWorkoutType]?.bodyParts ?: ArrayList()
    }

}