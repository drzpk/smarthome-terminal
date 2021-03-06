package dev.drzepka.smarthome.terminal.server.domain.entity

import dev.drzepka.smarthome.terminal.server.domain.Configuration
import dev.drzepka.smarthome.terminal.server.domain.value.Category
import java.time.Instant

/**
 * Represents a terminal client - a backend service that is registered in the terminal server and exposes
 * a number of [Screen]s that allow to interface with the service.
 */
class Client(val id: Int, val name: String) {

    var categories: List<Category>? = null
        get() = if (field != null) ArrayList(field!!) else null
        set(value) {
            value?.also { validateCategories(it) }
            field = value
        }

    var lastPingTime = Instant.now()

    fun notifyActive() {
        synchronized(this) {
            lastPingTime = Instant.now()
        }
    }

    /**
     * Returns whether clients doesn't respond to server messages longer than specified threshold (mainly ping messages)
     */
    fun isClientInactive(): Boolean {
        synchronized(this) {
            return lastPingTime.plus(Configuration.maxClientInactivity).isBefore(Instant.now())
        }
    }

    private fun validateCategories(categories: Collection<Category>) {
        categories.groupBy { it.id }
            .forEach { (id, values) ->
                if (values.size > 1) {
                    throw IllegalArgumentException("Id $id occurs more than once")
                }
            }
    }

    override fun toString(): String = "Client(id=$id, name=\"$name\")"
}