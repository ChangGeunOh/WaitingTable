package kr.pe.paran.waiting.ktor.server.plugins

import android.content.Context
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kr.pe.paran.waiting.domain.use_cases.UsesCases
import kr.pe.paran.waiting.ktor.server.routings.*

fun Application.configureRouting(context: Context, usesCases: UsesCases) {

    routing {
        get("/") {
            call.respondText("Welcome to Android TCP Server...")
        }


        val staticFileDirectory = context.filesDir.absolutePath
        static(staticFileDirectory, staticFileDirectory, "")
        receiptNumber(usesCases = usesCases)
        displayWaitingRouting(usesCases = usesCases)
        initReceiptNumberRouting(usesCases = usesCases)
        managerWaiting(usesCases = usesCases)
        settingDataRouting(usesCases = usesCases)
        waitingCount(usesCases = usesCases)
        numberPad(usesCases = usesCases)
    }
}