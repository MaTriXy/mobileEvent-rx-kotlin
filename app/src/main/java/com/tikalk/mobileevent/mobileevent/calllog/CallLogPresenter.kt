package com.tikalk.mobileevent.mobileevent.calllog

import android.provider.CallLog
import com.tikalk.mobileevent.mobileevent.data.source.CallLogRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CallLogPresenter(
        val callLogRepository: CallLogRepository,
        val callLogView: CallLogContract.View) : CallLogContract.Presenter {

    init {
        callLogView.presenter = this
    }

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun subscribe() {
//    loadCallLog()
//    loadCallLogsWithNamePrefix("m")
        loadCallLogsWithFilter(CallLog.Calls.INCOMING_TYPE)
    }

    override fun unsubscribe() {
        disposables.clear()
    }

    private fun loadCallLog() {
        val disposable = callLogRepository.getCallLog()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    res ->
                    callLogView.showCallLogs(res)
                }, {
                    err ->
                    Timber.e(err)
                })

        disposables.add(disposable)
    }

    private fun loadCallLogsWithNamePrefix(prefix: String) {
        val disposable = callLogRepository.getCallLog()
                .flatMapIterable { it }
                .filter { !it.name.isNullOrBlank() && it.name!!.startsWith(prefix, true) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    res ->
                    callLogView.showCallLogs(res)
                }, {
                    err ->
                    Timber.e(err)
                })

        disposables.add(disposable)
    }

    private fun loadCallLogsWithFilter(filter: Int) {
        val disposable = callLogRepository.getCallLog()
                .flatMapIterable { it }
                .filter { it.type == filter }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    res ->
                    callLogView.showCallLogs(res)
                }, {
                    err ->
                    Timber.e(err)
                })

        disposables.add(disposable)
    }
}