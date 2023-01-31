package kr.pe.paran.waiting.ktor.server.routings

import android.util.Log
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Routing.static(
    path: String,
    parentPath: String,
    url: String
) {
    File(path).listFiles()?.forEach { file ->

        if (file.isDirectory) {
            val routeUrl = url + file.absolutePath.substring(parentPath.length) + "/{filename}"
            Log.i(":::::", routeUrl)

            this.get(routeUrl) {
                val fileName = call.parameters["filename"]
                val targetFile = File("${file.absolutePath}/$fileName")
                if (targetFile.exists()) {
                    call.respondFile(targetFile)
                } else {
                    call.respond(status = HttpStatusCode.NotFound, "")
                }

            }

            static(file.absolutePath, parentPath, url)
        }
    }

}