package com.tikalk.mobileevent.mobileevent.calllog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.calllog.util.SpaceItemDecoration
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CallLogFragment : Fragment(), CallLogContract.View {

    override var presenter: CallLogContract.Presenter? = null

    lateinit var adapter: CallLogsAdapter

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.calllog_fragment, container, false)

        with(root) {
            val recyclerView = findViewById<RecyclerView>(R.id.list)!!
            setupRecyclerView(recyclerView)
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onResume() {
        super.onResume()
        presenter?.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter?.unsubscribe()
        disposables.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.calllogs_fragment_menu, menu)

        val disposable =  RxSearchView.queryTextChangeEvents(
                menu?.findItem(R.id.menu_search)?.actionView as SearchView)
                .skip(1)
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res -> if (res.queryText().isEmpty()) {
                        presenter?.loadLogs()
                    } else {
                        presenter?.loadCallLogsWitPhonePrefix(res.queryText().toString())
                    }
                })
        disposables.add(disposable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> showFilteringPopUpMenu()
        }
        return true
    }

    override fun showFilteringPopUpMenu() {
        PopupMenu(context, activity.findViewById(R.id.menu_filter)).apply {
            menuInflater.inflate(R.menu.filter_calllog, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.nav_incoming -> presenter?.currentFiltering = CallLogFilterType.INCOMING
                    R.id.nav_outgoing -> presenter?.currentFiltering = CallLogFilterType.OUTGOING
                    R.id.nav_missed -> presenter?.currentFiltering = CallLogFilterType.MISSED
                    else -> presenter?.currentFiltering = CallLogFilterType.ALL
                }
                presenter?.loadLogs()
                true
            }
            show()
        }
    }

    override fun setLoadingIndicator(active: Boolean) {

    }

    override fun showCallLogs(calllogs: List<CallLogDao>) {
        Timber.i(calllogs.toString())
        adapter.setItems(calllogs)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = CallLogsAdapter()

        val padding = resources.getDimensionPixelOffset(R.dimen.list_item_padding)

        recyclerView.addItemDecoration(SpaceItemDecoration(
                padding, padding / 2))
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): CallLogFragment {
            return CallLogFragment()
        }
    }
}