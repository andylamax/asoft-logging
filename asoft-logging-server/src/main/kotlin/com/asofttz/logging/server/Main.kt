package com.asofttz.logging.server

import com.asofttz.logging.Log
import com.asofttz.logging.Logger
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CORS
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.serialization.json.JSON
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

private val packages = arrayOf(
        Log::class.java.`package`.name
)

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

        get("/test/log/{msg}") {
            val log = Log(msg = call.parameters["msg"]!!)
            Neo4j.saveLog(log)
            call.respondHtml {
                head { title { +"Logs" } }
                body { section { h1 { +log.toString() } } }
            }
        }

        post("/log") {
            val log = JSON.parse(Log.serializer(), call.receive())
            log.log()
            Neo4j.saveLog(log)
            call.respond(HttpStatusCode.OK)
        }
    }
}

object Neo4j {
    private val configuration = Configuration.Builder()
            .uri("bolt://localhost:8082")
            .credentials("andylamax", "andymamson")
            .build()

    private val sessionFactory = SessionFactory(configuration,
            Log::class.java.`package`.name
    )

    fun saveLog(log: Log) = synchronized(this) {
        runBlocking {
            with(sessionFactory.openSession()) {
                save(log)
                clear()
            }
        }
    }
}