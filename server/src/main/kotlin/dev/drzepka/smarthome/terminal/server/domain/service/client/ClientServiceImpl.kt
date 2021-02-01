package dev.drzepka.smarthome.terminal.server.domain.service.client

import dev.drzepka.smarthome.terminal.common.transport.message.GetCategoriesMessage
import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.domain.Configuration
import dev.drzepka.smarthome.terminal.server.domain.converter.ConversionService
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import dev.drzepka.smarthome.terminal.server.domain.exception.ClientAlreadyRegisteredException
import dev.drzepka.smarthome.terminal.server.domain.repository.ClientRepository
import dev.drzepka.smarthome.terminal.server.domain.service.Scheduler
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.domain.value.Category
import java.util.concurrent.atomic.AtomicInteger

open class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val terminalQueue: TerminalQueue,
    private val conversionService: ConversionService,
    scheduler: Scheduler
) : ClientService {

    private val log by Logger()

    private val clientIdCounter = AtomicInteger()

    init {
        scheduler.registerScheduledTask("client ping test", Configuration.clientPingInterval) {
            checkClientPing()
        }
    }

    override suspend fun findClient(id: Int): Client? {
        log.trace("Looking for client id={}", id)
        val client = clientRepository.findById(id) ?: return null

        log.trace("{} found", client)
        initializeClient(client)
        return client
    }

    override suspend fun findClient(name: String, initialize: Boolean): Client? {
        log.trace("Looking for client name='{}'", name)
        val client = clientRepository.findAll().firstOrNull { it.name == name } ?: return null

        log.trace("{} found", client)
        if (initialize)
            initializeClient(client)
        return client
    }

    override fun getAllClients(): Collection<Client> {
        log.debug("Getting all clients")
        return clientRepository.findAll()
    }

    override fun registerClient(clientName: String): Client {
        log.info("Registering client {}", clientName)
        clientRepository.findByName(clientName)?.apply {
            throw ClientAlreadyRegisteredException(this)
        }

        val nextId = clientIdCounter.incrementAndGet()
        val client = Client(nextId, clientName)
        log.info("Registered {}", client)

        clientRepository.save(client)
        return client
    }

    override fun unregisterClient(client: Client) {
        log.info("Unregistering {}", client)
        clientRepository.delete(client)
    }

    private suspend fun initializeClient(client: Client) {
        if (client.categories == null) {
            log.info("{} doesn't have categories, downloading", client)
            val response = terminalQueue.putMessage(client, GetCategoriesMessage())
            val categories = response.categories.map { conversionService.convert<Category>(it) }

            log.info("Downloaded {} categories for {}, updating the entity", categories.size, client)
            client.categories = categories
        }
    }

    /**
     * Executes perdiodical actions on clients.
     */
    private fun checkClientPing() {
        log.trace("Checking clients ping")

        for (client in clientRepository.findAll()) {
            if (client.isClientInactive()) {
                log.info("$client is inactive for ${Configuration.maxClientInactivity} and will be unregistered")
                unregisterClient(client)
            }
        }
    }
}