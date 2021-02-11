package com.mikolove.allmightworkout.framework.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.state.DialogInputCaptureCallback
import com.mikolove.allmightworkout.business.domain.state.Response
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.framework.presentation.UIController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UIController {

    private var appBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navigation)
            .navigateUp(appBarConfiguration as AppBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun hideSoftKeyboard() {
        TODO("Not yet implemented")
    }

    override fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback) {
        TODO("Not yet implemented")
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        TODO("Not yet implemented")
    }

}