
package com.tikalk.mobileevent.mobileevent.util

import android.content.Context
import android.widget.TextView
import com.tikalk.mobileevent.mobileevent.R


fun TextView.setDrawableLeft(context: Context, resId:Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(resId), null, null, null)
    this.compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.drawable_padding)
}