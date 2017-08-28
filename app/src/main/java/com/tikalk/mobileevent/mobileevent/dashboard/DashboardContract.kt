package com.tikalk.mobileevent.mobileevent.dashboard

import com.tikalk.mobileevent.mobileevent.BasePresenter
import com.tikalk.mobileevent.mobileevent.BaseView
import com.tikalk.mobileevent.mobileevent.dashboard.data.DashboardItem

interface DashboardContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showDashboard(items: List<DashboardItem>)

    }

    interface Presenter : BasePresenter {

        fun loadDashboard()
    }
}