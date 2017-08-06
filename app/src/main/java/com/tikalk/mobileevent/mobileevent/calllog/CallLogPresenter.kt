package com.tikalk.mobileevent.mobileevent.calllog

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
    loadCallLog()
  }

  override fun unsubscribe() {
    disposables.clear()
  }

  private fun loadCallLog() {
    val disposable = callLogRepositoty.getCallLog()
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