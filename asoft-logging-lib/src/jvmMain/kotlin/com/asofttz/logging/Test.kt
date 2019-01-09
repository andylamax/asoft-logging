package com.asofttz.logging


fun main() {
    val log = Log(Log.Level.ERROR, "Error Testing JVM")
    pushToServer("http://localhost:8080/log", log)
}