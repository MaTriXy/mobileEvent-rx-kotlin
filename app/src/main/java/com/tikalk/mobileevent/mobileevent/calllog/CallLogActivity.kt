package com.tikalk.mobileevent.mobileevent.calllog

import android.Manifest
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.NavUtils
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.data.source.CallLogRepository
import com.tikalk.mobileevent.mobileevent.data.source.local.CallLogLocalDataSource
import com.tikalk.mobileevent.mobileevent.util.ActivityUtils


class CallLogActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var callLogPresenter: CallLogPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.callog_activity)
        requestPermissions()
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

        val callLogFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as CallLogFragment? ?: CallLogFragment.newInstance().also {
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, it, R.id.contentFrame)
        }

        // Create the presenter
        callLogPresenter = CallLogPresenter(CallLogRepository.getInstance(
                CallLogLocalDataSource.getInstance(applicationContext)),
                callLogFragment)

    }

    fun requestPermissions() {
        val rxPermissions = RxPermissions(this) // where this is an Activity instance
        rxPermissions
                .request(Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALL_LOG)
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        Toast.makeText(this, "permissions granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "permissions denied", Toast.LENGTH_LONG).show()
                    }
                }
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
            if (menuItem.itemId == R.id.dashboard_navigation_menu_item) {
                NavUtils.navigateUpFromSameTask(this@CallLogActivity)
            }
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }
}
