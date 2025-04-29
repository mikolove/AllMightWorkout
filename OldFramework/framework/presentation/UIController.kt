package com.mikolove.allmightworkout.framework.presentation

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