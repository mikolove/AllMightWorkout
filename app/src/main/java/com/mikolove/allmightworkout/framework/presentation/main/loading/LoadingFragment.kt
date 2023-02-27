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
import com.mikolove.allmightworkout.framework.presentation.session.SessionLoggedType
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val LOADING_FRAGMENT_NO_SYNC = "User is not connected, could not sync data with firebase"

@AndroidEntryPoint
class LoadingFragment : BaseFragment(R.layout.fragment_loading) {

    val viewModel: LoadingViewModel by viewModels()

    private var binding : FragmentLoadingBinding? = null

    @Inject
    lateinit var mAuth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)

        binding = FragmentLoadingBinding.bind(view)

        subscribeObservers()

        //binding?.mainLogo?.fadeIn {
        viewModel.onTriggerEvent(LoadingEvents.GetAccountPreferences)
        //}

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    private fun subscribeObservers(){

        viewModel.state.observe(viewLifecycleOwner) { state ->

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        viewModel.onTriggerEvent(LoadingEvents.OnRemoveHeadFromQueue)
                    }
                })

            state.lastSessionStatus?.let { sessionLoggedType ->

                printLogD("LoadingFrament","Account pref ${state.accountPreference}")
                printLogD("LoadingFrament","Session info ${state.lastSessionStatus}")
                when(sessionLoggedType){

                    SessionLoggedType.CONNECTED ->{

                        printLogD("LoadingFragment","CONNECTED STATUS ")
                        //Try reconnect
                        state.accountPreference?.let { accountPreference ->
                            printLogD("LoadingFragment","email ${accountPreference.email} | ${accountPreference.password} ")
                            if(!accountPreference.email.isNullOrBlank() && !accountPreference.password.isNullOrBlank()) {
                                 connectUser(accountPreference.email,accountPreference.password)
                            }
                        }
                    }

                    SessionLoggedType.DISCONNECTED->{
                        printLogD("LoadingFragment","DISCONNECTED STATUS ")
                        //Show login screen
                        createSignInIntent()
                    }


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
            // Successfully signed in²
            val user = mAuth.currentUser
            //connectToFirebase()
            // ...
            printLogD("LoadingFragment","Logged USER ID ${user?.uid}")
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            printLogD("LoadingFragment","ERROR not connected ${response?.getError()?.getErrorCode()}")

        }
    }

    private fun connectUser(email : String, password : String){

        val currentUser = mAuth.currentUser
        if( currentUser != null){

        }else{
            mAuth.signInWithEmailAndPassword(
                email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        /*Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)*/
                    } else {
                        // If sign in fails, display a message to the user.
/*                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)*/
                    }
                }
        }

    }
    private fun connectToFirebase(){

        //signOut()
        val currentUser = mAuth.currentUser
        if( currentUser != null){
            printLogD("LoadingFragment","User logged")
            subscribeObservers()

        }else{
            createSignInIntent()
             printLogD("Loading","User not logged")
            mAuth.signInWithEmailAndPassword(
                 FirestoreAuth.FIRESTORE_LOGIN,
                 FirestoreAuth.FIRESTORE_PASSWORD
             ).addOnCompleteListener{
                 if(it.isSuccessful){
                     printLogD("Loading","User is now logged")
                     //subscribeObservers()
                 }else{
                     printLogD("Loading","Error logging user go to history")
                     //navigateToHistory()
                 }
             }
        }
    }



    private fun navigateToHistory(){
        findNavController().navigate(R.id.action_global_history_fragment)
    }
}