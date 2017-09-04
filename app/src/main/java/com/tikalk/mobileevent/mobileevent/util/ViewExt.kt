package com.tikalk.mobileevent.mobileevent.util


import android.view.View


/**
 * Sets visibility to VISIBLE
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Sets visibility to GONE
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Sets visibility to INVISIBLE
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}


/**
 * Checks whether view is visible
 */
fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

/**
 * Checks whether view is visible
 */
fun View.isInvisible(): Boolean {
    return this.visibility == View.INVISIBLE
}

/**
 * Checks whether view is visible
 */
fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}