package com.confirmit.testsdkapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.MainApplication
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.domain.OverlayService
import com.confirmit.testsdkapp.utils.FetcherListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Testing purpose only
    var fetcherListener: FetcherListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        setupNavigation()
    }

    override fun onResume() {
        super.onResume()
        stopService(Intent(this, OverlayService::class.java))

        (ConfirmitSDK.androidContext as? MainApplication)?.let {
            it.activity = this
        }

        TriggerSDK.notifyAppForeground(emptyMap())
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this) && AppConfigs.enabledOverlayProgram.isNotEmpty()) {
            startService(Intent(this, OverlayService::class.java))
        }
    }

    private fun setupNavigation() {
        val navController = getNavController()

        // Update mainDrawer bar to reflect navigation
        setupActionBarWithNavController(this, navController, mainDrawer)
        mainNavigationView.setNavigationItemSelectedListener(this)
        mainNavigationView.setCheckedItem(R.id.menuTriggerList)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(findNavController(this, R.id.navHostFragment), mainDrawer)
    }

    override fun onBackPressed() {
        if (mainDrawer.isDrawerOpen(GravityCompat.START)) {
            mainDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        mainDrawer.closeDrawer(GravityCompat.START)
        val navController = getNavController()
        if (menuItem.itemId == R.id.menuSurveyList) {
            navController.graph.startDestination = R.id.surveyListFragment
            navController.popBackStack()
            navController.navigate(R.id.actionRootToSurveyList)

            return true
        }

        if (menuItem.itemId == R.id.menuSettings) {
            navController.graph.startDestination = R.id.settingsFragment
            navController.popBackStack()
            navController.navigate(R.id.actionRootToSettings)
            return true
        }

        if (menuItem.itemId == R.id.menuTriggerList) {
            navController.graph.startDestination = R.id.triggerListFragment
            navController.popBackStack()
            navController.navigate(R.id.actionRootToTriggerList)
            return true
        }

        return false
    }

    private fun getNavController(): NavController {
        return findNavController(this, R.id.navHostFragment)
    }

    fun hideFab() {
        (mainFab as View).visibility = View.GONE
    }

    fun showFab(): FloatingActionButton {
        (mainFab as View).visibility = View.VISIBLE
        return mainFab
    }

    fun showProgress() {
        runOnUiThread {
            mainLoading.visibility = ConstraintLayout.VISIBLE
        }
    }

    fun hideProgress() {
        runOnUiThread {
            mainLoading.visibility = ConstraintLayout.INVISIBLE
        }
    }
}