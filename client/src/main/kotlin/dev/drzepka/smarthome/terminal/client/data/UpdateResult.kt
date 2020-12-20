package dev.drzepka.smarthome.terminal.client.data

data class UpdateResult(val status: UpdateStatus, val message: String? = null)

enum class UpdateStatus {
    UPDATED, ERROR, UNKNOWN
}