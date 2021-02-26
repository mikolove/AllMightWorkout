package com.mikolove.allmightworkout.framework.presentation.main.loading


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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

    val viewModel: LoadingViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)
        connectToFirebase()
    }

    private fun connectToFirebase(){

        val currentUser = firebaseAuth.currentUser
        if( currentUser != null){
            printLogD("LoadingFragment","User logged")
            subscribeObservers()

        }else{

            printLogD("Loading","User not logged")
            firebaseAuth.signInWithEmailAndPassword(
                FirestoreAuth.FIRESTORE_LOGIN,
                FirestoreAuth.FIRESTORE_PASSWORD
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    printLogD("Loading","User is now logged")
                    subscribeObservers()
                }else{
                    printLogD("Loading","Error logging user go to history")
                    navigateToHistory()
                }
            }
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
    }
}