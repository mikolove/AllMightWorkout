package com.mikolove.allmightworkout.framework.presentation.main.home.state

sealed  class ListToolbarState {

    class MultiSelectionState: ListToolbarState(){

        override fun toString(): String {
            return "MultiSelectionState"
        }
    }
    class SearchViewState: ListToolbarState(){

        override fun toString(): String {
            return "SearchViewState"
        }
    }
}