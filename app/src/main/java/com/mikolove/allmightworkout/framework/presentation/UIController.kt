package com.mikolove.allmightworkout.framework.presentation

import androidx.appcompat.view.ActionMode
import com.mikolove.allmightworkout.business.domain.state.DialogInputCaptureCallback
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback

//Used to make communication between Fragments And MainActivity
interface UIController {

    fun displayAppBar()

    fun displayBottomNavigation()

    fun displayAppBarTitle()

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    /*fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback)

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )*/

    fun loadFabController(fabController: FabController?)
}