package kr.pe.paran.waiting.ktor.server.routings

import io.ktor.server.routing.*
import kr.pe.paran.waiting.domain.use_cases.UsesCases

fun Routing.initReceiptNumberRouting(
    usesCases: UsesCases
) {
    route("initReceiptNumber") {
       get {
           usesCases.initReceiptNumberUseCase()
       }
    }
}