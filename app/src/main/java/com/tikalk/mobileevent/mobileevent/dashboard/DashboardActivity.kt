package com.tikalk.mobileevent.mobileevent.dashboard

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.calllog.CallLogActivity
import com.tikalk.mobileevent.mobileevent.data.source.CallLogRepository
import com.tikalk.mobileevent.mobileevent.data.source.local.CallLogLocalDataSource
import com.tikalk.mobileevent.mobileevent.util.ActivityUtils
import com.tikalk.mobileevent.mobileevent.util.CallLogBootstrap

class DashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var dashboardPresenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.callog_activity)
        requestPermissions()

    }

    private fun requestPermissions() {
        val rxPermissions = RxPermissions(this) // where this is an Activity instance
        rxPermissions
                .request(Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALL_LOG)
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        doAfterPermission()
                    } else {
                        Toast.makeText(this, "permissions denied", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
    }

    private fun doAfterPermission() {
        // Set up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
            it.setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout)).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        setupDrawerContent(navigationView)

        val dashboardFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as DashboardFragment? ?: DashboardFragment.newInstance().also {
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, it, R.id.contentFrame)
        }

        // Create the presenter
        dashboardPresenter = DashboardPresenter(CallLogRepository.getInstance(
                CallLogLocalDataSource.getInstance(applicationContext)),
                dashboardFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Open the navigation drawer when the home icon is selected from the toolbar.
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.callllogs_navigation_menu_item) {
                val intent = Intent(this@DashboardActivity, CallLogActivity::class.java)
                startActivity(intent)
            } else if (menuItem.itemId == R.id.bootsrap_navigation_menu_item) {
                CallLogBootstrap(this).bootstrap()
                dashboardPresenter.loadDashboard()
            }
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }
}