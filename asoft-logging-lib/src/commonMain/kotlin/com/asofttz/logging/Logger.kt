package com.asofttz.logging

expect class Logger(source: String = "anonymous") {
    val source: String
    fun d(msg: String)
    fun e(msg: String)
    fun f(msg: String)
    fun w(msg: String)
    fun i(msg: String)
}