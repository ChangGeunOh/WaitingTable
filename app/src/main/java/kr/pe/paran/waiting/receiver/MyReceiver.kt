package kr.pe.paran.waiting.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.service.ServerService

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Logcat.i("Receiver Action> ${intent.action}")
        val serviceIntent = Intent(context, ServerService::class.java).apply {
            action = intent.action
        }
        context.startService(serviceIntent)
    }
}