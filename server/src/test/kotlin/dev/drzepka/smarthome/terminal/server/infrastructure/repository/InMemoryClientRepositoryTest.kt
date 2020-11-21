package dev.drzepka.smarthome.terminal.server.infrastructure.repository

import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import org.assertj.core.api.BDDAssertions.assertThatExceptionOfType
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class InMemoryClientRepositoryTest {

    @Test
    fun `should store client`() {
        val repository = InMemoryClientRepository()
        val client = Client(1, "name")

        repository.save(client)
        then(repository.findById(1)).isSameAs(client)

        repository.delete(client)
        then(repository.findById(1)).isNull()
    }

    @Test
    fun `should find client by name`() {
        val repository = InMemoryClientRepository()
        val client1 = Client(1, "name1")
        val client2 = Client(2, "name2")

        repository.save(client1)
        repository.save(client2)
        then(repository.findByName("name2")).isSameAs(client2)
    }

    @Test
    fun `should throw exception if client with the same id already exists`() {
        val repository = InMemoryClientRepository()
        val client = Client(1, "name")

        repository.save(client)

        assertThatExceptionOfType(Exception::class.java).isThrownBy {
            repository.save(client)
        }.withMessageMatching("Client id=\\d+ already exists")
    }
}