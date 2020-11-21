package dev.drzepka.smarthome.terminal.server.domain.service

import kotlinx.coroutines.CoroutineScope
import java.time.Duration

interface Scheduler {
    fun registerScheduledTask(taskName: String, period: Duration, handler: suspend CoroutineScope.() -> Unit)
}