package com.mikolove.allmightworkout.business.domain.state


import android.view.View
import com.mikolove.allmightworkout.util.TodoCallback


sealed class UIComponentType{

    object Toast : UIComponentType()

    object Dialog: UIComponentType()

    class InputCaptureDialog(
        val callback: DialogInputCaptureCallback
    ) : UIComponentType()

    //object AreYouSureDialog: UIComponentType()

    //object AreYouSureSaveDialog: UIComponentType()

    //class SnackBar(): UIComponentType()

    //class SimpleSnackBar() : UIComponentType()

    object None: UIComponentType()
}

sealed class MessageType{

    object Success: MessageType()

    object Error: MessageType()

    object None: MessageType()

    //class Info: MessageType()
}


interface StateMessageCallback{

    fun removeMessageFromQueue()
}


/*interface AreYouSureCallback {

    fun proceed()

    fun cancel()
}

interface AreYouSureSaveCallback {

    fun proceedSave()

    fun proceedNotSave()
}


interface SnackbarUndoCallback {

    fun undo()
}

class SnackbarUndoListener
constructor(
    private val snackbarUndoCallback: SnackbarUndoCallback?
): View.OnClickListener {

    override fun onClick(v: View?) {
        snackbarUndoCallback?.undo()
    }

}

*/
interface DialogInputCaptureCallback {
    fun onTextCaptured(text: String)
}