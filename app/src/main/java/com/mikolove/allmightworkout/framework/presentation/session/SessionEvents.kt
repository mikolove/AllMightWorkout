package com.mikolove.allmightworkout.framework.presentation.session

import androidx.work.WorkInfo
import com.mikolove.allmightworkout.business.domain.model.User

sealed class SessionEvents {

    object GetAuthState : SessionEvents()

    object SyncEverything : SessionEvents()

    data class SyncResult(val workInfo : WorkInfo) : SessionEvents()

    data class Signout(val googleAuthUiClient: GoogleAuthUiClient) : SessionEvents()

    object OnRemoveHeadFromQueue: SessionEvents()
}