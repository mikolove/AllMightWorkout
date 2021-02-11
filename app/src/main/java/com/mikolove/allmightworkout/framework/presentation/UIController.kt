package com.mikolove.allmightworkout.framework.presentation

import com.mikolove.allmightworkout.business.domain.state.DialogInputCaptureCallback
import com.mikolove.allmightworkout.business.domain.state.Response
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback

//Used to make communication between Fragments And MainActivity
interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback)

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}