package com.mikolove.allmightworkout.framework.presentation.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.util.printLogD

//Add a space between element of recycler view
class TopSpacingItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding

        //Set padding to last item - may change value
        parent.adapter?.let {
            if (parent.getChildAdapterPosition(view) == ( it.getItemCount().minus(1)) ) {
                outRect.bottom = 10*padding
            }
        }
    }

}