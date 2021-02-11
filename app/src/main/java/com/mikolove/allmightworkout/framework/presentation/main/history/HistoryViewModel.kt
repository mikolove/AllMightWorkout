package com.mikolove.allmightworkout.framework.presentation.main.history

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.history.HistoryListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
@Inject
constructor(
    private val historyListInteractors: HistoryListInteractors,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<HistoryViewState>(){

    override fun handleNewData(data: HistoryViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }

    override fun initNewViewState(): HistoryViewState {
        return HistoryViewState()
    }
}