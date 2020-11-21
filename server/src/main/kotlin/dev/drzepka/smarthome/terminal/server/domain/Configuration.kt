package dev.drzepka.smarthome.terminal.server.domain

import java.time.Duration

object Configuration {
    var clientPingInterval: Duration = Duration.ofSeconds(3)
    var maxClientInactivity: Duration = Duration.ofSeconds(10)
}