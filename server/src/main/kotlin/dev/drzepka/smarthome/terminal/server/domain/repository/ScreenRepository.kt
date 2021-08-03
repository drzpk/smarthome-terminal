package dev.drzepka.smarthome.terminal.server.domain.repository

import dev.drzepka.smarthome.terminal.server.domain.entity.Screen

interface ScreenRepository {
    fun get(clientId: Int, screenId: Int): Screen? // todo: shouldn't the screenId be unique?
    fun save(clientId: Int, screen: Screen)
    fun delete(clientId: Int)
}