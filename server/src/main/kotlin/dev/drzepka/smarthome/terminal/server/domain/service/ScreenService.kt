package dev.drzepka.smarthome.terminal.server.domain.service

import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.common.transport.message.GetScreenMessage
import dev.drzepka.smarthome.terminal.common.util.Logger
import dev.drzepka.smarthome.terminal.server.domain.entity.Client

class ScreenService(private val terminalQueue: TerminalQueue) {

    private val log by Logger()

    suspend fun getScreen(client: Client, categoryId: Int): ScreenModel? {
        log.info("Getting screen id={} from {}", categoryId, client)
        val message = GetScreenMessage().apply {
            this.categoryId = categoryId
        }

        val response = terminalQueue.putMessage(client, message)
        if (response.screen == null) {
            log.error("Screen id={} wasn't found in {}", categoryId, client)
            return null
        } else if (response.screen?.id != categoryId) {
            log.error("Returned screen id={} doesn't match requested screen id={}", response.screen?.id, categoryId)
            return null
        }

        return response.screen
    }
}