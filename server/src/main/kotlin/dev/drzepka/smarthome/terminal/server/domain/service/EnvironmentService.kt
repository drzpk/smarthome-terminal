package dev.drzepka.smarthome.terminal.server.domain.service

interface EnvironmentService {
    fun isDev(): Boolean
    fun isProd(): Boolean = !isDev()
}