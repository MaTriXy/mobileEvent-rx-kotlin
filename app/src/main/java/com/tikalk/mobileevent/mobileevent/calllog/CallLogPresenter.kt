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

    override var currentFiltering = CallLogFilterType.ALL

    init {
        callLogView.presenter = this
    }

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun subscribe() {
        loadLogs()
    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun loadLogs() {
        when (currentFiltering) {
            CallLogFilterType.ALL -> loadAllCallLog()
            CallLogFilterType.INCOMING -> loadCallLogsWithFilter(CallLog.Calls.INCOMING_TYPE)
            CallLogFilterType.OUTGOING -> loadCallLogsWithFilter(CallLog.Calls.OUTGOING_TYPE)
            CallLogFilterType.MISSED -> loadCallLogsWithFilter(CallLog.Calls.MISSED_TYPE)
        }
    }

    private fun loadAllCallLog() {
        val disposable = callLogRepository.getCallLog()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ res ->
                    callLogView.showCallLogs(res)
                }, { err ->
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
                .subscribe({ res ->
                    callLogView.showCallLogs(res)
                }, { err ->
                    Timber.e(err)
                })

        disposables.add(disposable)
    }

    override fun loadCallLogsWitPhonePrefix(prefix: String) {
        val disposable = callLogRepository.getCallLog()
                .flatMapIterable { it }
                .filter { !it.number.isNullOrBlank() && it.number!!.startsWith(prefix, true) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ res ->
                    callLogView.showCallLogs(res)
                }, { err ->
                    Timber.e(err)
                })

        disposables.add(disposable)
    }
}