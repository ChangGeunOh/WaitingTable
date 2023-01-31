package kr.pe.paran.waiting.ktor.server.routings

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.first
import kr.pe.paran.waiting.domain.use_cases.UsesCases

fun Routing.numberPad(
    usesCases: UsesCases
) {
    route("numberPad") {
        get {
            val numberPadData = usesCases.numberPadUseCase().first()
            call.respond(HttpStatusCode.Accepted, numberPadData)
        }
    }
}