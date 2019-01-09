package com.asofttz.logging.server

import com.asofttz.logging.Logger
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CORS
import io.ktor.html.respondHtml
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.html.*
import kotlinx.serialization.ImplicitReflectionSerializer

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    install(CORS) {
        anyHost()
    }

    val client = HttpClient(Apache)

    val logger = Logger("asoft-logging-server")

    routing {
        get("/logs") {
            call.respondHtml {
                head { title { +"Logs" } }
                body { section { h1 { +"Logs Comming Ya'll" } } }
            }
        }

        get("/logs/{start_time}/{end_time}") {
            val startTime = call.parameters["start_time"]

            val endTime = call.parameters["end_time"]

            call.respondHtml {
                head { title { +"Logs" } }
                body {
                    section {
                        h1 {
                            +"Logs Comming Ya'll"
                        }

                        div { +"From $startTime" }
                        div { +"To $endTime" }
                    }
                }
            }
        }

        post("/log") {
            call.respond(HttpStatusCode.OK)
        }
    }
}