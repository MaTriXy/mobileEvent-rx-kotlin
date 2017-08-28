package com.tikalk.mobileevent.mobileevent.calllog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.*
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.calllog.util.SpaceItemDecoration
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import timber.log.Timber

class CallLogFragment : Fragment(), CallLogContract.View {

    override var presenter: CallLogContract.Presenter? = null

    lateinit var adapter: CallLogsAdapter

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
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.calllogs_fragment_menu, menu)
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