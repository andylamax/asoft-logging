package com.asofttz.logging.server

import com.asofttz.logging.Log
import com.asofttz.logging.Logger
import com.asofttz.logging.data.viewmodal.LogViewModal
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CORS
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import kotlinx.serialization.stringify
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

private val viewModal = injection.viewModal

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    install(CORS) {
        anyHost()
    }

    val logger = Logger("asoft-logging-server")

    routing {
        get("/logs") {
            val logs = viewModal.getLogs().value
            val logsJson = JSON.indented.stringify(Log.serializer().list, logs)
            call.respondJson(logsJson)
        }

        post("/log") {
            val log = JSON.parse(Log.serializer(), call.receive())
            log.log()
            viewModal.saveLog(log)
            call.respond(HttpStatusCode.OK)
        }
    }
}

suspend fun ApplicationCall.respondJson(json: String) = respondText(json, ContentType.Application.Json)

//object Neo4j {
//    private val configuration = Configuration.Builder()
//            .uri("bolt://localhost:8082")
//            .credentials("andylamax", "andymamson")
//            .build()
//
//    private val sessionFactory = SessionFactory(configuration,
//            Log::class.java.`package`.name
//    )
//
//    fun saveLog(log: Log) = synchronized(this) {
//        runBlocking {
//            with(sessionFactory.openSession()) {
//                save(log)
//                clear()
//            }
//        }
//    }
//
//    fun getLogs() = synchronized(this) {
//        with(sessionFactory.openSession()) {
//            loadAll(Log::class.java)
//        }
//    }
//}