package com.tikalk.mobileevent.mobileevent.calllog.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by ronelg on 8/17/17.
 */
class SpaceItemDecoration(val left: Int, val top: Int) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView,
      state: RecyclerView.State?) {
    super.getItemOffsets(outRect, view, parent, state)

    outRect.top = top
    outRect.left = left
    outRect.right = left

    if (parent.getChildAdapterPosition(view) != parent.adapter.itemCount - 1) {
      outRect.bottom = top
    }
  }
}