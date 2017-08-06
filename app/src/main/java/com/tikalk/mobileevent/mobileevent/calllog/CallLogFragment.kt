package com.tikalk.mobileevent.mobileevent.calllog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tikalk.mobileevent.mobileevent.R

/**
 * Created by ronelg on 8/6/17.
 */
class CallLogFragment : Fragment(), CallLogContract.View {

  override var presenter: CallLogContract.Presenter? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {

    val root = inflater.inflate(R.layout.calllog_fragment, container, false)

    return root
  }

  override fun onResume() {
    super.onResume()
    presenter?.subscribe()
  }

  override fun onPause() {
    super.onPause()
    presenter?.unsubscribe()
  }

  override fun setLoadingIndicator(active: Boolean) {

  }

  companion object {
    fun newInstance(): CallLogFragment {
      return CallLogFragment()
    }
  }

}