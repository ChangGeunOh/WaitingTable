package kr.pe.paran.waiting.ktor.server.routings

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kr.pe.paran.waiting.domain.model.SettingData
import kr.pe.paran.waiting.domain.use_cases.UsesCases

fun Routing.settingDataRouting(
    usesCases: UsesCases
) {
    route("settingData") {
        get {
            val settingData = usesCases.settingDataUseCase()
            call.respond(HttpStatusCode.Accepted, settingData)
        }
        post {
            val settingData = call.receive<SettingData>()
            usesCases.settingDataUseCase(settingData = settingData)
            call.respond(HttpStatusCode.Accepted)
        }
    }
}