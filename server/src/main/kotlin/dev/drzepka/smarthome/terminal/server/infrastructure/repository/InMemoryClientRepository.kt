package dev.drzepka.smarthome.terminal.server.infrastructure.repository

import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import java.util.concurrent.ConcurrentHashMap

class InMemoryClientRepository : ClientRepository {

    private val clients = ConcurrentHashMap<Int, Client>()

    override fun findById(id: Int): Client? {
        return clients[id]
    }

    override fun findByName(name: String): Client? {
        return clients.values.find { it.name == name }
    }

    override fun findAll(): Collection<Client> {
        return clients.values
    }

    override fun save(client: Client) {
        if (clients.containsKey(client.id))
            throw IllegalArgumentException("Client id=${client.id} already exists")

        clients[client.id] = client
    }

    override fun delete(client: Client) {
        clients.remove(client.id)
    }
}