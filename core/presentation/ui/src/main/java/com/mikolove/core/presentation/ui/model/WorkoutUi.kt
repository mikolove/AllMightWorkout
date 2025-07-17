package com.mikolove.core.presentation.ui.model

data class WorkoutUi(
    val idWorkout : String ="",
    val name: String ="",
    val exercises: List<ExerciseUi> = emptyList(),
    val groups: List<GroupUi> = emptyList(),
    val isActive: Boolean = true,
    val startedAt: String? ="",
    val endedAt: String? ="",
    val createdAt: String ="",
    val updatedAt: String =""
)