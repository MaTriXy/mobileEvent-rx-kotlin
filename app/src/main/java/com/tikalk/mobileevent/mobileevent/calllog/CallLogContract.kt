package com.tikalk.mobileevent.mobileevent.calllog

import com.ronelg.tmdb.BasePresenter
import com.ronelg.tmdb.BaseView
import com.tikalk.mobileevent.mobileevent.data.CallLogDao

/**
 * Created by ronelg on 8/6/17.
 */
interface CallLogContract {

  interface View : BaseView<Presenter> {

    fun setLoadingIndicator(active: Boolean)

    fun showCallLogs(calllogs: List<CallLogDao>)

  }

  interface Presenter : BasePresenter {

  }
}