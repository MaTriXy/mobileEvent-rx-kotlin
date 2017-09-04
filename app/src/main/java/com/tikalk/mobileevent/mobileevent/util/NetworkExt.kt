package com.tikalk.mobileevent.mobileevent.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun Context.getConnectivityManager() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.isNetworkAvailable(): Boolean {
    val state = getConnectivityManager().activeNetworkInfo.state
    return (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
}

fun Context.getNetworkType(): Int = getConnectivityManager().activeNetworkInfo.type

fun Context.getNetworkTypeAsString(): String {
    when (getConnectivityManager().activeNetworkInfo.type) {
        ConnectivityManager.TYPE_WIFI -> return "WiFi"
        ConnectivityManager.TYPE_MOBILE -> return "Mobile"
        else -> return ""
    }
}
