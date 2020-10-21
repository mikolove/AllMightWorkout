package com.mikolove.allmightworkout.business.domain.model


enum class BodyGroup( val idGroup : Long, val group : String) {
    HIIT(1,"Cardio Hiit"),
    DELTS( 2,"Delts"),
    CHEST(3,"Chest"),
    ARM(4,"Arm"),
    BACK(5,"Back"),
    ABS(6,"Abs"),
    LEGS(7,"Legs")
}