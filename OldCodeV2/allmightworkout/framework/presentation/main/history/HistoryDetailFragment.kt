package com.mikolove.allmightworkout.framework.presentation.main.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment

class HistoryDetailFragment(): BaseFragment(R.layout.fragment_base){

    val viewModel : HistoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}