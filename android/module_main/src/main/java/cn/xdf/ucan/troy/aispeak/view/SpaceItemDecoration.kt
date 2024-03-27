package cn.xdf.ucan.troy.aispeak.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpaceItemDecoration(var space:Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom =space

        if (parent.getChildPosition(view) == 0){
            outRect.top = space
        }
    }
}