package com.mikolove.allmightworkout.framework.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState.*

class ListInteractionManager<DomainModel> {

    private val _selectedItems: MutableLiveData<ArrayList<DomainModel>> = MutableLiveData()

    val selectedItems: LiveData<ArrayList<DomainModel>>
        get() = _selectedItems

    private val _toolbarState: MutableLiveData<ListToolbarState>
            = MutableLiveData(SearchViewState())

    val toolbarState: LiveData<ListToolbarState>
        get() = _toolbarState


    fun setToolbarState(state: ListToolbarState){
        _toolbarState.value = state
    }

    fun getSelectedItems():ArrayList<DomainModel> = _selectedItems.value?: ArrayList()

    fun isMultiSelectionStateActive(): Boolean{
        return _toolbarState.value.toString() == MultiSelectionState().toString()
    }

    fun addOrRemoveItemFromSelectedList(item: DomainModel){
        var list = _selectedItems.value
        if(list == null){
            list = ArrayList()
        }
        if (list.contains(item)){
            list.remove(item)
        }
        else{
            list.add(item)
        }
        _selectedItems.value = list
    }

    fun isItemSelected(item: DomainModel): Boolean{
        return _selectedItems.value?.contains(item)?: false
    }

    fun clearSelectedItems(){
        _selectedItems.value = null
    }

}