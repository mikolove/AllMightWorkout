package com.mikolove.allmightworkout.framework.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.main.history.HistoryFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

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
            viewPager = view.findViewById(R.id.fragment_home_view_pager)
            tabLayout = view.findViewById(R.id.fragment_home_tab_layout)
            viewPager.adapter = fragmentCollectionAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getTitle(position)
                tab.setIcon(getIcon(position))
            }.attach()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCollectionAdapter = null
    }

    fun getIcon(position : Int) : Int{
        return when(position){
            0 -> R.drawable.ic_baseline_history_24
            1 -> R.drawable.ic_baseline_list_alt_24
            else -> R.drawable.ic_baseline_fitness_center_24
        }
    }
    fun getTitle(position : Int) : String{
        return when(position){
            0 -> getString(R.string.fragment_home_tab_layout_history)
            1 -> getString(R.string.fragment_home_tab_layout_workout)
            else -> getString(R.string.fragment_home_tab_layout_exercise)
        }
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