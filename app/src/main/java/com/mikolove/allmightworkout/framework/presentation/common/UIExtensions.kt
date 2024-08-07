package com.mikolove.allmightworkout.framework.presentation.common

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
/*import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.input.input*/
import com.mikolove.allmightworkout.util.printLogD
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.Queue
import com.mikolove.core.domain.state.StateMessageCallback
import com.mikolove.core.domain.state.UIComponentType

fun processQueue(
    context: Context?,
    queue: Queue<GenericMessageInfo>,
    stateMessageCallback: StateMessageCallback
) {
    context?.let { ctx ->
        if(!queue.isEmpty()){
            printLogD("UIExtensions","Queue content ${queue.items.mapIndexed { _, message -> message.id }}")
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

        is UIComponentType.None -> {
            // This would be a good place to send to your Error Reporting
            // software of choice (ex: Firebase crash reporting)
            printLogD("UIExtensions", "onResponseReceived: ${message.id}")
            stateMessageCallback.removeMessageFromQueue()
        }
    }
}

private fun Context.displayDialog(
    message: GenericMessageInfo,
    stateMessageCallback: StateMessageCallback
) {

    when(message.messageType){

        is MessageType.Success ->{
            displaySuccessDialog(
                message,
                stateMessageCallback
            )
        }
        is MessageType.Error ->{
            displayErrorDialog(
                message,
                stateMessageCallback
            )
        }
        is MessageType.Info ->{
            displayInfoDialog(
                message,
                stateMessageCallback
            )
        }
        else ->{
            printLogD("UIExtensions", "onResponseReceived: ${message.id}")
            stateMessageCallback.removeMessageFromQueue()
        }
    }
}


private fun Context.displaySuccessDialog(
    message: GenericMessageInfo,
    stateMessageCallback: StateMessageCallback
){
/*    MaterialDialog(this)
        .show{
            title(text = message.title)
            message(text = message.description)
            positiveButton(text = message.positiveAction?.positiveBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                message.positiveAction?.onPositiveAction?.let { it() }
                dismiss()
            }
            onDismiss {
                message.onDismiss?.invoke()
            }
            cancelable(false)
        }*/
}

private fun Context.displayErrorDialog(
    message: GenericMessageInfo,
    stateMessageCallback: StateMessageCallback
){
/*    MaterialDialog(this)
        .show{
            title(text = message.title)
            message(text = message.description)
            positiveButton(text = message.positiveAction?.positiveBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                message.positiveAction?.onPositiveAction?.let { it() }
                dismiss()
            }
            onDismiss {
                message.onDismiss?.invoke()
            }
            cancelable(false)
        }*/
}

private fun Context.displayInfoDialog(
    message: GenericMessageInfo,
    stateMessageCallback: StateMessageCallback
) {
/*    MaterialDialog(this)
        .show{
            title(text = message.title)
            message(text = message.description)
            positiveButton(text = message.positiveAction?.positiveBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                message.positiveAction?.onPositiveAction?.let { it() }
                dismiss()
            }
            negativeButton(text = message.negativeAction?.negativeBtnTxt){
                stateMessageCallback.removeMessageFromQueue()
                message.negativeAction?.onNegativeAction?.invoke()
                dismiss()
            }
            onCancel {
                stateMessageCallback.removeMessageFromQueue()
            }
            onDismiss {
                message.onDismiss?.invoke()
            }
            cancelable(true)
        }*/
}


private fun Context. displayInputCaptureDialog(
    message: GenericMessageInfo,
    stateMessageCallback : StateMessageCallback
){
    when(message.uiComponentType){

        is UIComponentType.InputCaptureDialog -> {

       /*     MaterialDialog(this).show {
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
                onCancel {
                    stateMessageCallback.removeMessageFromQueue()
                }
                onDismiss {
                    message.onDismiss?.invoke()
                }
                cancelable(true)
            }*/
        }

        else -> {
            stateMessageCallback.removeMessageFromQueue()
        }
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