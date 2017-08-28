package com.tikalk.mobileevent.mobileevent.util

fun Long.toCompoundDuration(): String {
    val hours = (this / (60 * 60))
    val min = (this / 60) % 60
    val sec = this % 60

    return "${hours}h ${min}m ${sec}s"
}