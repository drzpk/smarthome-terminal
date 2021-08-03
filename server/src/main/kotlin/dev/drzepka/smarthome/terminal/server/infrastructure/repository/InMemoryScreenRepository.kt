package dev.drzepka.smarthome.terminal.server.infrastructure.repository

import dev.drzepka.smarthome.terminal.server.domain.entity.Screen
import dev.drzepka.smarthome.terminal.server.domain.repository.ScreenRepository
import java.util.concurrent.ConcurrentHashMap

class InMemoryScreenRepository : ScreenRepository {

    private val screens = ConcurrentHashMap<Int, MutableMap<Int, Screen>>()

    override fun get(clientId: Int, screenId: Int): Screen? {
        return screens[clientId]?.get(screenId)
    }

    override fun save(clientId: Int, screen: Screen) {
        val clientScreens = screens.computeIfAbsent(clientId) { ConcurrentHashMap() }
        if (clientScreens.size == CLIENT_SCREEN_LIMIT)
            throw IllegalStateException("Screen limit ($CLIENT_SCREEN_LIMIT) reached for client $clientId")

        clientScreens[screen.id] = screen
    }

    override fun delete(clientId: Int) {
        screens.remove(clientId)
    }

    companion object {
        private const val CLIENT_SCREEN_LIMIT = 10
    }
}