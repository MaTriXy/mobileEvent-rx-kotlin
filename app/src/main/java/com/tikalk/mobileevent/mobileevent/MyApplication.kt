package com.tikalk.mobileevent.mobileevent

import android.app.Application
import timber.log.Timber

/**
 * Created by ronelg on 8/6/17.
 */
class MyApplication: Application() {


  override fun onCreate() {
    super.onCreate()

    Timber.plant(Timber.DebugTree())
  }
}