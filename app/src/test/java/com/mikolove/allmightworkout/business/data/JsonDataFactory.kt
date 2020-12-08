package com.mikolove.allmightworkout.business.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

abstract class JsonDataFactory<T>(
    private val filename : String,
    private val testClassLoader: ClassLoader
) {

    fun produceListOfT(myClass : Class<T>): List<T>{

        //at compile time Gson cannot identify T as workout so we use this
        val collectionType = TypeToken.getParameterized(
            List::class.java,
            myClass
        ).type

        val listOfT: List<T> = Gson()
            .fromJson(
                getTFromFile(filename+".json"),
                collectionType//object: TypeToken<List<T>>() {}.type
            )
        return listOfT
    }

    abstract fun produceHashMapOfT(tList: List<T>): HashMap<String, T>

    fun produceEmptyListOfT(): List<T>{
        return ArrayList()
    }

    fun getTFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}