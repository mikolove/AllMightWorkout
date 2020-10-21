package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.model.BodyGroup.*

enum class BodyPart( idWorkoutType: Long, type: String, workoutGroup : BodyGroup) {
    TRAPS(1, "Traps", BACK),
    DELT_FRONT(2, "Front Delt",DELTS),
    DELT_BACK(3, "Back Delt",DELTS),
    DELT_MIDDLE(4, "Middle Delt",DELTS),
    CHEST_UPPER(5, "Upper Chest",CHEST),
    CHEST_MIDDLE(6, "Middle Chest",CHEST),
    CHEST_LOWER(7, "Lower Chest",CHEST),
    BICEPS(8, "Biceps",ARM),
    TRICEPS(9, "Triceps",ARM),
    FOREARM(10, "Forearm",ARM),
    LATS(11, "Lats",BACK),
    BACK_UPPER(12, "Upper Back",BACK),
    BACK_LOWER(13, "Lower Back",BACK),
    ABS_CORE(14, "Core Abs",ABS),
    ABS_OBLIQUE(15, "Oblique",ABS),
    ABS_LOWER(16, "Lower Abs",ABS),
    GLUTES(17, "Glutes",LEGS),
    HAMSTRING(18, "Hamstring",LEGS),
    HIIT_ROWER(19,"Rower",HIIT),
    HIIT_MOUNTAIN_CLIMBERS(20,"Mountain Climbers",HIIT),
    HIIT_BURPEES(21,"Burpees",HIIT),
    HIIT_THRUSTERS(22,"Thrusters",HIIT)
}