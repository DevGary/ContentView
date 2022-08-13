package com.devgary.contentviewdemo.util

import kotlinx.coroutines.Job

fun Job?.cancel() {
    this?.cancel()
}