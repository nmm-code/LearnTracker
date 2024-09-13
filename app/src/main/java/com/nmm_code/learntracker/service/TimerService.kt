package com.nmm_code.learntracker.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.pages.TrackTimeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {
    private var isRunning = false

    private val binder = Binder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(1, notification)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
        startTimer()
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, TrackTimeActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return Notification.Builder(this, "TIMER_CHANNEL")
            .setContentTitle(getString(R.string.timer_is_running))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .build()
    }

    @SuppressLint("DefaultLocale")
    private fun startTimer() {
        isRunning = true
        val sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("running", isRunning).apply()

        isRunning = true
        CoroutineScope(Dispatchers.IO).launch {
            while (isRunning) {
                delay(1000L)
                with(sharedPreferences.edit()) {
                    putInt("seconds", sharedPreferences.getInt("seconds",0) + 1)
                    apply()
                }
            }
        }
    }

    override fun onDestroy() {
        isRunning = false
        val sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("running", isRunning).apply()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onDestroy()

    }
}
