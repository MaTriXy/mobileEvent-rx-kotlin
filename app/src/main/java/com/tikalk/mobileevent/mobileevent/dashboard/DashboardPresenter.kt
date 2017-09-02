package com.tikalk.mobileevent.mobileevent.dashboard

import android.provider.CallLog
import com.tikalk.mobileevent.mobileevent.dashboard.data.DashboardItem
import com.tikalk.mobileevent.mobileevent.data.source.CallLogRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DashboardPresenter(
        val callLogRepository: CallLogRepository,
        val dashboardView: DashboardContract.View) : DashboardContract.Presenter {

    init {
        dashboardView.presenter = this
    }

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun subscribe() {
        loadDashboard()
    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun loadDashboard() {
        val disposable = Singles.zip(
                loadTotalCalls(),
                loadIncomingCalls(),
                loadMissedCalls(),
                loadOngoingCalls(),
                { total, incoming, missed, outgoing -> mutableListOf(total, incoming, missed, outgoing) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    dashboardView.showDashboard(res)
                }, { err ->
                    Timber.e(err)
                })

        disposables.add(disposable)
    }

    private fun loadTotalCalls(): Single<DashboardItem> {
        return callLogRepository.getCallLog()
                .flatMapIterable { it }
                .reduce(DashboardItem().apply { this.type = DashboardItem.TYPE_TOTAL }) { acc, cur ->
                    acc.apply {
                        this.duration += cur.duration
                        this.count++
                    }
                }
    }

    private fun loadIncomingCalls(): Single<DashboardItem> {
        return callLogRepository.getCallLog()
                .flatMapIterable { it }
                .filter { it.type == CallLog.Calls.INCOMING_TYPE }
                .reduce(DashboardItem().apply { this.type = DashboardItem.TYPE_INCOMING }) { acc, cur ->
                    acc.apply {
                        this.duration += cur.duration
                        this.count++
                    }
                }
    }

    private fun loadMissedCalls(): Single<DashboardItem> {
        return callLogRepository.getCallLog()
                .flatMapIterable { it }
                .filter { it.type == CallLog.Calls.MISSED_TYPE }
                .reduce(DashboardItem().apply {this.type = DashboardItem.TYPE_MISSED }) { acc, cur ->
                    acc.apply {
                        this.duration += cur.duration
                        this.count++
                    }
                }
    }

    private fun loadOngoingCalls(): Single<DashboardItem> {
        return callLogRepository.getCallLog()
                .flatMapIterable { it }
                .filter { it.type == CallLog.Calls.OUTGOING_TYPE }
                .reduce(DashboardItem().apply { this.type  = DashboardItem.TYPE_OUTGOING }) { acc, cur ->
                    acc.apply {
                        this.duration += cur.duration
                        this.count++
                    }
                }
    }
}