package com.tikalk.mobileevent.mobileevent.dashboard.data

/**
 * Created by ronelg on 8/19/17.
 */
data class DashboardItem(var type: Int = 0, var duration: Long = 0,var count:Int = 0) {

  companion object {
    val TYPE_MISSED = 3
    val TYPE_INCOMING = 1
    val TYPE_OUTGOING = 2
    val TYPE_TOTAL = 0
  }
}