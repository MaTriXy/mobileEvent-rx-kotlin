package com.tikalk.mobileevent.mobileevent.calllog

import android.provider.CallLog
import com.tikalk.mobileevent.mobileevent.data.source.CallLogRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by ronelg on 8/6/17.
 */
class CallLogPresenter(
    val callLogRepositoty: CallLogRepository,
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
    val disposable = callLogRepositoty.getCallLog()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe({
          res -> callLogView.showCallLogs(res)
        }, {
          err -> Timber.e(err)
        })

    disposables.add(disposable)
  }

  private fun loadCallLogsWithNamePrefix(prefix: String) {
    val disposable = callLogRepositoty.getCallLog()
        .flatMapIterable { it }
        .filter { !it.name.isNullOrBlank() && it.name!!.startsWith(prefix, true) }
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe({
          res -> callLogView.showCallLogs(res)
        }, {
          err -> Timber.e(err)
        })

    disposables.add(disposable)
  }

  private fun loadCallLogsWithFilter(filter: Int) {
    val disposable = callLogRepositoty.getCallLog()
        .flatMapIterable { it }
        .filter { it.type == filter }
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe({
          res -> callLogView.showCallLogs(res)
        }, {
          err -> Timber.e(err)
        })

    disposables.add(disposable)
  }
}