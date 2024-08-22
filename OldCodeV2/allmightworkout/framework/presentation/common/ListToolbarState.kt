package com.mikolove.allmightworkout.framework.presentation.common

sealed  class ListToolbarState {

    class MultiSelectionState: ListToolbarState(){

        override fun toString(): String {
            return "MultiSelectionState"
        }
    }
    class SelectionState: ListToolbarState(){

        override fun toString(): String {
            return "SelectionState"
        }
    }
}