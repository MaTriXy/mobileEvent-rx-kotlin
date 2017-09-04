package com.tikalk.mobileevent.mobileevent.util


import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Context.hideKeyboard(caller: View) {
    caller.postDelayed({
        try {
            val imm = caller.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(caller.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            Log.w(javaClass.simpleName, "Could not hide keyboard. " + e.message)
        }
    }, 300)
}

fun Context.showKeyboard(caller: View) {
    caller.postDelayed({
        val imm = caller.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(caller, InputMethodManager.SHOW_IMPLICIT)
    }, 100)
}

