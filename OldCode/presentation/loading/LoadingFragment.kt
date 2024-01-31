package com.mikolove.allmightworkout.framework.presentation.main.loading


import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.UserFactory
import com.mikolove.allmightworkout.databinding.FragmentLoadingBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.compose.ui.LoadingScreen
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val LOADING_FRAGMENT_NO_SYNC = "User is not connected, could not sync data with firebase"

@AndroidEntryPoint
class LoadingFragment : BaseFragment(R.layout.fragment_loading) {

    @Inject
    lateinit var userFactory: UserFactory

/*    private val googleAuthUiClient by lazy {
        sessionManager.googleAuthUiClient
    }*/



    private var binding : FragmentLoadingBinding? = null

    /*private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val signInResult = googleAuthUiClient.signInWithIntent(
                    intent = result.data ?: return@launch
                )
                //Update sign in state here
                printLogD("LoadingFragment","User ${signInResult}")
                viewModel.onTriggerEvent(LoadingEvents.SignInResult(signInResult))
            }
        }else{
            viewModel.onTriggerEvent(LoadingEvents.LoadStep(LoadingStep.INIT))
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val viewModel: LoadingViewModel = hiltViewModel()

   /*             val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if(result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                //Update sign in state here
                                printLogD("LoadingFragment","User ${signInResult}")
                                viewModel.onTriggerEvent(LoadingEvents.SignInResult(signInResult))
                            }
                        }else{
                            viewModel.onTriggerEvent(LoadingEvents.LoadStep(LoadingStep.INIT))
                        }
                    }
                )*/
            }
        }
    }


    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(false)

        binding = FragmentLoadingBinding.bind(view)

        binding?.connectButton?.setOnClickListener {
            lifecycleScope.launch{

                viewModel.onTriggerEvent(LoadingEvents.LoadStep(LoadingStep.LOAD_USER))

                val signInIntenSender = googleAuthUiClient.signIn()
                this@LoadingFragment.signInLauncher.launch(
                    IntentSenderRequest.Builder(
                        signInIntenSender ?: return@launch
                    ).build()
                )
            }
        }

/*        binding?.signoutButton?.setOnClickListener {
            lifecycleScope.launch {
                googleAuthUiClient.signOut()
            }
        }*/

        val user = sessionManager.googleAuthUiClient.getSignedInUser()
        if( user != null){
            viewModel.onTriggerEvent(LoadingEvents.LoadStep(LoadingStep.LAUNCH_SYNC))
            viewModel.onTriggerEvent(LoadingEvents.SyncEverything(user))
        }

        subscribeObservers()
    }*/

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    /*private fun subscribeObservers() {

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
                LoadingStep.LOAD_USER,
                LoadingStep.LOADED_USER_CREATE,
                LoadingStep.LOADED_USER_SYNC,
                LoadingStep.LAUNCH_SYNC,->{
                    showLoadingStep(state.loadingStep.loadingMessage)
                }
                LoadingStep.GO_TO_APP -> {
                    navigateToHistory()
                }
                else -> {}
            }

        }
    }
*/
    /*
        Firebase
     */
    private fun showLoadingStep(loadingMessage : String){
        binding?.txtConnect?.text = loadingMessage
        binding?.connectButton?.fadeOut()
        //binding?.signoutButton?.invisible()
    }

    private fun showInit(loadingMessage : String){
        binding?.txtConnect?.text = loadingMessage
        binding?.connectButton?.fadeIn()
        //binding?.signoutButton?.fadeIn()
    }

    private fun showProgressbar(isLoading : Boolean){
        if(isLoading){
            binding?.mainProgressBar?.fadeIn()
        }else{
            binding?.mainProgressBar?.fadeOut()
        }
    }

    private fun navigateToHistory(){
        findNavController().navigate(R.id.action_global_history_fragment)
    }
}