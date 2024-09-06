package com.mikolove.core.database.util

import androidx.room.TypeConverter
import com.mikolove.core.domain.exercise.ExerciseType

class ExerciseTypeConverter {

    @TypeConverter
    fun toExerciseType(value: String): ExerciseType {
        return ExerciseType.from(value)
    }

    @TypeConverter
    fun fromExerciseType(exerciseType : ExerciseType): String {
        return exerciseType.type
    }

}