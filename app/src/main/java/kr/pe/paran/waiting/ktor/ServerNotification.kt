@file:OptIn(ExperimentalComposeUiApi::class)

package kr.pe.paran.waiting.ktor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import kr.pe.paran.waiting.MainActivity
import kr.pe.paran.waiting.R
import kr.pe.paran.waiting.ktor.model.ServerAction
import kr.pe.paran.waiting.receiver.MyReceiver

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalPagerApi::class
)
object ServerNotification {

    const val NOTIFICATION_ID = 10
    const val CHANNEL_ID = "foreground_service_channel" // 임의의 채널 ID

    fun createNotification(
        context: Context
    ): Notification {

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent
            .getActivity(context, 0, notificationIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val stopIntent = Intent(context, MyReceiver::class.java).apply {
            action = ServerAction.SERVER_STOP
        }
        val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("WebSocket Server")
            .setContentText("WebSocket Server is Running...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)                                           // true 일경우 알림 리스트에서 클릭하거나 좌우로 드래그해도 사라지지 않음
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_pause,
                    "STOP",
                    stopPendingIntent
                )
            )
            .setContentIntent(pendingIntent)
            .build()


        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "WebSocket Server Notification", // 채널표시명
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(serviceChannel)

        return notification
    }
}