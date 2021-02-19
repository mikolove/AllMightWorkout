package com.mikolove.allmightworkout.framework.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.mikolove.allmightworkout.framework.presentation.UIController
import com.mikolove.allmightworkout.framework.presentation.main.MainActivity
import com.mikolove.allmightworkout.util.printLogD

abstract class BaseFragment
constructor(
    private @LayoutRes val layoutRes: Int
): Fragment() {

    lateinit var uiController: UIController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLogD("BaseFragment","on View Created")
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayAppBarTitle()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            printLogD("BaseFragment","Activity is ${it}")
            if(it is MainActivity){
                try {
                    uiController = context as UIController
                    printLogD("BaseFragment","UIController is ${uiController}")
                }catch (e: ClassCastException){
                    e.printStackTrace()
                }
            }
        }
    }

   fun displayAppBarTitle(){
       uiController?.displayAppBarTitle()
   }
}
