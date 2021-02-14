package com.mikolove.allmightworkout.framework.presentation.main.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment(): BaseFragment(R.layout.fragment_history){

    val viewModel : HistoryViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        printLogD("HistoryFragment","OnDestroyView")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}