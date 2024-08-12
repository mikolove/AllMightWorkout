package com.mikolove.allmightworkout.framework.datasource.data

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.workouttype.WorkoutTypeFactory
import java.io.IOException
import java.io.InputStream

class WorkoutTypeDataFactory
constructor(
    private val application: Context,
    private val workoutTypeFactory : WorkoutTypeFactory
){

    fun produceListOfWorkoutTypes(): List<WorkoutType>{
        val datas: List<WorkoutType> = Gson()
            .fromJson(
                getWorkoutTypesFromFile("workouttype_list.json"),
                object: TypeToken<List<WorkoutType>>() {}.type
            )
        return datas
    }

    fun produceEmptyListOfWorkoutTypes(): List<WorkoutType>{
        return ArrayList()
    }

    fun getWorkoutTypesFromFile(fileName: String): String? {
        return readJSONFromAsset(fileName)
    }

    private fun readJSONFromAsset(fileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use{it.readText()}
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun createWorkoutType( idWorkoutType : String,
                           name: String,
                           bodyParts : List<BodyPart>?
    ) = workoutTypeFactory.createWorkoutType(idWorkoutType = idWorkoutType ,name = name, bodyParts = bodyParts)


}