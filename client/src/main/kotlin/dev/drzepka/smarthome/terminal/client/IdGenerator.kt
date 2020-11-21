package dev.drzepka.smarthome.terminal.client

/**
 * Generates identificators for all objects used by clients.
 */
object IdGenerator {
    private const val MAX_ID = 65_536

    private var currentId = 1

    @Synchronized
    fun nextId(): Int {
        if (currentId == MAX_ID)
            throw IllegalStateException("Id pool has been depleted")
        return currentId++
    }
}