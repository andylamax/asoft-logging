package com.asofttz.logging

import kotlin.test.BeforeTest

class LoggerTest {
    @BeforeTest
    fun init() {
        val log = Logger("Products")
        log.d("This is a debug test")
    }
}
