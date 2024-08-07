package com.mikolove.core.data.datastore

interface AppDataStore {

    suspend fun setValue(
        key: String,
        value: String
    )

    suspend fun readValue(
        key: String,
    ): String?

}