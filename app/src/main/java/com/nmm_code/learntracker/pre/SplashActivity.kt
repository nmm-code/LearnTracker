package com.nmm_code.learntracker.pre

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.pages.MainActivity
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var active = true

        installSplashScreen().setKeepOnScreenCondition {
            active
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val key = DataStoreState.PAGE
            val dataStore = DataStoreState(this@SplashActivity, key)
            when (dataStore.get(0)) {
                0 -> startActivity(Intent(this@SplashActivity, PrivacyActivity::class.java))
                1 -> startActivity(Intent(this@SplashActivity, SelectActivity::class.java))
                2 -> startActivity(
                    Intent(
                        this@SplashActivity,
                        WorkingTitleActivity::class.java
                    )
                )
                else -> startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            active = false
            finish()
        }
    }
}