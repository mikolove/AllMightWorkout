@file:Suppress("OPT_IN_USAGE")

package com.mikolove.exercise.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.exercise.domain.ExerciseRepository
import com.mikolove.exercise.presentation.mapper.toExerciseUi
import com.mikolove.exercise.presentation.model.ExerciseUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class ExerciseSearchViewModel(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel(){

    var state by mutableStateOf(ExerciseSearchState())
        private set

    private val eventChannel = Channel<ExerciseSearchEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        snapshotFlow { state.searchQuerySelected.text }
            .flatMapLatest { searchQuery ->
                Timber.d("Search query: $searchQuery")
                exerciseRepository.getExercises(searchQuery.toString())
            }.map { exercises ->
                Timber.d("Exercises: $exercises")
                state = state.copy(exercises = exercises.map { it.toExerciseUi() })
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                state.exercises
            )
    }

    fun onAction(action: ExerciseSearchAction){
        when(action) {
            else -> {}
        }
    }
}