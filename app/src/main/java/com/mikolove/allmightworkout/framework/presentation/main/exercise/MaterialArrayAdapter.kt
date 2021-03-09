package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class MaterialArrayAdapter<Item>(context: Context, layout: Int, var values: ArrayList<Item>) :
    ArrayAdapter<Item>(context, layout, values) {
    private val filter_that_does_nothing = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = values
            results.count = values.size
            return results
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filter_that_does_nothing
    }
}