package com.mikolove.allmightworkout.framework.presentation.main.loading


import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.databinding.FragmentLoadingBinding
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth

import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.fadeIn
import com.mikolove.allmightworkout.framework.presentation.common.processQueue
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents
import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents
import com.mikolove.allmightworkout.framework.presentation.session.SessionLoggedType
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

        subscribeObservers()

        binding?.mainLogo?.fadeIn {
            if(!sessionManager.isAuth()){
                createSignInIntent()
            }
        }

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

            sessionState.logged?.let { sessionLoggedType ->

                when (sessionLoggedType) {

                    SessionLoggedType.CONNECTED -> {
                        printLogD("LoadingFragment", "CONNECTED STATUS ")
                        binding?.txtConnect?.text = "Status : Connected"
                    }

                    SessionLoggedType.DISCONNECTED -> {
                        printLogD("LoadingFragment", "DISCONNECTED STATUS ")
                        binding?.txtConnect?.text = "Status : Disconnected"
                    }
                }
            }
        }

            viewModel.state.observe(viewLifecycleOwner) { state ->

                processQueue(
                    context = context,
                    queue = state.queue,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromQueue() {
                            viewModel.onTriggerEvent(LoadingEvents.OnRemoveHeadFromQueue)
                        }
                    })

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
            // Successfully signed in²
            printLogD("LoadingFragment","user is logged"+sessionManager.state.value?.logged.toString())

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            printLogD("LoadingFragment","ERROR not connected ${response?.getError()?.getErrorCode()}")

        }
    }


    private fun navigateToHistory(){
        findNavController().navigate(R.id.action_global_history_fragment)
    }
}