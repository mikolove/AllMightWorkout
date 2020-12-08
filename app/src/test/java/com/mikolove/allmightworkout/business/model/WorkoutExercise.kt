package com.mikolove.allmightworkout.business.model

//Model for testing not sure if would have same thing in buisness package using room entity and mapper in presentation
data class WorkoutExercise(
    var idWorkout : String,
    var idExercise : String,
    var order : Int,
    var created_at : String
    )