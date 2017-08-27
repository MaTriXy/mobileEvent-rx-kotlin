package com.tikalk.mobileevent.mobileevent.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.dashboard.data.DashboardItem
import com.tikalk.mobileevent.mobileevent.dashboard.util.GridSpacingItemDecoration


class DashboardFragment : Fragment(), DashboardContract.View {

    override var presenter: DashboardContract.Presenter? = null

    lateinit var adapter: DashboardAdapter

    lateinit var piechart: PieChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.dashboard_fragment, container, false)

        with(root) {
            piechart = findViewById(R.id.piechart)
            val recyclerView = findViewById<RecyclerView>(R.id.list)!!
            setupRecyclerView(recyclerView)
        }

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

    override fun showDashboard(items: List<DashboardItem>) {
        adapter.setItems(items)
        loadPieChart(items)
    }

    private fun loadPieChart(items: List<DashboardItem>) {
        val entries = mutableListOf<PieEntry>()
        val colors = ColorTemplate.MATERIAL_COLORS.toList()

        var t: String = ""
        for (i in items) {
            when (i.type) {
                DashboardItem.TYPE_INCOMING -> t = "INCOMING"
                DashboardItem.TYPE_OUTGOING -> t = "OUTGOING"
                DashboardItem.TYPE_MISSED -> t = "MISSED"
                DashboardItem.TYPE_TOTAL -> t = "TOTAL"
            }
            entries.add(PieEntry(i.count.toFloat(), t))

        }

        val set = PieDataSet(entries, "CallLog Analytics")
        set.colors = colors

        val data = PieData(set)
        piechart.setUsePercentValues(true)
        piechart.data = data
        piechart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val padding = resources.getDimensionPixelOffset(R.dimen.list_item_padding)
        val spanCount = 2

        adapter = DashboardAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, spanCount)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, padding, true))
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}