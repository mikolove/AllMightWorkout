package com.mikolove.allmightworkout.framework.presentation.main.loading


import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.datasource.network.util.FirestoreAuth

import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val LOADING_FRAGMENT_NO_SYNC = "User is not connected, could not sync data with firebase"

@AndroidEntryPoint
class LoadingFragment : BaseFragment(R.layout.fragment_loading) {

   /* val viewModel: LoadingViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

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
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            //val user = firebaseAuth.currentUser
            connectToFirebase()
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun signOut() {
        // [START auth_fui_signout]
        context?.let {
            AuthUI.getInstance()
                .signOut(it)
                .addOnCompleteListener {
                    // ...
                    printLogD("LoadingFragment","Sign out complete")
                }
        }
        // [END auth_fui_signout]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)
        connectToFirebase()
    }

    private fun connectToFirebase(){

        //signOut()
        val currentUser = firebaseAuth.currentUser
        if( currentUser != null){
            printLogD("LoadingFragment","User logged")
            subscribeObservers()

        }else{
            createSignInIntent()
           *//* printLogD("Loading","User not logged")
            firebaseAuth.signInWithEmailAndPassword(
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
            }*//*
        }
    }

    private fun subscribeObservers(){
        viewModel.hasSyncBeenExecuted().observe(viewLifecycleOwner, Observer { hasSyncBeenExecuted ->
            if(hasSyncBeenExecuted){
                navigateToHistory()
            }
        })
    }

    private fun navigateToHistory(){
        findNavController().navigate(R.id.action_global_history_fragment)
    }*/
}