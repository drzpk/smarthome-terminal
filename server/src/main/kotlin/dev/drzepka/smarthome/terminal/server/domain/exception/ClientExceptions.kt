package dev.drzepka.smarthome.terminal.server.domain.exception

import dev.drzepka.smarthome.terminal.server.domain.entity.Client

class ClientAlreadyRegisteredException(client: Client) :
    IdentifiableException("Client '${client.name}' is already registered") {
    override val code: String = "client.already.registered"
}