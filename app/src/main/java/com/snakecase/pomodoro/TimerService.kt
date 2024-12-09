package com.snakecase.pomodoro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TimerService : Service() {

    private val CHANNEL_ID = "TimerChannel"
    private val NOTIFICATION_ID = 1
    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 60000L
    private var isPaused: Boolean = false

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        when (intent?.action) {
            "START" -> {
                remainingTime = intent.getLongExtra("duration", 60000L)
                startTimer(remainingTime)
            }
            "PAUSE" -> {
                pauseTimer()
            }
            "RESUME" -> {
                resumeTimer()
            }
            "STOP" -> {
                stopTimer()
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        stopForeground(true)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Timer Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    fun startTimer(duration: Long) {
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateNotification("Time left: ${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                stopForeground(true)
                stopSelf()
            }
        }.start()
        startForeground(NOTIFICATION_ID, createNotification("Time left: ${duration / 1000}").build())
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isPaused = true
        updateNotification("Paused at: ${remainingTime / 1000}")
    }

    private fun resumeTimer() {
        isPaused = false
        startTimer(remainingTime)
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        stopForeground(true)
        stopSelf()
    }

    private fun createNotification(contentText: String): NotificationCompat.Builder {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val pauseIntent = Intent(this, TimerService::class.java).apply { action = "PAUSE" }
        val pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val resumeIntent = Intent(this, TimerService::class.java).apply { action = "RESUME" }
        val resumePendingIntent = PendingIntent.getService(this, 0, resumeIntent, PendingIntent.FLAG_IMMUTABLE)

        val stopIntent = Intent(this, TimerService::class.java).apply { action = "STOP" }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer is Running")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.icono_tomate)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.pause, "Pause", pausePendingIntent)
            .addAction(R.drawable.play, "Resume", resumePendingIntent)
            .addAction(R.drawable.cancel, "Stop", stopPendingIntent)
    }

    private fun updateNotification(contentText: String) {
        val notification = createNotification(contentText).build()
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }
}
