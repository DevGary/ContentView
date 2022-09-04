package com.devgary.testcore

/**
 * Exception that should only be thrown in test code and could maybe be ignored
 */
class TestException(message: String? = null) : Exception(message)