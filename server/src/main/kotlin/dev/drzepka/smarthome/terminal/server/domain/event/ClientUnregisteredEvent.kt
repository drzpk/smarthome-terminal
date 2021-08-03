package dev.drzepka.smarthome.terminal.server.domain.event

import dev.drzepka.smarthome.terminal.server.domain.entity.Client

data class ClientUnregisteredEvent(val client: Client) : Event