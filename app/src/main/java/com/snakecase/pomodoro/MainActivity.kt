package com.snakecase.pomodoro

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {

    private var timerService: TimerService? = null
    private var isBound = false

    private val PERMISSION_REQUEST_CODE = 123
    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val aplicacion = AplicacionPomodoro()

        val permissionsToRequest = mutableListOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE)
        }

        if (permissionsToRequest.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            bindTimerService()
            writeToFile()
        }

        setContent {
            aplicacion.EjecutarAplicacion(savedInstanceState = savedInstanceState)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                    break
                }
            }

            if (allGranted) {
                bindTimerService()
                writeToFile()
            } else {
                Log.e("MainActivity", "One or more permissions denied")
            }
        }
    }

    private fun bindTimerService() {
        val intent = Intent(this, TimerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun writeToFile() {
        val externalStorageDir = Environment.getExternalStorageDirectory()
        val appDir = File(externalStorageDir, "pomodoro")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val file = File(appDir, "testfile.txt")
        try {
            FileOutputStream(file).use { fos ->
                fos.write("Hello, World!".toByteArray())
                Log.d("MainActivity", "File written successfully")
            }
        } catch (e: IOException) {
            Log.e("MainActivity", "Failed to write file", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    fun startPomodoro(duration: Long) {
        val serviceIntent = Intent(this, TimerService::class.java).apply { action = "START" }
        serviceIntent.putExtra("duration", duration)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
