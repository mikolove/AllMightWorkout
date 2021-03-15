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
    private var binding : FragmentHistoryBinding? = null

}