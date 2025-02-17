package com.btracsolutions.ems.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.btracsolutions.ems.R

class SplashActivity:  AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // Your Code
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}