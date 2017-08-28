package com.tikalk.mobileevent.mobileevent.calllog

import com.tikalk.mobileevent.mobileevent.BasePresenter
import com.tikalk.mobileevent.mobileevent.BaseView
import com.tikalk.mobileevent.mobileevent.data.CallLogDao

interface CallLogContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showCallLogs(calllogs: List<CallLogDao>)

        fun showFilteringPopUpMenu()
    }

    interface Presenter : BasePresenter {

        fun loadLogs()

        var currentFiltering: CallLogFilterType
    }
}