package dev.drzepka.smarthome.terminal.server.domain.repository

import dev.drzepka.smarthome.terminal.server.domain.entity.Client

interface ClientRepository {
    fun findById(id: Int): Client?
    fun findByName(name: String): Client?
    fun findAll(): Collection<Client>
    fun save(client: Client)
    fun delete(client: Client)
}