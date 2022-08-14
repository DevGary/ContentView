package com.devgary.contentcore.util

import java.util.concurrent.TimeUnit

fun Long.secondsToMillis(): Long = TimeUnit.SECONDS.toMillis(this) 
fun Int.secondsToMillis(): Long = TimeUnit.SECONDS.toMillis(this.toLong()) 
