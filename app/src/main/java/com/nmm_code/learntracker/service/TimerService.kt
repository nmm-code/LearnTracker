package com.nmm_code.learntracker.service

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.pages.TrackTimeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TimerService : Service() {
    private var isRunning = false
    private var seconds: Int = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        seconds = sharedPreferences.getInt("seconds", 0)
        val notification = createNotification(seconds)
        startForeground(1, notification)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw Error()
        }

        isRunning = true
        sharedPreferences.edit().putBoolean("running", isRunning).apply()

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            if (isRunning) {
                seconds++
                val updatedNotification = createNotification(seconds)
                NotificationManagerCompat.from(this).notify(1, updatedNotification)
                sharedPreferences.edit().putInt("seconds", seconds).apply()
                handler.postDelayed(runnable, 1000L)
            }
        }
        handler.post(runnable)

        return START_STICKY
    }


    private fun createNotification(seconds: Int): Notification {
        val notificationIntent = Intent(this, TrackTimeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "TIMER_CHANNEL")
            .setContentTitle("Timer running")
            .setContentText("%02d:%02d".format(seconds / 60, seconds % 60))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .build()
    }

    override fun onDestroy() {
        isRunning = false
        handler.removeCallbacks(runnable) // Stop handler updates
        val sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("running", isRunning).apply()
        stopSelf()
        super.onDestroy()
    }
}
