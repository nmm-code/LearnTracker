package com.nmm_code.learntracker.service

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.pages.TrackTimeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {
    private var isRunning = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        var notification = createNotification(sharedPreferences.getInt("seconds",0))
        startForeground(1, notification)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return START_NOT_STICKY
        }


        isRunning = true
        sharedPreferences.edit().putBoolean("running", isRunning).apply()

        CoroutineScope(Dispatchers.Default).launch {
            while (isRunning) {
                notification = createNotification(sharedPreferences.getInt("seconds",0))
                with(NotificationManagerCompat.from(this@TimerService)) {

                    notify(1, notification)
                }

                with(sharedPreferences.edit()) {
                    putInt("seconds", sharedPreferences.getInt("seconds",0) + 1)
                    apply()
                }
                delay(1000L)
            }
        }

        return START_STICKY
    }

    private fun createNotification(int: Int): Notification {
        val notificationIntent = Intent(this, TrackTimeActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)


        return NotificationCompat.Builder(this, "TIMER_CHANNEL")
            .setContentTitle("Timer running")
            .setContentText("%02d:%02d".format(int / 60,int % 60))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .build()
    }


    override fun onDestroy() {
        isRunning = false
        val sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("running", isRunning).apply()
        stopSelf()
        super.onDestroy()
    }
}
