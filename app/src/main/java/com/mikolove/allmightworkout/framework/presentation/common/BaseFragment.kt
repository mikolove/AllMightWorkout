package com.mikolove.allmightworkout.framework.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import com.mikolove.allmightworkout.framework.presentation.UIController
import com.mikolove.allmightworkout.framework.presentation.main.MainActivity
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseFragment
constructor(
    private @LayoutRes val layoutRes: Int
): Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    lateinit var uiController: UIController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        printLogD("BaseFragment","onViewCreated")
        displayAppBar()
        displayAppBarTitle()
        displayBottomNavigation()
        loadFabController()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
  /*      printLogD("BaseFragment","OnAttach")
        activity?.let {
            if(it is MainActivity){
                try {
                    uiController = context as UIController
                }catch (e: ClassCastException){
                    e.printStackTrace()
                }
            }
        }*/
    }

    fun displayAppBar(){
        uiController.displayAppBar()
    }

   fun displayAppBarTitle(){
       uiController.displayAppBarTitle()
   }

    fun displayBottomNavigation(){
        uiController.displayBottomNavigation()
    }

    fun loadFabController(){
        //COMMENTED LETS GO COMPOSE
        /*printLogD("BaseFragment","LoadFabController")
        uiController.loadFabController(null)*/
    }
}
