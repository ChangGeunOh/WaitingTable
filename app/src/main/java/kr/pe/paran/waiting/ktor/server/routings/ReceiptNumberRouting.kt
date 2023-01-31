package kr.pe.paran.waiting.ktor.server.routings

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kr.pe.paran.waiting.domain.model.ReceiptNumberData
import kr.pe.paran.waiting.domain.model.WaitingStatus
import kr.pe.paran.waiting.domain.use_cases.UsesCases

fun Routing.receiptNumber(
    usesCases: UsesCases
) {
    route("receiptNumber") {
        get {
            call.respondText("Get Waiting")
            val data = usesCases.settingDataUseCase()
        }
        post {
            val receiptNumberData = call.receive<ReceiptNumberData>()
            usesCases.receiptNumberUseCase(receiptNumberData)
            call.respond(HttpStatusCode.Accepted, "")
        }
        get("id/{id}") {
            call.parameters["id"]?.let { it ->
                val id = it.toInt()
                val receiptNumberData = usesCases.receiptNumberUseCase(id = id)
                call.respond(HttpStatusCode.Accepted, receiptNumberData)
            }
        }
        get("first/{isFirst}") {
            val isFirst = call.parameters["isFirst"].toBoolean()
            val receiptNumberData = usesCases.receiptNumberUseCase(isFirst = isFirst)
            call.respond(HttpStatusCode.Accepted, receiptNumberData)
        }
        get("status/{value}") {
            call.parameters["value"]?.let { value ->
                val waitingStatus = WaitingStatus.valueOf(value)
                val receiptNumberData =
                    usesCases.receiptNumberUseCase(waitingStatus = waitingStatus)
                call.respond(HttpStatusCode.Accepted, receiptNumberData)
            }
        }
        get("last") {
            val last = usesCases.receiptNumberUseCase()
            call.respond(HttpStatusCode.Accepted, last)
        }
    }

}
