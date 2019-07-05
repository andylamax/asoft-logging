package tz.co.asoft.logging

import tz.co.asoft.logging.tools.Cause
import kotlin.test.Test

class LogTest {
    private val log = Logger("test-logger")

    @Test
    fun info() {
        log.i("This is a test info")
    }

    @Test
    fun debug() {
        log.d("Debugging some shit here")
    }

    @Test
    fun errorMessage() {
        log.e("This is a test error message")
    }

    @Test
    fun errorException() {
        log.e(Cause("This is a test error exception"))
    }

    @Test
    fun errorNull() {
        log.e(null)
    }

    @Test
    fun failureMessage(){
        log.f("This is a test failure message")
    }

    @Test
    fun failureException() {
        log.f(Cause("This is a test failure exception"))
    }

    @Test
    fun failureNull() {
        log.f(null)
    }

    @Test
    fun warning() {
        log.w("This is a warning test")
    }
}