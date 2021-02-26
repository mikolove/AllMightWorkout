package com.mikolove.allmightworkout.framework.presentation.main.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel
@Inject
constructor(
    private val networkSyncManager: NetworkSyncManager
) : ViewModel() {

    init {
        syncCacheWithNetwork()
    }

    fun hasSyncBeenExecuted() = networkSyncManager.hasSyncBeenExecuted

    private fun syncCacheWithNetwork() {
        networkSyncManager.executeDataSync(viewModelScope)
    }
}