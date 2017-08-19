package com.tikalk.mobileevent.mobileevent.dashboard

import com.ronelg.tmdb.BasePresenter
import com.ronelg.tmdb.BaseView
import com.tikalk.mobileevent.mobileevent.dashboard.data.DashboardItem

/**
 * Created by ronelg on 8/17/17.
 */
interface DashboardContract {

  interface View : BaseView<Presenter> {

    fun setLoadingIndicator(active: Boolean)

    fun showDashboard(items: List<DashboardItem>)

  }

  interface Presenter : BasePresenter {

    fun loadDashboard()
  }
}