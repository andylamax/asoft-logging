package com.asofttz.logging

import com.asofttz.persist.PaginatedRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

actual class Logger actual constructor(actual val source: String, actual val repo: PaginatedRepo<Log>?) : CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Default

    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG, msg, source)
        console.log("$log")
        log.send()
    }

    actual fun e(msg: String) {
        val log = Log(Log.Level.ERROR, msg, source)
        console.error("$log")
        log.send()
    }

    actual fun f(msg: String) {
        val log = Log(Log.Level.FAILURE, msg, source)
        console.error("$log")
        log.send()
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING, msg, source)
        console.warn("$log")
        log.send()
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO, msg, source)
        console.info("$log")
        log.send()
    }

    actual fun obj(vararg o: Any?) {
        console.log(o)
    }

    actual fun obj(o: Any?) = console.log(o)

    private fun Log.send() = launch {
        repo?.create(this@send)
    }
}