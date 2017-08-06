package com.tikalk.mobileevent.mobileevent.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by ronelg on 8/7/17.
 */
object ActivityUtils {

  /**
   * The `fragment` is added to the container view with id `frameId`. The operation is
   * performed by the `fragmentManager`.
   */
  fun addFragmentToActivity(fragmentManager: FragmentManager,
      fragment: Fragment, frameId: Int) {
    val transaction = fragmentManager.beginTransaction()
    transaction.add(frameId, fragment)
    transaction.commit()
  }

}