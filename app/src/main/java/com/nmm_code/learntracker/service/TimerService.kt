package com.nmm_code.learntracker.service

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nmm_code.learntracker.R
import com.nmm_code.learntracker.data.DataStoreState
import com.nmm_code.learntracker.logic.TimeUtils
import com.nmm_code.learntracker.pages.TrackTimeActivity
import kotlinx.coroutines.runBlocking

class TimerService : Service() {
    private var seconds = 0L
    private var timeSince = 0L

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runBlocking {
            seconds = DataStoreState(this@TimerService, DataStoreState.SECONDS).get(0)
            timeSince = SystemClock.elapsedRealtime()
        }

        val notification = createNotification()
        startForeground(1, notification)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw Error()
        }

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            val updatedNotification = createNotification()
            NotificationManagerCompat.from(this).notify(1, updatedNotification)
            val display = seconds + ((SystemClock.elapsedRealtime() - timeSince) / 1000)
            println("DDD")
            val delay = when {
                display % 60 == 0L -> 60000L
                display % 60 == 59L || display < 60 -> 1000L
                else -> (60 - (display % 60)) * 1000
            }
            handler.postDelayed(runnable, delay)
        }
        handler.postDelayed(runnable, 1000L)

        return START_STICKY
    }


    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, TrackTimeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val display = seconds + ((SystemClock.elapsedRealtime() - timeSince) / 1000)
        return NotificationCompat.Builder(this, "TIMER_CHANNEL")
            .setContentTitle("Timer running")
            .setContentText(TimeUtils.formatSeconds(display))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .build()
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        stopSelf()
        super.onDestroy()
    }
}
