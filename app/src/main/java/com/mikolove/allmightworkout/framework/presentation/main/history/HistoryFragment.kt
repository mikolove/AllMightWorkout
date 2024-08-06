/*
package com.mikolove.allmightworkout.framework.presentation.main.history

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.FragmentHistoryBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment(): BaseFragment(R.layout.fragment_history)*/
/*,
        HistoryListAdapter.Interaction*//*

{
*/
/*    @Inject
    lateinit var dateUtil: DateUtil

    val viewModel : HistoryViewModel by activityViewModels()
    private var binding : FragmentHistoryBinding? = null
    private var listAdapter : HistoryListAdapter? = null

    *//*
*/
/********************************************************************
    LIFECYCLE MANANGEMENT
     *********************************************************************//*
*/
/*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        printLogD("HistoryFragment","OnCreate")
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        printLogD("HistoryFragment","OnViewCreated")

        setHasOptionsMenu(true)
        binding = FragmentHistoryBinding.bind(view)

        setupUI()
        setupSwipeRefresh()
        setupRecyclerView()
        subscribeObservers()

    }

    override fun onResume() {
        printLogD("HistoryFragment","OnResume")
        super.onResume()

        viewModel.loadTotalHistory()
        viewModel.refreshSearchQuery()
    }

    override fun onDestroyView() {
        printLogD("HistoryFragment","OnDestroyView")
        super.onDestroyView()
        binding?.fragmentHistoryRecyclerview?.adapter = null
        listAdapter = null
        binding = null
    }

    *//*
*/
/********************************************************************
        SUBSCRIBE OBSERVERS
     *********************************************************************//*
*/
/*

    private fun subscribeObservers(){


        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {

                viewState.listHistoryWorkouts?.let { workoutList ->

                    if (workoutList.size > 0) {
                        if (viewModel.isHistoryPaginationExhausted()&& !viewModel.isQueryExhausted()) {
                            viewModel.setQueryExhausted(true)
                        }
                        listAdapter?.submitList(workoutList)
                        showList()
                    } else {
                        hideList()
                    }
                }
            }})

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { isLoading ->
            printActiveJobs()
            uiController.displayProgressBar(isLoading)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when (response.message) {

                    else -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                    }
                }
            }

        })
    }

    *//*
*/
/********************************************************************
    SETUP
     *********************************************************************//*
*/
/*

    private fun showList(){
        if(binding?.fragmentHistorySwiperefreshlayout?.isVisible == false) {
            binding?.fragmentHistorySwiperefreshlayout?.fadeIn()
            binding?.fragmentHistoryNoWorkout?.fadeOut()
        }
    }

    private fun hideList(){
        if(binding?.fragmentHistorySwiperefreshlayout?.isVisible == true){
            binding?.fragmentHistorySwiperefreshlayout?.fadeOut()
            binding?.fragmentHistoryNoWorkout?.fadeIn()
        }
    }

    private fun setupUI(){
        view?.hideKeyboard()
    }


    private fun setupSwipeRefresh(){
        binding?.fragmentHistorySwiperefreshlayout?.setOnRefreshListener {
            startNewSearch()
            binding?.fragmentHistorySwiperefreshlayout?.isRefreshing = false
        }
    }

    private fun startNewSearch(){
        //viewModel.clearListWorkouts()
        viewModel.startNewSearch()
    }


    private fun setupRecyclerView(){
        binding?.fragmentHistoryRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            listAdapter = HistoryListAdapter(
                this@HistoryFragment,
                dateUtil
            )


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == listAdapter?.itemCount?.minus(1)) {
                        viewModel.nextPageHistory()
                    }
                }
            })

            listAdapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = listAdapter
        }
    }


    *//*
*/
/********************************************************************
    INTERACTION
     *********************************************************************//*
*/
/*

    override fun onItemSelected(position: Int?, item: HistoryWorkout) {
    }

    *//*
*/
/********************************************************************
    DEBUG
     *********************************************************************//*
*/
/*

    private fun printActiveJobs(){

        for((index, job) in viewModel.getActiveJobs().withIndex()){
            printLogD("WorkoutFragment", "${index}: ${job}")
        }
    }*//*

}*/
