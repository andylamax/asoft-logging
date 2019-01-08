package com.asofttz.logging

actual class Logger actual constructor(actual val source: String) {

    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG, msg, source)
        console.log("$log")
    }

    actual fun e(msg: String) {
        val log = Log(Log.Level.ERROR, msg, source)
        console.error("$log")
    }

    actual fun f(msg: String) {
        val log = Log(Log.Level.FAILURE, msg, source)
        console.error("$log")
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING, msg, source)
        console.warn("$log")
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO, msg, source)
        console.info("$log")
    }
}