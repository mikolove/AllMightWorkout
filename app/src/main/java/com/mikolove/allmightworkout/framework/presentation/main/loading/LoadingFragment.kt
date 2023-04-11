package com.mikolove.allmightworkout.framework.presentation.main.loading


import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.databinding.FragmentLoadingBinding
import com.mikolove.allmightworkout.framework.presentation.common.*

import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents
import com.mikolove.allmightworkout.framework.presentation.session.SessionLoggedType
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

const val LOADING_FRAGMENT_NO_SYNC = "User is not connected, could not sync data with firebase"

@AndroidEntryPoint
class LoadingFragment : BaseFragment(R.layout.fragment_loading) {

    val viewModel: LoadingViewModel by viewModels()

    private var binding : FragmentLoadingBinding? = null


    /*
     Switch for Session manager
     @Inject
     lateinit var mAuth : FirebaseAuth*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)

        binding = FragmentLoadingBinding.bind(view)

        binding?.connectButton?.setOnClickListener {
            if(!sessionManager.isAuth()){
                createSignInIntent()
            }
        }

        binding?.signoutButton?.setOnClickListener {
            if(sessionManager.isAuth()){
                sessionManager.onTriggerEvent(SessionEvents.Signout)
            }
        }

        if(sessionManager.isAuth()){
            val userId = sessionManager.getUserId()
            val userEmail = sessionManager.getUserEmail()
            if(userId != null && userEmail != null){
                viewModel.onTriggerEvent(LoadingEvents.LoadUser(userId, userEmail))
            }
        }

        subscribeObservers()

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    private fun subscribeObservers() {

        sessionManager.state.observe(viewLifecycleOwner) { sessionState ->

            processQueue(
                context = context,
                queue = sessionState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        sessionManager.onTriggerEvent(SessionEvents.OnRemoveHeadFromQueue)
                    }
                })

            printLogD("LoadingFrament", "Session info ${sessionState.logged} current User : ${sessionManager.getUserId()}")

        }

        viewModel.state.observe(viewLifecycleOwner) { state ->

            printLogD("LoadingFragment","Viewmodel state change")

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        viewModel.onTriggerEvent(LoadingEvents.OnRemoveHeadFromQueue)
                    }
                })

            showProgressbar(state.isLoading)

            when(state.loadingStep) {
                LoadingStep.INIT -> {
                    showInit(state.loadingStep.loadingMessage)
                }
                else -> {
                    showLoadingStep(state.loadingStep.loadingMessage)
                }

            }

        }
    }

    /*
        Firebase
     */


    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_mhalogo)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }



    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            printLogD("LoadingFragment","user is logged"+sessionManager.state.value?.logged.toString())

            //Check if user exist in database if not create it and sync
            sessionManager.isAuth().let {
                val userId = sessionManager.getUserId()
                val userEmail = sessionManager.getUserEmail()
                if(userId != null && userEmail != null){
                    viewModel.onTriggerEvent(LoadingEvents.LoadUser(userId, userEmail))
                }
            }

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            printLogD("LoadingFragment","ERROR not connected ${response?.getError()?.getErrorCode()}")
            viewModel.onTriggerEvent(LoadingEvents.LoadStep(LoadingStep.INIT))
        }
    }

    private fun showLoadingStep(loadingMessage : String){
        binding?.txtConnect?.text = loadingMessage
        binding?.connectButton?.invisible()
        binding?.signoutButton?.invisible()
    }

    private fun showInit(loadingMessage : String){
        binding?.txtConnect?.text = loadingMessage
        binding?.connectButton?.visible()
        binding?.signoutButton?.visible()
    }

    private fun showProgressbar(isLoading : Boolean){
        if(isLoading){
            binding?.mainProgressBar?.visible()
        }else{
            binding?.mainProgressBar?.invisible()
        }
    }

    private fun navigateToHistory(){
        findNavController().navigate(R.id.action_global_history_fragment)
    }
}