package com.mikolove.allmightworkout.oldCode

/*
class ViewModelFactory
@Inject
constructor(
    private val homeListInteractors: HomeListInteractors,
    private val historyListInteractors: HistoryListInteractors,
    private val manageExerciseListInteractors: ManageExerciseListInteractors,
    private val managerWorkoutListInteractors: ManageWorkoutListInteractors,
    private val workoutListInteractors: WorkoutListInteractors,
    private val workoutFactory: WorkoutFactory,
    private val exerciseFactory: ExerciseFactory,
    private val exerciseSetFactory: ExerciseSetFactory,
    private val workoutTypeFactory: WorkoutTypeFactory,
    private val bodyPartFactory: BodyPartFactory,
    private val historyWorkoutFactory: HistoryWorkoutFactory,
    private val historyExerciseFactory: HistoryExerciseFactory,
    private val historyExerciseSetFactory: HistoryExerciseSetFactory,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){

            HomeViewModel::class.java -> {
                HomeViewModel(
                    homeListInteractors = homeListInteractors,
                    editor = editor,
                    sharedPreferences = sharedPreferences
                ) as T
            }

            HistoryViewModel::class.java -> {
                HistoryViewModel(
                    historyListInteractors = historyListInteractors,
                    editor = editor,
                    sharedPreferences = sharedPreferences
                ) as T
            }

            ManageWorkoutViewModel::class.java -> {
                ManageWorkoutViewModel(
                    manageWorkoutListInteractors = managerWorkoutListInteractors,
                    workoutFactory = workoutFactory,
                    exerciseFactory = exerciseFactory,
                    exerciseSetFactory = exerciseSetFactory,
                    editor = editor,
                    sharedPreferences = sharedPreferences
                ) as T
            }

            ManageExerciseViewModel::class.java -> {
                ManageExerciseViewModel(
                    manageExerciseListInteractors = manageExerciseListInteractors,
                    exerciseFactory = exerciseFactory,
                    exerciseSetFactory = exerciseSetFactory,
                    bodyPartFactory = bodyPartFactory,
                    workoutTypeFactory = workoutTypeFactory,
                    editor = editor,
                    sharedPreferences = sharedPreferences
                ) as T
            }

            WorkoutViewModel::class.java -> {
                WorkoutViewModel(
                    workoutListInteractors = workoutListInteractors,
                    historyWorkoutFactory = historyWorkoutFactory,
                    historyExerciseFactory = historyExerciseFactory,
                    historyExerciseSetFactory = historyExerciseSetFactory,
                    editor = editor,
                    sharedPreferences = sharedPreferences
                ) as T
            }

            else -> {
                throw IllegalArgumentException("unknown model class $modelClass")
            }
        }
    }
}*/
