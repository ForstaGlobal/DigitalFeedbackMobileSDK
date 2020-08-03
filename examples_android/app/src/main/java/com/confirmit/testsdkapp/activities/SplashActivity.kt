package com.confirmit.testsdkapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot) {
            finish()
            return
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}