package com.tikalk.mobileevent.mobileevent.calllog

import com.ronelg.tmdb.BasePresenter
import com.ronelg.tmdb.BaseView

/**
 * Created by ronelg on 8/6/17.
 */
interface CallLogContract {

  interface View : BaseView<Presenter> {

    fun setLoadingIndicator(active: Boolean)

  }

  interface Presenter : BasePresenter {

  }
}