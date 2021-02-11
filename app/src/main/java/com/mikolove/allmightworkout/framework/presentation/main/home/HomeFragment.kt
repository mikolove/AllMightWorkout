package com.mikolove.allmightworkout.framework.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.api.ResourceDescriptor

import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.main.history.HistoryFragment
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment
constructor(): BaseFragment(R.layout.fragment_home)
{

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var fragmentCollectionAdapter : FragmentCollectionAdapter? = null

    val viewModel : HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            fragmentCollectionAdapter = FragmentCollectionAdapter(this)
            viewPager = view.findViewById(R.id.home_view_pager)
            tabLayout = view.findViewById(R.id.tab_layout)
            viewPager.adapter = fragmentCollectionAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = "OBJECT ${(position + 1)}"
            }.attach()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCollectionAdapter = null
    }
}

class FragmentCollectionAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HistoryFragment()
            1 -> ChooseWorkoutFragment()
            2 -> ChooseExerciseFragment()
            else -> { throw Exception("Exception getting Fragment")}
        }
    }
}