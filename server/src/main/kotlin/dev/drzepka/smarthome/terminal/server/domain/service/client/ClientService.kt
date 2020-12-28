package dev.drzepka.smarthome.terminal.server.domain.service.client

import dev.drzepka.smarthome.terminal.server.domain.entity.Client

interface ClientService {
    suspend fun findClient(id: Int): Client?
    suspend fun findClient(name: String, initialize: Boolean = true): Client?
    fun getAllClients(): Collection<Client>
    fun registerClient(clientName: String): Client
    fun unregisterClient(client: Client)
}