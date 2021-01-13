package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.framework.datasource.database.BODYPART_PAGINATION_PAGE_SIZE


const val FORCE_NEW_BODYPART_EXCEPTION = "FORCE_NEW_BODYPART_EXCEPTION"
const val FORCE_DELETE_BODYPART_EXCEPTION = "FORCE_DELETE_BODYPART_EXCEPTION"
const val FORCE_DELETES_BODYPART_EXCEPTION = "FORCE_DELETES_BODYPART_EXCEPTION"
const val FORCE_SEARCH_BODYPART_EXCEPTION = "FORCE_SEARCH_BODYPART_EXCEPTION"

class FakeBodyPartCacheDataSourceImpl
constructor(
    private val bodyPartsData : HashMap<String, BodyPart>
) : BodyPartCacheDataSource{

    override suspend fun insertBodyPart(bodyPart: BodyPart): Long {
        if(bodyPart.idBodyPart.equals(FORCE_NEW_BODYPART_EXCEPTION)){
            throw Exception("Something went wrong inserting bodypart.")
        }
        if(bodyPart.idBodyPart.equals(FORCE_GENERAL_FAILURE)){
            return -1
        }
        bodyPartsData.put(bodyPart.idBodyPart,bodyPart)
        return 1
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

    override suspend fun getTotalBodyParts(idWorkoutType: String): Int {
        return bodyPartsData.size
    }

    override suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPart>? {
        var listOfBodyPartByIdWorkoutType : ArrayList<BodyPart> = ArrayList()
        for( bodyPart in bodyPartsData.values){
            if(idWorkoutType.equals(bodyPart.workoutType.idWorkoutType))
                listOfBodyPartByIdWorkoutType.add(bodyPart)
        }
        return listOfBodyPartByIdWorkoutType
    }
}