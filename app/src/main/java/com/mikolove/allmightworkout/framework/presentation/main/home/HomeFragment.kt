package com.mikolove.allmightworkout.framework.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.databinding.FragmentHomeBinding
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.main.history.HistoryFragment
import com.mikolove.allmightworkout.framework.presentation.main.workout.WorkoutFragment
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

const val HOME_LIST_STATE_BUNDLE_KEY = "com.mikolove.allmightworkout.framework.presentation.main.home.state"

@AndroidEntryPoint
class HomeFragment
constructor(): BaseFragment(R.layout.fragment_home)
{


    //private var viewPager: ViewPager2? = null
    //private var tabLayout: TabLayout? = null
    //private var fragmentCollectionAdapter : FragmentCollectionAdapter? = null
    private var binding : FragmentHomeBinding? = null
    //private var tabLayoutMediator : TabLayoutMediator? = null

    val viewModel : HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
/*        binding?.let {

            it.fragmentHomeViewPager.adapter = FragmentCollectionAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

            TabLayoutMediator(it.fragmentHomeTabLayout,it.fragmentHomeViewPager) { tab, position ->
                tab.text = getTitle(position)
                tab.setIcon(getIcon(position))
            }.attach()


        }*/
        //val tabLayout = view.findViewById<TabLayout>(R.id.fragment_home_tab_layout)

        //val viewpager = view.findViewById<ViewPager2>(R.id.fragment_home_view_pager)

    }


    override fun onDestroyView() {

        printLogD("HomeFragment","OnDestroyView")
        /*tabLayoutMediator?.detach()
        tabLayoutMediator = null
        */
        val viewpager = binding?.fragmentHomeViewPager

        viewpager?.let {
            printLogD("HomeFragment","Clear adapter")
            it.adapter = null
        }

        binding = null
      /*  fragmentCollectionAdapter = null
        binding = null*/
        super.onDestroyView()
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

//TODO : giving an array of fragment add them in homeFragment clearthem in destroy maybe it could avoid leaks
class FragmentCollectionAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HistoryFragment()
            1 -> WorkoutFragment()
            2 -> ChooseExerciseFragment()
            else -> { throw Exception("Exception getting Fragment")}
        }
    }


}