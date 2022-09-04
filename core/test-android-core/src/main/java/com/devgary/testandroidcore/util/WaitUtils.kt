package com.devgary.testandroidcore.util

import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import com.devgary.testcore.TestException

object WaitUtils {
    enum class ConditionStatus {
        CONDITION_NOT_MET,
        CONDITION_MET,
        TIMEOUT,
    }

    /**
     * Causes current thread to [Thread.sleep] until [condition] is true or after [timeoutMillis] duration
     * 
     * @param [condition] Condition to wait for
     * @param [timeoutMillis] Duration until exception is thrown if [condition] not met 
     * @param [checkIntervalMillis] How long to wait between each [condition] check
     */
    fun waitFor(timeoutMillis: Int = 5000, checkIntervalMillis: Int = 250, condition: () -> Boolean) {
        var conditionStatus = ConditionStatus.CONDITION_NOT_MET
        val startTime = System.currentTimeMillis()
        
        do {
            if (condition()) {
                conditionStatus = ConditionStatus.CONDITION_MET
            } 
            else if ((System.currentTimeMillis() - startTime) >= timeoutMillis) {
                conditionStatus = ConditionStatus.TIMEOUT
                break
            }
            else {
                sleep(checkIntervalMillis.toLong())
            }
        } while (conditionStatus != ConditionStatus.CONDITION_MET)
        if (conditionStatus == ConditionStatus.TIMEOUT) throw TestException("Timed out after $timeoutMillis ms.")
    }

    fun waitForShortTimeout(condition: () -> Boolean) {
        waitFor(timeoutMillis = 500, condition = condition)
    }

    fun waitForMediumTimeout(condition: () -> Boolean) {
        waitFor(timeoutMillis = 2000, condition = condition)
    }

    fun waitForLongTimeout(condition: () -> Boolean) {
        waitFor(timeoutMillis = 5000, condition = condition)
    }
    
    fun withDelay(delay: Int, action: () -> Unit) {
        sleep(delay.toLong())
        action()
    }
}