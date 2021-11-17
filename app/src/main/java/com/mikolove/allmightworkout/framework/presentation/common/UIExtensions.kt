package com.mikolove.allmightworkout.framework.presentation.common

import android.content.Context
import android.text.InputType
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.input.input
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.util.printLogD

fun processQueue(
    context: Context?,
    queue: Queue<GenericMessageInfo>,
    stateMessageCallback: StateMessageCallback
) {
    context?.let { ctx ->
        if(!queue.isEmpty()){
            printLogD("UIExtensions","Queue content ${queue.items.mapIndexed { index, message -> message.id }}")
            queue.peek()?.let { message ->
                ctx.onResponseReceived(
                    message = message,
                    stateMessageCallback = stateMessageCallback
                )
            }
        }
    }
}

private fun Context.onResponseReceived(
    message : GenericMessageInfo,
    stateMessageCallback: StateMessageCallback
) {
    when(message.uiComponentType){

        is UIComponentType.Toast -> {
            message.description?.let {
                displayToast(
                    message = it,
                    stateMessageCallback = stateMessageCallback
                )
            }
        }

        is UIComponentType.Dialog -> {
            displayDialog(
                message = message,
                stateMessageCallback = stateMessageCallback
            )
        }

        is UIComponentType.InputCaptureDialog ->{
            displayInputCaptureDialog(
                message = message,
                stateMessageCallback = stateMessageCallback
            )
        }
  /*      is UIComponentType.AreYouSureDialog -> {

            areYouSureDialog(
                message = message,
                callback = message.uiComponentType.callback,
                stateMessageCallback = stateMessageCallback
            )
        }*/


        is UIComponentType.None -> {
            // This would be a good place to send to your Error Reporting
            // software of choice (ex: Firebase crash reporting)
            printLogD("UIExtensions", "onResponseReceived: ${message.id}")
            stateMessageCallback.removeMessageFromQueue()
        }
    }
}


/*private fun Context.displayDialog(
   message: GenericMessageInfo,
   stateMessageCallback: StateMessageCallback
){
  when (message.messageType) {

       *//*is MessageType.Info -> {
            displayInfoDialog(
                message = message,
                stateMessageCallback = stateMessageCallback
            )
        }*//*

        is MessageType.Success -> {
            displaySuccessDialog(
                message = message,
                stateMessageCallback = stateMessageCallback
            )
        }

        is MessageType.Error -> {
            displayErrorDialog(
                message = message,
                stateMessageCallback = stateMessageCallback
            )
        }

        else -> {
            stateMessageCallback.removeMessageFromQueue()
        }
    }
}*/

/*private fun Context.displaySuccessDialog(
    message: GenericMessageInfo?,
    stateMessageCallback: StateMessageCallback
) {
    MaterialDialog(this)
        .show{
            title(text = message?.title)
            message(text = message?.description)
            positiveButton(text = message?.positiveAction?.positiveBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}*/

private fun Context.displayDialog(
    message: GenericMessageInfo?,
    stateMessageCallback: StateMessageCallback
) {

    MaterialDialog(this)
        .show{
            title(text = message?.title)
            message(text = message?.description)
            positiveButton(text = message?.positiveAction?.positiveBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                message?.positiveAction?.onPositiveAction?.let { it() }
                dismiss()
            }
            negativeButton(text = message?.negativeAction?.negativeBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                message?.negativeAction?.onNegativeAction?.invoke()
                dismiss()
            }
            onDismiss {
                message?.onDismiss?.invoke()
                //dismiss()
            }
            cancelable(false)
        }
}

private fun Context. displayInputCaptureDialog(
    message: GenericMessageInfo,
    stateMessageCallback : StateMessageCallback
){

    //printLogD("UIExtensions","${message.uiComponentType.toString()}")
    when(message.uiComponentType){

        is UIComponentType.InputCaptureDialog -> {

            printLogD("UIExtensions","Launch material dialog")
            MaterialDialog(this).show {
                title(text = message.title)
                input(
                    waitForPositiveButton = true,
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                ) { _, text ->
                    message.uiComponentType.callback.onTextCaptured(text.toString())
                }
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromQueue()
                    dismiss()
                }
                onDismiss {
                }
                cancelable(true)
            }
        }

        else -> {
            stateMessageCallback.removeMessageFromQueue()
        }
    }
}

private fun Context.displayInfoDialog(
    message: GenericMessageInfo,
    stateMessageCallback: StateMessageCallback
) {
    MaterialDialog(this)
        .show{
            title(text = message?.title)
            message(text = message?.description)
            positiveButton(text = message?.positiveAction?.positiveBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

fun Context.areYouSureDialog(
    message: GenericMessageInfo,
    stateMessageCallback: StateMessageCallback
) {
    MaterialDialog(this)
        .show{
            title(text = message?.title)
            message(text = message?.description)
            negativeButton(text = message?.negativeAction?.negativeBtnTxt){
                message?.negativeAction?.onNegativeAction?.invoke()
                dismiss()
            }
            positiveButton(text = message?.positiveAction?.positiveBtnTxt){
                message?.positiveAction?.onPositiveAction?.invoke()
                dismiss()
            }
            onDismiss {
            }
            cancelable(false)
        }
}

fun Context.displayToast(
    @StringRes message:Int,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromQueue()
}

fun Context.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromQueue()
}