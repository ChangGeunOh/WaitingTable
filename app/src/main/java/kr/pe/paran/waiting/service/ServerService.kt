package kr.pe.paran.waiting.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.ktor.ServerNotification
import kr.pe.paran.waiting.ktor.ServerNotification.NOTIFICATION_ID
import kr.pe.paran.waiting.ktor.model.ServerAction
import kr.pe.paran.waiting.ktor.server.Server
import javax.inject.Inject

@AndroidEntryPoint
class ServerService : Service() {

    @Inject
    lateinit var server: Server

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Logcat.i("Receiver Action> ${intent?.action}")
        intent?.let {
            when(intent.action) {
                ServerAction.SERVER_START -> {
                    Logcat.i("Service> START")
                    server.start()
                    sendBroadcastState(true)
                    startForeground(NOTIFICATION_ID, ServerNotification.createNotification(this))
                }
                ServerAction.SERVER_STOP -> {
                    Logcat.i("Service> STOP")
                    server.stop()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                    sendBroadcastState(false)

                }
                else -> {}
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun sendBroadcastState(isStart: Boolean) {
        val intent = Intent().apply {
            action = ServerAction.SERVER_STATE
            putExtra("SERVER_STATE", isStart)
        }
        sendBroadcast(intent)
    }
}