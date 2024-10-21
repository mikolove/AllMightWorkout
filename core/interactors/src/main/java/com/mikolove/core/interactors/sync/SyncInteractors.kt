package com.mikolove.core.interactors.sync

class SyncInteractors(
    //val syncNetworkConnectivity: SyncNetworkConnectivity,
    val syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
    val syncDeletedExerciseSets: SyncDeletedExerciseSets,
    val syncDeletedExercises: SyncDeletedExercises,
    val syncDeletedWorkouts: SyncDeletedWorkouts,
    val syncHistory: SyncHistory,
    val syncExercises: SyncExercises,
    val syncWorkoutGroups: SyncWorkoutGroups,
    val syncWorkouts : SyncWorkouts,
    val syncWorkoutExercises: SyncWorkoutExercises
)