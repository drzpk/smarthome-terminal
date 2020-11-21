package dev.drzepka.smarthome.terminal.server.infrastructure

interface ShutdownAware {
    fun onShutdown()
}