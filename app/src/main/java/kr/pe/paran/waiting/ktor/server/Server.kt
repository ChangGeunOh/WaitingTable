package kr.pe.paran.waiting.ktor.server

import android.content.Context
import android.widget.Toast
import io.ktor.events.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kr.pe.paran.waiting.common.utils.Logcat
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.ktor.server.plugins.configureRouting
import kr.pe.paran.waiting.ktor.server.plugins.configureSerialization
import javax.inject.Inject

class Server @Inject constructor(
    private val context: Context,
    private val usesCases: UsesCases
) {

    private var server: NettyApplicationEngine? = null

    private var running = false

    private fun getServerEngine(host: String = "0.0.0.0", port: Int): NettyApplicationEngine {

        return embeddedServer(
            Netty,
            port = port,
            host = host,
            module = {
                module(context = context, usesCases = usesCases)
            }
        )
    }

    fun start() {
        CoroutineScope(Dispatchers.Default).launch {
            val settingData = usesCases.settingDataUseCase.invoke()
            Logcat.i(settingData.toString())
            server = getServerEngine(port = settingData.port)
            if (!running) {
                try {
                    server!!.start(wait = true)
                    Toast.makeText(context, "Start Server...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                running = true
            }
            isRunning.emit(true)
        }
    }

    fun stop() {
        if (running) {
            CoroutineScope(Dispatchers.Default).launch {
                server?.stop(1000, 1000)
                server = null
                running = false
                isRunning.emit(false)
            }
            Toast.makeText(context, "Stop Server...", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val isRunning = MutableSharedFlow<Boolean>(0)
    }
}

fun Application.module(
    context: Context,
    usesCases: UsesCases
) {
//    configureWebSockets()
    configureSerialization()
    configureRouting(context = context, usesCases = usesCases)
}
