package com.mikolove.allmightworkout.framework.presentation.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.mikolove.core.domain.state.StateMessageCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// threshold for when contents of collapsing toolbar is hidden
const val COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD = -75
const val CLICK_THRESHOLD = 150L // a click is considered 150ms or less
const val CLICK_COLOR_CHANGE_TIME = 250L

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun View.fadeIn(callback: (() -> Unit)? = null) {
    val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    apply {
        visible()
        alpha = 0f
        animate()
            .alpha(1f)
            .setDuration(animationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    callback?.invoke()
                }
            })
    }
}

fun View.fadeOut(callback: (() -> Unit)? = null){
    val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    apply {
        animate()
            .alpha(0f)
            .setDuration(animationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    invisible()
                    callback?.invoke()
                }
            })
    }
}

fun View.onSelectChangeColor(
    lifeCycleScope: CoroutineScope,
    clickColor: Int
) = CoroutineScope(lifeCycleScope.coroutineContext).launch {
    val intialColor = (background as ColorDrawable).color
    setBackgroundColor(
        ContextCompat.getColor(
            context,
            clickColor
        )
    )
    delay(CLICK_COLOR_CHANGE_TIME)
    setBackgroundColor(intialColor)
}

fun View.changeColor(newColor: Int) {
    setBackgroundColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun EditText.disableContentInteraction() {
    keyListener = null
    isFocusable = false
    isFocusableInTouchMode = false
    isCursorVisible = false
    //setBackgroundResource(android.R.color.transparent)
    clearFocus()
}

fun EditText.enableContentInteraction() {
    keyListener = EditText(context).keyListener
    isFocusable = true
    isFocusableInTouchMode = true
    isCursorVisible = true
    //setBackgroundResource(android.R.color.white)
    requestFocus()
    if(text != null){
        setSelection(text.length)
    }
}



fun Activity.displayToast(
    @StringRes message:Int,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromQueue()
}

fun Activity.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromQueue()
}