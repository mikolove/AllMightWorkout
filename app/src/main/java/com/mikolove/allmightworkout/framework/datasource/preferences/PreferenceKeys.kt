package com.mikolove.allmightworkout.framework.datasource.preferences

class PreferenceKeys {

    //To store filter preferences of the user
    companion object{

        // Shared Preference Files:
        const val WORKOUT_LIST_PREFERENCES: String = "com.mikolove.allmightworkout.workouts"

        // Shared Preference Keys
        val WORKOUT_LIST_FILTER: String = "${WORKOUT_LIST_PREFERENCES}.WORKOUT_LIST_FILTER"
        val WORKOUT_LIST_ORDER: String = "${WORKOUT_LIST_PREFERENCES}.WORKOUT_LIST_ORDER"

        val EXERCISE_LIST_FILTER: String = "${WORKOUT_LIST_PREFERENCES}.EXERCISE_LIST_FILTER"
        val EXERCISE_LIST_ORDER: String = "${WORKOUT_LIST_PREFERENCES}.EXERCISE_LIST_ORDER"

    }
}