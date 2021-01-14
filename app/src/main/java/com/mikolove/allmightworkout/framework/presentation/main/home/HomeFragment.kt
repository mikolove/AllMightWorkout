package com.mikolove.allmightworkout.framework.presentation.main.home

import android.os.Bundle
import android.view.View

import com.google.android.material.tabs.TabLayout
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.main.common.BaseFragment

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
    }
}