package com.tikalk.mobileevent.mobileevent.util

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View


/**
 * Register [OnClickListener] on ViewHolder root view
 * @param clickEvent callback function receiving root view, item position and type
 * @return returns this view holder
 */
fun <T : RecyclerView.ViewHolder> T.click(clickEvent: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        clickEvent.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}


/**
 * Register [OnLongClickListener] on ViewHolder root view
 * @param longClickEvent callback function receiving root view, item position and type
 * @return returns this view holder
 */
fun <T : RecyclerView.ViewHolder> T.onLongClick(longClickEvent: (view: View, position: Int, type: Int) -> Boolean): T {
    itemView.setOnLongClickListener() {
        longClickEvent.invoke(it, getAdapterPosition(), getItemViewType())
    }
    return this
}

/**
 * Register [OnTouchListener] on ViewHolder root view
 * @param touchEvent callback function receiving root view, motion event, item position and type
 * @return returns this view holder
 */
fun <T : RecyclerView.ViewHolder>
        T.onTouch(touchEvent: (view: View, motionEvent: MotionEvent,
                               position: Int, type: Int) -> Boolean): T {
    itemView.setOnTouchListener { v, e ->
        touchEvent.invoke(v, e, getAdapterPosition(), getItemViewType())
    }
    return this
}