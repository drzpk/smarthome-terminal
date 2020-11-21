package dev.drzepka.smarthome.terminal.server.infrastructure.service

import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.domain.service.Scheduler
import dev.drzepka.smarthome.terminal.server.infrastructure.ShutdownAware
import kotlinx.coroutines.*
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class SchedulerImpl : Scheduler, ShutdownAware {
    private val log by Logger()

    private var working = AtomicBoolean(true)
    private val dispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(dispatcher + SupervisorJob())

    override fun onShutdown() {
        dispatcher.close()
    }

    override fun registerScheduledTask(taskName: String, period: Duration, handler: suspend CoroutineScope.() -> Unit) {
        log.info("Registering scheudled task $taskName")

        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            log.error("Error while executing task $taskName", exception)
        }

        val delayMs = period.get(ChronoUnit.SECONDS) * 1000L
        coroutineScope.launch(exceptionHandler) {
            while (working.get()) {
                delay(delayMs)
                handler()
            }
        }
    }
}