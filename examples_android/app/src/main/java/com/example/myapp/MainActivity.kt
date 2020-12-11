package com.example.myapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.mobilesdk.database.externals.Program
import com.example.myapp.databinding.ActivityMainBinding
import com.example.myapp.fragments.TriggerListFragment
import com.example.myapp.fragments.TriggerOverviewFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // TEST IMPL.
        MainApplication.instance!!.activity = this

        setContentView(binding.root)

        navigateToList()
    }

    override fun onResume() {
        super.onResume()

        TriggerSDK.notifyAppForeground(emptyMap())
    }

    fun showProgress() {
        runOnUiThread {
            binding.mainLoading.visibility = View.VISIBLE
        }
    }

    fun hideProgress() {
        runOnUiThread {
            binding.mainLoading.visibility = View.INVISIBLE
        }
    }

    fun navigateToOverview(program: Program) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContentView, TriggerOverviewFragment.newInstance(program)).disallowAddToBackStack().commit()
    }

    fun navigateToList() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContentView, TriggerListFragment.newInstance()).disallowAddToBackStack().commit()
    }
}