package com.mikolove.allmightworkout.framework.presentation.main.history

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.activityViewModels
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.databinding.FragmentHistoryBinding
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment(): BaseFragment(R.layout.fragment_history){

    val viewModel : HistoryViewModel by activityViewModels()
    private var actionMode : ActionMode? = null
    private var actionModeCallBack : ActionMode.Callback? = null
    private var binding : FragmentHistoryBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        printLogD("HistoryFragment","OnDestroyView")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHistoryBinding.bind(view)

        binding?.fragmentHistoryAddButton?.setOnClickListener {
            //enableMultiSelectToolbarState()
        }
    }


    private fun enableMultiSelectToolbarState(){
        printLogD("ChooseWorkoutFragment", "Start action Mode")
        if(actionMode == null) {
            printLogD("ChooseWorkoutFragment", "Action mode  null")
            actionModeCallBack = initializeActionMode()
            actionMode = activity?.startActionMode(actionModeCallBack)
        }
    }

    private fun disableMultiSelectToolbarState(){
        printLogD("ChooseWorkoutFragment", "Stop action Mode")
        if(actionMode != null){
            printLogD("ChooseWorkoutFragment", "Action Mode is not null")
            actionMode?.finish()
            actionModeCallBack = null
            actionMode = null
         }

    }

    private fun initializeActionMode() : ActionMode.Callback = object : ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            activity?.menuInflater?.inflate(R.menu.toolbar_workout_menu_multiselection, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.toolbar_workout_delete -> {
                    printLogD("ChooseWorkoutFragment", "DELETE IS PRESSED")
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            printLogD("ChooseWorkoutFragment", "Close is pressed")
            mode?.finish()
            disableMultiSelectToolbarState()
        }
    }

}