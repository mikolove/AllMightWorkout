package com.mikolove.core.presentation.designsystem.components


import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


/*@Composable
fun QueueProcessing(
    name : String,
    queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
    onRemoveHeadFromQueue: () -> Unit,
    progressBarState: Boolean = false,
    snackbarHostState : SnackbarHostState,
    content: @Composable () -> Unit,
){

        val scope = rememberCoroutineScope()

        Box() {

            // Screen content
            if(progressBarState){
                //Deal with progressbar
            }

            content()

            if(!queue.isEmpty()){
                printLogD("QueueProcessing ${name} :","Queue content : ${queue.items.mapIndexed { _, message -> message.id }}")
                queue.peek()?.let { message ->

                    when(message.uiComponentType){

                        is UIComponentType.Toast ->{
                            message.description?.let {description ->
                                scope.launch {
                                    onRemoveHeadFromQueue()
                                    snackbarHostState.showSnackbar(description)
                                }
                            }
                        }

                        is UIComponentType.Dialog ->{

                            when(message.messageType){

                                is MessageType.Info->{
                                    DisplayAskDialog(
                                        onDismissRequest = {
                                            onRemoveHeadFromQueue()
                                            message.onDismiss?.invoke()
                                        },
                                        onPositiveAction = {
                                            onRemoveHeadFromQueue()
                                            message.positiveAction?.onPositiveAction?.invoke()
                                        },
                                        onNegativeAction = {
                                            onRemoveHeadFromQueue()
                                            message.negativeAction?.onNegativeAction?.invoke()
                                        },
                                        dialogTitle = message.title,
                                        dialogText = message.description ?: "",
                                        dialogPositiveText = message.positiveAction?.positiveBtnTxt ?: "Confirm",
                                        dialogNegativeText = message.negativeAction?.negativeBtnTxt ?: "Dismiss")
                                }

                                is MessageType.Success, MessageType.Error->{
                                    DisplayInfoDialog(
                                        onPositiveAction = {
                                            onRemoveHeadFromQueue()
                                            message.positiveAction?.onPositiveAction?.invoke()
                                        },
                                        onDismissRequest = {
                                            onRemoveHeadFromQueue()
                                            message.onDismiss?.invoke()
                                        },
                                        dialogTitle = message.title,
                                        dialogText = message.description ?: "",
                                        dialogPositiveText = message.positiveAction?.positiveBtnTxt ?: "Confirm")
                                }
                                else->{
                                    onRemoveHeadFromQueue()
                                }
                            }
                        }

                        is UIComponentType.InputCaptureDialog->{}

                        is UIComponentType.None->{
                            onRemoveHeadFromQueue()
                        }

                    }
                }
            }
        }

}


@Composable
fun DisplayAskDialog(
    onPositiveAction: () -> Unit,
    onNegativeAction:() -> Unit,
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    dialogPositiveText : String,
    dialogNegativeText : String
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveAction()
                }
            ) {
                Text(dialogPositiveText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onNegativeAction()
                }
            ) {
                Text(dialogNegativeText)
            }
        }
    )
}

@Composable
fun DisplayInfoDialog(
    onPositiveAction: () -> Unit,
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    dialogPositiveText : String,
){
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveAction()
                }
            ) {
                Text(dialogPositiveText)
            }
        }
    )
}*/